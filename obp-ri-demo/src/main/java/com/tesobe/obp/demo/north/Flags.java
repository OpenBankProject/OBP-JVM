/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.util.Options;
import joptsimple.OptionSpec;

/**
 * Command line flags accepted by {@link North}.
 *
 * @since 2016.9
 */
class Flags extends Options
{
  final OptionSpec<String> consumerTopic = acceptsAll("Consumer Topic",
    "consumer-topic").withRequiredArg().describedAs("CONSUMER_TOPIC")
    .defaultsTo("Request");
  final OptionSpec<String> producerTopic = acceptsAll("Producer Topic",
    "producer-topic").withRequiredArg().describedAs("PRODUCER_TOPIC")
    .defaultsTo("Response");

  final OptionSpec<String> consumerProps = acceptsAll("Consumer Configuration",
    "c", "consumer").withRequiredArg().describedAs("CONSUMER")
    .defaultsTo("consumer.props");
  final OptionSpec<String> producerProps = acceptsAll("Producer Configuration",
    "p", "producer").withRequiredArg().describedAs("PRODUCER")
    .defaultsTo("producer.props");
}
