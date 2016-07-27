// Copyright
package com.tesobe.obp.demo.south;

import com.tesobe.obp.util.Options;
import joptsimple.OptionSpec;

import java.io.File;

/**
 * Command line flags accepted by {@link South}.
 * @since 2016.0
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
