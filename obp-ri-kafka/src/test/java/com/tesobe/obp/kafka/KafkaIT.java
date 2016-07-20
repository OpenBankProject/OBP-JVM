/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.kafka;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.LegacyResponder;
import com.tesobe.obp.transport.spi.Receiver;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * user -> REST ->
 * api -> producer -> | -> consumer -> in -\
 * out <- consumer <- | <- producer <- responder
 * |
 * server      |    client
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
    String userId = "charles.swann@example.org";
    SimpleNorth north = new SimpleNorth("Request", "Response");

    north.receive(); // listen for responses

    Connector connector = factory.connector(north);

    List<Connector.Bank> privateBanks = new ArrayList<>();
    List<Connector.Bank> publicBanks = new ArrayList<>();

    connector.getPrivateBanks(userId).forEach(privateBanks::add);
    connector.getPublicBanks().forEach(publicBanks::add);

    assertThat(privateBanks.size(), is(0));
    assertThat(publicBanks.size(), is(1));

    north.shutdown();
  }

  static Transport.Factory factory = Transport.defaultFactory()
    .orElseThrow(RuntimeException::new);
  static final Logger log = LoggerFactory.getLogger(KafkaIT.class);

  public static class RemoteSouth
  {
    public static void main(String[] ignored)
    {
      Connector.Bank publicBank = new Connector.Bank("CRESGIGI",
        "CREDIT SUISSE (GIBRALTAR) LTD");

      factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
      Receiver responder = new LegacyResponder(factory.decoder(),
        factory.encoder())
      {
        @Override
        protected String getPrivateBanks(String packet, Decoder.Request r,
          Encoder e)
        {
          log.trace(packet);

          return e.banks(Collections.emptyList());
        }

        @Override protected String getPublicBanks(String packet, Encoder e)
        {
          log.trace(packet);

          return e.banks(publicBank);
        }
      };

      SimpleSouth south = new SimpleSouth("Request", "Response", responder);

      south.receive();

      while(true)
      {
        LockSupport.park();
      }
    }
  }
}
