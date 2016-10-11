/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo;

import com.tesobe.obp.demo.north.Rest;
import com.tesobe.obp.demo.south.DemoDatabase;
import com.tesobe.obp.demo.south.DemoReceiver;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.spi.LoggingReceiver;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.util.Options;
import joptsimple.OptionSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;


/**
 * Alternative to the kafka north/south demo that has both sides in a single
 * process.
 */
public class Standalone
{
  public static void main(String[] commandLine) throws IOException
  {
    if(flags.parse(commandLine))
    {
      log.info("Starting TESOBE's Standalone OBP Demo REST Server...");

      String ipAddress = flags.valueOf(flags.ipAddress);
      int port = flags.valueOf(flags.port);
      Transport.Factory factory = Transport.defaultFactory();
      final BlockingQueue<String> in = new SynchronousQueue<>();
      final BlockingQueue<Message> out = new SynchronousQueue<>();
      ExecutorService service = Executors.newCachedThreadPool();
      Sender sender = request ->
      {
        out.put(request);

        return in.take();
      };
      Rest north = new Rest(factory.connector(sender), ipAddress, port);
      Receiver south = new LoggingReceiver(
        new DemoReceiver(factory.decoder(), factory.encoder(),
          DemoDatabase.simple()));

      service.submit(new Callable<Void>()
      {
        @Override @SuppressWarnings({"InfiniteLoopStatement"})
        public Void call() throws InterruptedException
        {
          for(; ; )
          {
            in.put(south.respond(out.take()));
          }
        }
      });
    }
  }

  private final static Flags flags = new Flags();
  private final static Logger log = LoggerFactory.getLogger(Standalone.class);

  static class Flags extends Options
  {
    final OptionSpec<String> ipAddress = acceptsAll(
      "REST server listen interface", "interface").withRequiredArg()
      .describedAs("IP_ADDRESS").defaultsTo("0.0.0.0");
    final OptionSpec<Integer> port = acceptsAll("REST server listen port",
      "port").withRequiredArg().describedAs("PORT").ofType(Integer.class)
      .defaultsTo(4567);
  }
}
