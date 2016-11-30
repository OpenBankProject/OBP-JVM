/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class South
{
  @SuppressWarnings("InfiniteLoopStatement")
  public static void main(String[] commandLine)
    throws IOException, JAXBException
  {
    if(flags.parse(commandLine))
    {
      log.info("Starting TESOBE's OBP South Demo...");
//
//      String consumerProps = flags.valueOf(flags.consumerProps);
//      String consumerTopic = flags.valueOf(flags.consumerTopic);
//      String producerProps = flags.valueOf(flags.producerProps);
//      String producerTopic = flags.valueOf(flags.producerTopic);
//
//      Transport.Factory factory = Transport.defaultFactory();
//      Receiver receiver = new DemoReceiver(factory.codecs(),
//        DemoDatabase.simple());
//      SimpleSouth south = new SimpleSouth(consumerTopic, producerTopic,
//        new Props(South.class, consumerProps).toMap(),
//        new Props(South.class, producerProps).toMap(),
//        new LoggingReceiver(receiver));
//
//      south.receive();
//
//      while(true)
//      {
//        log.trace("Parking main...");
//
//        LockSupport.park(Thread.currentThread());
//      }
    }
  }

  final static Flags flags = new Flags();
  final static Logger log = LoggerFactory.getLogger(South.class);
}
