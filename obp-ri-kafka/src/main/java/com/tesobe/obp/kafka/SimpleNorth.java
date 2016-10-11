/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import java.util.Map;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class SimpleNorth
  extends SimpleTransport
{
  public SimpleNorth(String consumerTopic, String producerTopic)
  {
    super(consumerTopic, producerTopic);
  }

  public SimpleNorth(String consumerTopic, String producerTopic,
    Map<String, Object> consumerProps, Map<String, Object> producerProps)
  {
    super(consumerTopic, producerTopic, consumerProps, producerProps);
  }

  @Override protected String topicToSendTo()
  {
    return producerTopic;
  }

  @Override protected String topicToReceiveFrom()
  {
    return consumerTopic;
  }

}
