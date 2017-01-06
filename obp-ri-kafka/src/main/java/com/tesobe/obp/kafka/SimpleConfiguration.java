/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.util.Props;

import java.io.IOException;
import java.util.Map;

public class SimpleConfiguration implements Configuration
{
  public SimpleConfiguration(String consumerProps, String consumerTopic,
    String producerProps, String producerTopic)
  {
    this.consumerProps = consumerProps;
    this.consumerTopic = consumerTopic;
    this.producerProps = producerProps;
    this.producerTopic = producerTopic;
  }

  public String consumerTopic()
  {
    return consumerTopic;
  }

  public String producerTopic()
  {
    return producerTopic;
  }

  public Map<String, ?> consumerProps() throws IOException
  {
    return new Props(getClass(), consumerProps).toMap();
  }


  public Map<String, ?> producerProps() throws IOException
  {
    return new Props(getClass(), producerProps).toMap();
  }

  public final String consumerProps;
  public final String consumerTopic;
  public final String producerProps;
  public final String producerTopic;
}
