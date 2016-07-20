/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Check for changes in public signatures.
 */
public class ConnectorSignatureTest
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport.defaultFactory()
      .orElseThrow(RuntimeException::new);

    connector = factory.connector(request -> receiver.respond(request));
    receiver = new Receiver(factory.decoder(), factory.encoder());
  }

  @Test public void getPublicBanks() throws Exception
  {
    ArrayList<Connector.Bank> banks = new ArrayList<>();

    connector.getPublicBanks().forEach(banks::add);

    assertThat(banks.size(), is(1));
  }

  @Test public void getPrivateBanks() throws Exception
  {
    ArrayList<Connector.Bank> banks = new ArrayList<>();

    connector.getPrivateBanks("charles.swann@example.org").forEach(banks::add);

    assertThat(banks.size(), is(1));
  }

  private Connector connector;
  private Receiver receiver;

  private class Receiver extends LegacyResponder
  {
    Receiver(Decoder d, Encoder e)
    {
      super(d, e);
    }

    @Override
    protected String getPrivateBanks(String packet, Decoder.Request request,
      Encoder e)
    {
      assertThat(request.userId(), notNullValue());

      return e.banks(new Connector.Bank("bank", request.userId()));
    }

    @Override protected String getPublicBanks(String packet, Encoder e)
    {
      return e.banks(new Connector.Bank("bank", "public"));
    }
  }
}
