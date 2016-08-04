/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.kafka.SimpleSouth;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

/**
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class South
{
  @SuppressWarnings("InfiniteLoopStatement")
  public static void main(String[] commandLine)
    throws IOException, JAXBException
  {
    if(flags.parse(commandLine))
    {
      if(log.isInfoEnabled())
      {
        log.info("Starting TESOBE's OBP API South Demo ...");
      }

      String consumerProps = flags.valueOf(flags.consumerProps);
      String consumerTopic = flags.valueOf(flags.consumerTopic);
      String producerProps = flags.valueOf(flags.producerProps);
      String producerTopic = flags.valueOf(flags.producerTopic);

      Transport.Factory factory = Transport.defaultFactory()
        .orElseThrow(RuntimeException::new); // highly unlikely
      Receiver responder = new DemoData(factory.decoder(), factory.encoder());
      SimpleSouth south = new SimpleSouth(consumerTopic, producerTopic,
        new Props(South.class, consumerProps).getProperties(),
        new Props(South.class, producerProps).getProperties(), responder);

      south.receive();

      while(true)
      {
        log.trace("Parking main...");

        LockSupport.park(Thread.currentThread());
      }
    }
  }

  final static Flags flags = new Flags();
  final static Logger log = LoggerFactory.getLogger(South.class);
}
