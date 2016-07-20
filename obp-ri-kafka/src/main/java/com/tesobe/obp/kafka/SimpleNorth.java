/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.kafka;

import org.slf4j.MDC;

public class SimpleNorth extends SimpleTransport
{
  public SimpleNorth(String producerTopic, String consumerTopic)
  {
    super(consumerTopic, producerTopic);
  }
}
