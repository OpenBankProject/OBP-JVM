/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.MockResponder;
import com.tesobe.obp.transport.spi.Receiver;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
  /**
   * {@link RemoteSouth} must be started for this to work.
   *
   * @throws InterruptedException in receive when shutdown
   */
  @SuppressWarnings("InfiniteLoopStatement") @Test public void simple()
    throws InterruptedException
  {
    String userId = MockResponder.charles;
    SimpleNorth north = new SimpleNorth("Request", "Response");

    north.receive(); // listen for responses

    Connector connector = factory.connector(north);

    List<Connector.Bank> privateBanks = new ArrayList<>();
    List<Connector.Bank> publicBanks = new ArrayList<>();

    connector.getPrivateBanks(userId).forEach(privateBanks::add);
    connector.getPublicBanks().forEach(publicBanks::add);

    assertThat(privateBanks.size(),
      is(MockResponder.PRIVATE_BANKS.get(MockResponder.charles).size()));
    assertThat(publicBanks.size(), is(MockResponder.PUBLIC_BANKS.size()));

    north.shutdown();
  }

  static Transport.Factory factory = Transport.defaultFactory()
    .orElseThrow(RuntimeException::new);
  static final Logger log = LoggerFactory.getLogger(KafkaIT.class);

  public static class RemoteSouth
  {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] ignored)
    {
      factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
      Receiver responder = new MockResponder(factory.decoder(),
        factory.encoder());

      SimpleSouth south = new SimpleSouth("Request", "Response", responder);

      south.receive();

      while(true)
      {
        LockSupport.park();
      }
    }
  }
}
