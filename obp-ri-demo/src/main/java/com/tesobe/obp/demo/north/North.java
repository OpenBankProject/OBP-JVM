/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.demo.south.South;
import com.tesobe.obp.kafka.SimpleNorth;
import com.tesobe.obp.kafka.SimpleTransport;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * A simple REST server that uses the kafka transport to send messages.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class North
{
  public North(String consumerTopic, String producerTopic, String consumerProps,
    String producerProps) throws IOException
  {
    log.info("Starting TESOBE's OBP kafka north demo...");

    Transport.Factory factory = Transport.defaultFactory();
    SimpleTransport transport = new SimpleNorth(consumerTopic, producerTopic,
      new Props(North.class, consumerProps).toMap(),
      new Props(South.class, producerProps).toMap());

    connector = factory.connector(transport);

    transport.receive();
  }

  public List<Bank> getBanks() throws InterruptedException
  {
    return StreamSupport.stream(connector.getBanks().spliterator(), false)
      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  final static Logger log = LoggerFactory.getLogger(North.class);
  final Connector connector;
}
