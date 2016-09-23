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

  //  static // todo enable if desperate
//  {
//    try
//    {
//      Class<?> logger = Class.forName("ch.qos.logback.classic.Logger");
//      Class<?> level = Class.forName("ch.qos.logback.classic.Level");
//
//      if(logger != null && level != null)
//      {
//        Object log = logger
//          .cast(LoggerFactory.getLogger("com.tesobe.obp.socgen"));
//        Method setLevel = logger.getMethod("setLevel", level);
//        Field trace = level.getField("TRACE");
//
//        setLevel.invoke(log, trace);
//      }
//    }
//    catch(ClassNotFoundException | NoSuchMethodException |
//      IllegalAccessException | InvocationTargetException |
//      NoSuchFieldException x)
//    {
//      x.printStackTrace();
//    }
//  }
}
