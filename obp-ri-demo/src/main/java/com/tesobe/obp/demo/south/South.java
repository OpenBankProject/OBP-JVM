/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.kafka.Configuration;
import com.tesobe.obp.kafka.SimpleConfiguration;
import com.tesobe.obp.kafka.SimpleSouth;
import com.tesobe.obp.kafka.SimpleTransport;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.LoggingReceiver;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class South
{
  public South(Responder r, String consumerTopic, String consumerProps,
    String producerTopic, String producerProps) throws IOException
  {
    this(r, new SimpleConfiguration(consumerProps, consumerTopic, producerProps,
      producerTopic));
  }

  public South(Responder r, Configuration c) throws IOException
  {
    log.info("Starting TESOBE's OBP kafka south demo...");

    Transport.Factory factory = Transport.defaultFactory();
    Receiver receiver = new ReceiverNov2016(r, factory.codecs());
    SimpleTransport transport = new SimpleSouth(c.consumerTopic(),
      c.producerTopic(), c.consumerProps(), c.producerProps(),
      new LoggingReceiver(receiver));

    transport.receive();
  }

  final static Logger log = LoggerFactory.getLogger(South.class);
}
