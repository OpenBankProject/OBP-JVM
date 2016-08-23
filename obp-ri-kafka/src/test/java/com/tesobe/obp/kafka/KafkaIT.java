/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Must start Remote South before running the test.
 * <pre>
 * user -> REST ->
 * connector -> producer -> | -> consumer -> queue -\
 *     queue <- consumer <- | <- producer <- responder
 *                          |
 *      north               |          south
 * </pre>
 */
@SuppressWarnings("WeakerAccess") public class KafkaIT
{
//  /**
//   * {@link RemoteSouth} must be started for this to work.
//   *
//   * @throws InterruptedException in receive when shutdown
//   */
  @SuppressWarnings("InfiniteLoopStatement") @Test public void simple()
    throws InterruptedException
  {
    SimpleNorth north = new SimpleNorth("Request", "Response");

    north.receive(); // listen for responses

    Connector connector = factory.connector(north);

    OutboundContext outboundContext = new OutboundContext(null, null, null);

    Iterable<Bank> banks = connector.getBanks(outboundContext).banks;

    north.shutdown();
  }

  static Transport.Factory factory = Transport.defaultFactory();
  static final Logger log = LoggerFactory.getLogger(KafkaIT.class);
//
//  public static class RemoteSouth
//  {
//    @SuppressWarnings("InfiniteLoopStatement")
//    public static void main(String[] ignored)
//    {
//      factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
//      Receiver responder = new MockResponderV0(factory.decoder(),
//        factory.encoder());
//
//      SimpleSouth south = new SimpleSouth("Request", "Response", responder);
//
//      south.receive();
//
//      while(true)
//      {
//        LockSupport.park();
//      }
//    }
//  }
}
