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
    .defaultsTo("Response");
  final OptionSpec<String> producerTopic = acceptsAll("Producer Topic",
    "producer-topic").withRequiredArg().describedAs("PRODUCER_TOPIC")
    .defaultsTo("Request");

  final OptionSpec<String> consumerProps = acceptsAll("Consumer Configuration",
    "consumer").withRequiredArg().describedAs("CONSUMER")
    .defaultsTo("consumer.props");
  final OptionSpec<String> producerProps = acceptsAll("Producer Configuration",
    "producer").withRequiredArg().describedAs("PRODUCER")
    .defaultsTo("producer.props");

  final OptionSpec<String> ipAddress = acceptsAll(
    "REST server listen interface", "interface").withRequiredArg()
    .describedAs("IP_ADDRESS").defaultsTo("0.0.0.0");
  final OptionSpec<Integer> port = acceptsAll("REST server listen port", "port")
    .withRequiredArg().describedAs("PORT").ofType(Integer.class)
    .defaultsTo(4567);
}
