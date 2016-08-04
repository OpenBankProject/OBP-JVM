/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import org.junit.Ignore;

/**
 * Asynchronous, in memory speed test:
 * <pre>
 * 21:59:44.415 1.000.000 asynchronous in memory api calls: min 0 sec 44.822
 * 22:00:25.635 1.000.000 asynchronous in memory api calls: min 0 sec 41.207
 * 22:01:05.183 1.000.000 asynchronous in memory api calls: min 0 sec 39.548
 * 22:01:44.564 1.000.000 asynchronous in memory api calls: min 0 sec 39.380
 * 22:02:21.362 1.000.000 asynchronous in memory api calls: min 0 sec 36.794
 * 22:03:01.297 1.000.000 asynchronous in memory api calls: min 0 sec 39.935
 * 22:03:43.848 1.000.000 asynchronous in memory api calls: min 0 sec 42.549
 * 22:04:28.835 1.000.000 asynchronous in memory api calls: min 0 sec 44.984
 * 22:05:07.366 1.000.000 asynchronous in memory api calls: min 0 sec 38.529
 * 22:05:43.343 1.000.000 asynchronous in memory api calls: min 0 sec 35.975
 * </pre>
 */
@Ignore("Takes a few minutes on old hardware")
@SuppressWarnings({"WeakerAccess", "Convert2MethodRef"}) public class SpeedTest
{
//  /**
//   * <a href="https://www.iban.de/bic-suchen.html">BICs</a>
//   */
//  @Before public void setup()
//  {
//    factory = Transport.defaultFactory().orElseThrow(RuntimeException::new);
//    responder = new MockResponder(factory.decoder(), factory.encoder());
//  }
//
//  /**
//   * Turn of logging. Will throw if logging framework is not logback.
//   * You should then add code to switch off your loggers...
//   */
//  @Before public void logging()
//  {
//    if(!(LoggerFactory.getILoggerFactory() instanceof LoggerContext))
//    {
//      throw new RuntimeException("Please add code to switch off logging!");
//    }
//
//    LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
//    ConsoleAppender<ILoggingEvent> console = new ConsoleAppender<>();
//    PatternLayoutEncoder pattern = new PatternLayoutEncoder();
//
//    pattern.setContext(context);
//    pattern.setPattern("%d{HH:mm:ss.SSS} %msg%n");
//    pattern.start();
//
//    console.setContext(context);
//    console.setEncoder(pattern);
//    console.start();
//
//    context.getLoggerList().forEach(l -> l.detachAndStopAllAppenders());
//
//    context.getLogger(getClass()).addAppender(console);
//  }
//
//  @Test public void million() throws InterruptedException
//  {
//    final BlockingQueue<String> in = new SynchronousQueue<>();
//    final BlockingQueue<Message> out = new SynchronousQueue<>();
//    ExecutorService service = Executors.newSingleThreadExecutor();
//
//    // receiver
//    service.submit(new Callable<Void>()
//    {
//      @Override @SuppressWarnings({"InfiniteLoopStatement"}) public Void call()
//        throws InterruptedException
//      {
//        for(; ; )
//        {
//          in.put(responder.respond(out.take()));
//        }
//      }
//    });
//
//    // sender
//    Connector connector = factory.connector(request ->
//    {
//      out.put(request);
//
//      return in.take();
//    });
//
//    // driver
//    for(int i = 0; i < 10; ++i)
//    {
//      long start = System.nanoTime();
//
//      for(List<Connector.Bank> banks = new ArrayList<>();
//          banks.size() < 1_000_000; )
//      {
//        connector.getPrivateBanks(charles).forEach(banks::add);
//      }
//
//      long duration = System.nanoTime() - start;
//      long minutes = MINUTES.convert(duration, NANOSECONDS);
//      long seconds = SECONDS.convert(duration, NANOSECONDS) - SECONDS
//        .convert(minutes, MINUTES);
//      long millis = MILLISECONDS.convert(duration, NANOSECONDS) - MILLISECONDS
//        .convert(seconds, SECONDS);
//
//      log.info("1.000.000 asynchronous in memory api calls: min {} sec {}.{}",
//        minutes, seconds, millis);
//    }
//
//    service.shutdown();
//  }
//
//  Transport.Factory factory;
//  private Connector connector;
//  private MockResponder responder;
//
//  static final Logger log = LoggerFactory.getLogger(SpeedTest.class);
}
