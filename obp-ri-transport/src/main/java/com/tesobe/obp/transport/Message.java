/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

/**
 * Lightweight, trusting packet for the transport layer to use.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class Message
{
  public Message(String id, String payload)
  {
    this.id = id;
    this.payload = payload;
  }

  @Override public String toString()
  {
    return id + " " + payload;
  }

  public final String id;
  public final String payload;
}
