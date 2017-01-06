/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import java.io.IOException;
import java.util.Map;

/**
 * Collect the kafka configuration options into one place
 */
public interface Configuration
{
  String consumerTopic();

  String producerTopic();

  Map<String, ?> consumerProps() throws IOException;

  Map<String, ?> producerProps() throws IOException;
}
