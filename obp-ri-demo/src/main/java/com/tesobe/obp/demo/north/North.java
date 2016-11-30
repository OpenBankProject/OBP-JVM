/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.kafka.SimpleNorth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * A simple REST server that uses the kafka transport to send messages.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class North extends SimpleNorth
{
  public North(String consumerTopic, String producerTopic,
    Map<String, Object> consumerProps, Map<String, Object> producerProps)
  {
    super(consumerTopic, producerTopic, consumerProps, producerProps);
  }

  public static void main(String[] commandLine) throws IOException
  {
    if(flags.parse(commandLine))
    {
      log.info("Starting TESOBE's OBP North Demo REST Server...");

//      String consumerProps = flags.valueOf(flags.consumerProps);
//      String consumerTopic = flags.valueOf(flags.consumerTopic);
//      String producerProps = flags.valueOf(flags.producerProps);
//      String producerTopic = flags.valueOf(flags.producerTopic);
//      String ipAddress = flags.valueOf(flags.ipAddress);
//      int port = flags.valueOf(flags.port);
//
//      Transport.Factory factory = Transport.defaultFactory();
//      North north = new North(consumerTopic, producerTopic,
//        new Props(North.class, consumerProps).toMap(),
//        new Props(South.class, producerProps).toMap());
//
//      north.receive();
    }
  }

  final static Flags flags = new Flags();
  final static Logger log = LoggerFactory.getLogger(North.class);
}
