/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

/**
 * Log the traffic at <em>info</em>, empty requests at <em>warn</em>.
 * The logger is {@code com.tesobe.obp.transport.spi.LoggingReceiver}.
 * By configuring this logger to write to a separate file (for example) you can
 * create a log of all traffic between north and south independent of the
 * configuration of the loggers in the classes that are doing the work.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class LoggingReceiver implements Receiver
{
  public LoggingReceiver(Receiver r)
  {
    receiver = r;
  }

  @Override public String respond(Message request)
  {
    if(nonNull(request))
    {
      log.info("{} \u2192 {}", request.id, request.payload);
    }
    else
    {
      log.warn("empty request \u2192 ");
    }

    String response = receiver.respond(request);

    if(nonNull(request))
    {
      log.info("{} \u2190 {}", request.id, response);
    }
    else
    {
      log.warn("empty request \u2190 {}", response);
    }

    return response;
  }
  protected static final Logger log = LoggerFactory
    .getLogger(LoggingReceiver.class);
  private final Receiver receiver;
}
