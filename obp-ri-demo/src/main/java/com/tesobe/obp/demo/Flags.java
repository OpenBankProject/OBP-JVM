/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.south.South;
import com.tesobe.obp.util.Options;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;

/**
 * Command line flags accepted by {@link South}.
 *
 * @since 2016.9
 */
class Flags extends Options
{
  final OptionSpecBuilder kafka = acceptsAll("Use Apache Kafka", "kafka");
  final OptionSpecBuilder north = acceptsAll("This is the north side", "north");
  final OptionSpecBuilder south = acceptsAll("This is the south side", "south");
  final OptionSpec<String> responder = acceptsAll("The demo data",
    "responder").availableIf(kafka)
    .withRequiredArg()
    .describedAs("RESPONDER")
    .defaultsTo("com.tesobe.obp.demo.OneBankTwoAccounts");
  final OptionSpec<String> producerTopic = acceptsAll("Producer topic",
    "producer-topic").availableIf(kafka)
    .withRequiredArg()
    .describedAs("PRODUCER_TOPIC");
  final OptionSpec<String> consumerTopic = acceptsAll("Consumer topic",
    "consumer-topic").availableIf(kafka)
    .withRequiredArg()
    .describedAs("CONSUMER_TOPIC");

  final OptionSpec<String> producerProps = acceptsAll("Producer configuration",
    "producer-props").availableIf(kafka)
    .withRequiredArg()
    .describedAs("PRODUCER_PROPS")
    .defaultsTo("producer.props");
  final OptionSpec<String> consumerProps = acceptsAll("Consumer configuration",
    "consumer-props").availableIf(kafka)
    .withRequiredArg()
    .describedAs("CONSUMER_PROPS")
    .defaultsTo("consumer.props");
}
