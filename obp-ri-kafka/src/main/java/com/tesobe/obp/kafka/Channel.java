/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Todo support for multiple producers.
 */
@SuppressWarnings("WeakerAccess") class Channel
{
  public String take(String id) throws InterruptedException
  {
    String[] packet = in.take();

    if(packet == null || packet.length < 2 || !id.equals(packet[0]))
    {
      throw new RuntimeException(id); // todo fix
    }

    return packet[1];
  }

  public void put(String id, String payload) throws InterruptedException
  {
    in.put(new String[]{id, payload});
  }

  protected final BlockingQueue<String[]> in = new SynchronousQueue<>();

  static final Logger log = LoggerFactory.getLogger(Channel.class);
}
