/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.data.Banks;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.LegacyResponder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Run a synchronous and an asynchronous version of the default API with the
 * default encoding and the transport in memory.
 */
@SuppressWarnings("WeakerAccess") public class LocalDefaultConnectorTest
{
  /**
   * <a href="https://www.iban.de/bic-suchen.html">BICs</a>
   */
  @Before public void setup()
  {
    factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
    responder = new LegacyResponder(factory.decoder(), factory.encoder())
    {
      @Override
      protected String getPrivateBanks(String packet, Decoder.Request r,
        Encoder e)
      {
        Connector.Bank bank = Banks.PRIVATE_BANKS.get(r.userId());

        return e.banks(bank);
      }

      @Override protected String getPublicBanks(String packet, Encoder e)
      {
        return e.banks(Banks.ALL_BANKS);
      }
    };
  }

  @Test public void synchronous() throws InterruptedException
  {
    Connector connector = factory
      .connector(request -> responder.respond(request));

    String userId = "charles.swann@example.org";
    List<Connector.Bank> publicBanks = new ArrayList<>();
    List<Connector.Bank> privateBanks = new ArrayList<>();

    connector.getPublicBanks().forEach(publicBanks::add);
    connector.getPrivateBanks(userId).forEach(privateBanks::add);

    assertThat(publicBanks.size(), is(2));
    assertThat(privateBanks.size(), is(1));
  }

  @Test public void asynchronous() throws InterruptedException
  {
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();
    ExecutorService service = Executors.newSingleThreadExecutor();

    service.submit(new Callable<Void>()
    {
      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
        throws Exception
      {
        for(; ; )
        {
          in.put(responder.respond(out.take()));
        }
      }
    });

    Connector connector = factory.connector(request ->
    {
      out.put(request);

      return in.take();
    });

    String userId = "charles.swann@example.org";
    List<Connector.Bank> publicBanks = new ArrayList<>();
    List<Connector.Bank> privateBanks = new ArrayList<>();

    connector.getPublicBanks().forEach(publicBanks::add);
    connector.getPrivateBanks(userId).forEach(privateBanks::add);

    assertThat(publicBanks.size(), is(2));
    assertThat(privateBanks.size(), is(1));

    service.shutdown();
  }

  Transport.Factory factory;
  LegacyResponder responder;

  static final Logger log = LoggerFactory
    .getLogger(LocalDefaultConnectorTest.class);
}
