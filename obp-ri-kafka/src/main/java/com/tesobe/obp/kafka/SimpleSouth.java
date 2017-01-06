/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.spi.Receiver;

import java.io.IOException;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class SimpleSouth
  extends SimpleTransport
{
  public SimpleSouth(Configuration c, Receiver r) throws IOException
  {
    super(c);

    receiver = r;
  }

  @Override protected void receive(String key, String value)
    throws InterruptedException
  {
    log.trace("{} {} {}", topicToReceiveFrom(), key, value);

    String response = receiver.respond(new Message(key, value));

    send(key, response);
  }

  @Override protected String topicToSendTo()
  {
    return producerTopic;
  }

  @Override protected String topicToReceiveFrom()
  {
    return consumerTopic;
  }

  protected final Receiver receiver;
}
