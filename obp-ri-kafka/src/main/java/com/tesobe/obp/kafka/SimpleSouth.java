/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.spi.Receiver;

import java.util.Properties;

/**
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class SimpleSouth
  extends SimpleTransport
{
  public SimpleSouth(String consumerTopic, String producerTopic, Receiver r)
  {
    super(consumerTopic, producerTopic);

    receiver = r;
  }

  public SimpleSouth(String consumerTopic, String producerTopic,
    Properties consumerProps, Properties producerProps, Receiver r)
  {
    super(consumerTopic, producerTopic, consumerProps, producerProps);

    receiver = r;
  }

  @Override protected void receive(String key, String value)
    throws InterruptedException
  {
    log.trace("{} {} {}", consumerTopic, key, value);

    String response = receiver.respond(new Message(key, value));

    send(key, response);
  }

  protected final Receiver receiver;
}
