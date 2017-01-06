/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.North;
import com.tesobe.obp.kafka.Configuration;
import com.tesobe.obp.kafka.SimpleConfiguration;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Sender;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * todo document
 */
public class ConfigurationTest
{
  @Test public void north() throws IOException
  {
    Connector connector = mock(Connector.class);
    Sender sender = mock(Sender.class);

    when(connector.sender()).thenReturn(sender);

    North north = new North(connector);
    Configuration cfg = new SimpleConfiguration(north, "consumer.props", "ct",
      "producer.props", "pt");

    assertThat(cfg.consumerTopic(), is("ct"));
    assertThat(cfg.producerTopic(), is("pt"));
    assertThat(cfg.consumerProps(), notNullValue());
    assertThat(cfg.producerProps(), notNullValue());
  }
}
