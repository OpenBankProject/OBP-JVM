/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Data;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Compatible to end 2016 OBP-API.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class ConnectorNov2016
  implements Connector
{
  public ConnectorNov2016(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    if(e == null)
    {
      throw new IllegalArgumentException("Encoder required!");
    }

    if(d == null)
    {
      throw new IllegalArgumentException("Decoder required!");
    }

    if(s == null)
    {
      throw new IllegalArgumentException("Sender required!");
    }

    if(v == null)
    {
      throw new IllegalArgumentException("Version required!");
    }

    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  @Override public String describe() throws Exception
  {
    String id = UUID.randomUUID().toString();
    Encoder.Request request = encoder.describe();
    Message message = new Message(id, request.toString());
    String response = sender.send(message);

    log.trace("{} \u2192 {}", message, response);

    return decoder.describe(response);
  }

  @Override public Decoder.Response get(String caller, Transport.Target target,
    Pager pager, Map<String, ?> parameters) throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    Encoder.Request request = encoder.get(caller, target, pager, parameters);
    Message message = new Message(id, request.toString());
    String response = sender.send(message);
    Decoder.Response decoded = decoder.get(target, response);

    log.trace("{} \u2192 {}", message, response);

    if(pager.count() == decoded.count())
    {
      pager.more(decoded.state(), decoded.count(), decoded.hasMorePages());

      return decoded;
    }
    else // error
    {
      return new Decoder.Response()
      {
        @Override public List<Data> data()
        {
          return decoded.data();
        }

        @Override public boolean hasMorePages()
        {
          return decoded.hasMorePages();
        }

        @Override public String state()
        {
          return decoded.state();
        }

        @Override public int count()
        {
          return decoded.count();
        }

        @Override public Optional<Decoder.Error> error()
        {
          return Optional.of(() -> "Message counts do not match!");
        }
      };
    }
  }

  /**
   * North: send data to the south.
   *
   * @param caller label for the message, not required
   * @param target select data sink, required
   * @param parameters request parameters
   * @param fields data to send
   *
   * @return a response
   *
   * @throws Exception network trouble
   */
  @Override public Decoder.Response put(String caller, Transport.Target target,
    Map<String, ?> parameters, Map<String, ?> fields) throws Exception
  {
    String id = UUID.randomUUID().toString();
    Encoder.Request request = encoder.put(caller, target, parameters, fields);
    Message message = new Message(id, request.toString());
    String response = sender.send(message);

    log.trace("{} \u2192 {}", message, response);

    return decoder.put(target, response);
  }

  @Override public Decoder.Response fetch() throws Exception
  {
    String id = UUID.randomUUID().toString();
    Encoder.Request request = encoder.fetch();
    Message message = new Message(id, request.toString());
    String response = sender.send(message);

    log.trace("{} \u2192 {}", message, response);

    return decoder.fetch(response);
  }

  @Override public Pager pager()
  {
    return new NullPager();
  }

  @Override
  public Pager pager(int pageSize, int offset, Pager.Filter f, Pager.Sorter s)
  {
    return new DefaultPager(pageSize, offset, f, s);
  }

  protected static final Logger log = LoggerFactory.getLogger(
    ConnectorNov2016.class);
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Transport.Version version;
  protected final Sender sender;
}
