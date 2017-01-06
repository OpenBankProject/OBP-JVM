/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.North;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.transport.spi.ReceiverNov2016;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * To make sure the demo will run after changes this test is the same as the
 * demo but does not use kafka.
 */
public class DemoTest
{
  @Before public void setup() throws IOException
  {
    Transport.Factory factory = Transport.defaultFactory();
    Responder responder = new OneBankTwoAccounts();
    Receiver receiver = new ReceiverNov2016(responder, factory.codecs());
    final BlockingQueue<String> in = new SynchronousQueue<>();
    final BlockingQueue<Message> out = new SynchronousQueue<>();
    final Sender sender = request ->
    {
      out.put(request);

      return in.take();
    };

    north = new North(factory.connector(sender));

    // south: receiver in a background thread
    service.submit(new Callable<Void>()
    {
      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
        throws InterruptedException
      {
        for(; ; )
        {
          in.put(receiver.respond(out.take()));
        }
      }
    });
  }

  @After public void shutdown()
  {
    north.shutdown();
    service.shutdown();
  }

  @Test public void demo() throws Exception
  {
    new Demo().run(north);
  }

  North north;
  ExecutorService service = Executors.newCachedThreadPool();
}
