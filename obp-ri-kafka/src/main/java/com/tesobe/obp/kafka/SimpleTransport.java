/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class SimpleTransport implements Sender
{
  /**
   * Use default properties for consumer and producer.
   *
   * @param consumerTopic required
   * @param producerTopic required
   */
  public SimpleTransport(String consumerTopic, String producerTopic)
  {
    this(consumerTopic, producerTopic, Collections.emptyMap(),
      Collections.emptyMap());
  }

  /**
   * Use default properties for consumer and producer for those keys that are
   * not provided as argument.
   *
   * @param consumerTopic required
   * @param producerTopic required
   * @param consumerProps override selected kafka config keys
   * @param producerProps override selected kafka config keys
   */
  public SimpleTransport(String consumerTopic, String producerTopic,
    Map<String, ?> consumerProps, Map<String, ?> producerProps)
  {
    this.consumerTopic = consumerTopic;
    this.producerTopic = producerTopic;

    HashMap<String, Object> cp = new HashMap<>();
    HashMap<String, Object> pp = new HashMap<>();

    cp.putAll(consumer());

    if(consumerProps != null)
    {
      cp.putAll(consumerProps);
    }

    pp.putAll(producer());

    if(producerProps != null)
    {
      pp.putAll(producerProps);
    }

    consumer = new KafkaConsumer<>(cp);
    producer = new KafkaProducer<>(pp);
  }

  public SimpleTransport(Configuration c) throws IOException
  {
    this(c.consumerTopic(), c.producerTopic(), c.consumerProps(),
      c.producerProps());
  }

  @Override public String send(Message request) throws InterruptedException
  {
    send(request.id, request.payload);

    return in.take(request.id);
  }

  protected void send(String key, String value)
  {
    MDC.put("kafka", getClass().getSimpleName());

    String topic = topicToSendTo();

    log.trace("{} {} {}", topic, key, value);

    producer.send(new ProducerRecord<>(topic, key, value));

    MDC.remove("kafka");
  }

  protected String topicToSendTo()
  {
    return producerTopic;
  }

  protected String topicToReceiveFrom()
  {
    return consumerTopic;
  }

  @SuppressWarnings("InfiniteLoopStatement") public void receive()
  {
    final String type = getClass().getSimpleName();
    final String topic = topicToReceiveFrom();

    service.submit(new Callable<Void>() // lambda fails to compile here
    {
      @Override public Void call()
      {
        MDC.put("kafka", type);

        log.trace("Starting consumer on {}", topic);

        consumer.subscribe(Collections.singletonList(topic));

        while(!done.get())
        {
          try
          {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for(ConsumerRecord<String, String> record : records)
            {
              receive(record.key(), record.value());
            }
          }
          catch(Exception e)
          {
            log.warn("{} consumer done: {}", topic, done);
          }
        }

        consumer.close();

        MDC.remove("kafka");

        return null;
      }
    });
  }

  protected void receive(String key, String value) throws InterruptedException
  {
    log.trace("{} {} {}", topicToReceiveFrom(), key, value);

    in.put(key, value);
  }

  public void shutdown()
  {
    done.set(true);

    consumer.wakeup();
    producer.close();
    service.shutdown();
  }

  /**
   * Configure the kafka consumer.
   *
   * @return props to use for consumer when no properties file is given to ctor.
   */
  protected Map<String, Object> consumer()
  {
    Map<String, Object> props = new HashMap<>();

    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "test");
    props.put("enable.auto.commit", "true");
    props.put("session.timeout.ms", "30000");
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());

    return props;
  }

  /**
   * Configure the kafka producer.
   *
   * @return props to use for producer when no properties file is given to ctor.
   */
  protected Map<String, Object> producer()
  {
    Map<String, Object> props = new HashMap<>();

    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", StringSerializer.class.getName());

    return props;
  }

  static final Logger log = LoggerFactory.getLogger(SimpleTransport.class);
  protected final String consumerTopic;
  protected final String producerTopic;
  protected final KafkaConsumer<String, String> consumer;
  protected final KafkaProducer<String, String> producer;
  protected final AtomicBoolean done = new AtomicBoolean(false);
  protected final ExecutorService service = Executors.newSingleThreadExecutor();
  protected final Channel in = new Channel();
}
