/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import org.junit.Before;

/**
 * Run a synchronous and an asynchronous version of the default API with the
 * default encoding and the transport in memory.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class LocalDefaultConnectorTest
{
  @Before
  public void setup()
  {
//    Transport.Factory factory = Transport.defaultFactory();
//    Decoder decoder = factory.decoder();
//    Encoder encoder = factory.encoder();
//    Sender sender = request -> new Receiver(){
//
//      @Override public String respond(Message request)
//      {
//        throw new tbd();
//      }
//    }.respond(request);
//    Connector connector = factory.connector(sender);
  }
//  /**
//   * <a href="https://www.iban.de/bic-suchen.html">BICs</a>
//   */
//  @Before public void setup()
//  {
//    factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
//    responder = new DemoResponder(factory.decoder(), factory.encoder());
//  }
//
//  @Test public void synchronous() throws InterruptedException
//  {
//    Connector connector = factory
//      .connector(request -> responder.respond(request));
//
//    List<Connector.Bank> publicBanks = new ArrayList<>();
//    List<Connector.Bank> privateBanks = new ArrayList<>();
//
//    connector.getPublicBanks().forEach(publicBanks::add);
//    connector.getPrivateBanks(Users.charles).forEach(privateBanks::add);
//
//    Assert.assertThat(publicBanks.size(), Is.is(2));
//    Assert.assertThat(privateBanks.size(), Is.is(1));
//  }
//
//  @Test public void asynchronous() throws InterruptedException
//  {
//    final BlockingQueue<String> in = new SynchronousQueue<>();
//    final BlockingQueue<Message> out = new SynchronousQueue<>();
//    ExecutorService service = Executors.newSingleThreadExecutor();
//
//    service.submit(new Callable<Void>()
//    {
//      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
//        throws Exception
//      {
//        for(; ; )
//        {
//          in.put(responder.respond(out.take()));
//        }
//      }
//    });
//
//    Connector connector = factory.connector(request ->
//    {
//      out.put(request);
//
//      return in.take();
//    });
//
//    String userId = "charles.swann@example.org";
//    List<Connector.Bank> publicBanks = new ArrayList<>();
//    List<Connector.Bank> privateBanks = new ArrayList<>();
//
//    connector.getPublicBanks().forEach(publicBanks::add);
//    connector.getPrivateBanks(userId).forEach(privateBanks::add);
//
//    Assert.assertThat(publicBanks.size(), Is.is(2));
//    Assert.assertThat(privateBanks.size(), Is.is(1));
//
//    service.shutdown();
//  }
//
//  Transport.Factory factory;
//  LegacyResponder responder;
//
//  static final Logger log = LoggerFactory
//    .getLogger(LocalDefaultConnectorTest.class);
}
