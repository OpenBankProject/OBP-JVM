/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.MockResponder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.tesobe.obp.transport.spi.MockResponder.CRESGIGI;
import static com.tesobe.obp.transport.spi.MockResponder.charles;
import static com.tesobe.obp.transport.spi.MockResponder.charles_CRESGIGI_1;
import static com.tesobe.obp.transport.spi.MockResponder.hacker;
import static java.util.Objects.deepEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Check for changes in public signatures.
 *
 * @since 2016.0
 */
public class ConnectorSignatureTest
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport.defaultFactory()
      .orElseThrow(RuntimeException::new);

    connector = factory.connector(request -> responder.respond(request));
    responder = new MockResponder(factory.decoder(), factory.encoder());
  }

  @Test public void getPrivateAccount() throws Exception
  {
//    ArrayList<Connector.Account> accounts = new ArrayList<>();

    Connector.Account account = connector
      .getPrivateAccount(charles, CRESGIGI.id, "CRESGIGI-1")
      .orElseThrow(RuntimeException::new);

    assertTrue(deepEquals(account, charles_CRESGIGI_1));
  }

  @Test public void getPublicBanks() throws Exception
  {
    ArrayList<Connector.Bank> banks = new ArrayList<>();

    connector.getPublicBanks().forEach(banks::add);

    assertThat(banks.size(), is(MockResponder.PUBLIC_BANKS.size()));
  }

  @Test public void getPrivateBanks() throws Exception
  {
    ArrayList<Connector.Bank> banks = new ArrayList<>();

    connector.getPrivateBanks(charles).forEach(banks::add); // 1
    connector.getPrivateBanks(hacker).forEach(banks::add); // 0
    connector.getPrivateBanks(null).forEach(banks::add); // 0

    assertThat(banks.size(),
      is(MockResponder.PRIVATE_BANKS.get(charles).size()));
  }

  private Connector connector;
  private MockResponder responder;
}
