/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Responder;
import com.tesobe.obp.transport.Response;
import com.tesobe.obp.transport.Transport;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

/**
 * todo document
 */
@SuppressWarnings("WeakerAccess") public class ReceiverNov2016
  extends AbstractReceiver
{
  public ReceiverNov2016(Responder r, Codecs cs)
  {
    super(cs);

    responder = r;
  }

  /**
   * @param r request, already decoded
   * @param e response encoder
   *
   * @return response
   */
  @Override protected String get(Decoder.Request r, Encoder e)
  {
    assert r != null;
    assert e != null;

    try
    {
      return r.target()
        .map(t -> get(r, e, t))
        .orElseGet(() -> errorEncoder.error("Target missing!"));
    }
    catch(Throwable x) // !
    {
      log.error("{} {}", r.raw(), x.getMessage());
      log.trace(r.raw(), x);

      return errorEncoder.error(x.getMessage());
    }
  }

  protected String get(Decoder.Request r, Encoder e, Transport.Target t)
  {
    assert r != null;
    assert e != null;
    assert t != null;

    return get(r.pager(), r.parameters(), e, t);
  }

  /**
   * Help with the paging.
   *
   * @param p pager
   * @param ps parameters
   * @param e encoder
   * @param t target
   *
   * @return encoded response
   */
  protected String get(Decoder.Pager p, Decoder.Parameters ps, Encoder e,
    Transport.Target t)
  {
    assert p != null;
    assert ps != null;
    assert e != null;
    assert t != null;

    int count = p.count();
    String state = p.state().map(s -> s).orElse(null);
    Response response;

    if(state == null || count == 0)
    {
      state = ps.requestId(); // need a new state
      count = 0; // restart the count
      response = first(p, ps, t, state);
    }
    else
    {
      response = next(p, state);
    }

    if(response != null)
    {
      List<? extends Map<String, ?>> data = response.data();

      if(data != null)
      {
        int offset = p.offset();

        if(offset < data.size())
        {
          int pageSize = Math.min(p.size(), data.size() - offset);

          return e.response(response, offset, pageSize, state, count,
            offset + pageSize < data.size(), t);
        }
      }
    }

    return e.empty(state, count, t);
  }

  protected Response next(Decoder.Pager p, String state)
  {
    return responder.next(state, p);
  }

  protected Response first(Decoder.Pager p, Decoder.Parameters ps,
    Transport.Target t, String state)
  {
    return responder.first(state, p, ps, t);
  }

  @Override protected String put(Decoder.Request r, Encoder e)
  {
    assert r != null;
    assert e != null;

    try
    {
      return r.target()
        .map(t -> put(r, e, t))
        .orElseGet(() -> errorEncoder.error("Target missing!"));
    }
    catch(Throwable x) // !
    {
      log.error("{} {}", r.raw(), x.getMessage());
      log.trace(r.raw(), x);

      return errorEncoder.error(x.getMessage());
    }
  }

  @Override protected String describe(Decoder.Request r, Encoder e)
  {
    JSONObject response = responder.describe(new JSONObject());

    return e.description(response);
  }

  @Override protected String fetch(Decoder.Request r, Encoder e)
  {
    return e.response(responder.fetch(), null);
  }

  protected String put(Decoder.Request r, Encoder e, Transport.Target t)
  {
    assert r != null;
    assert e != null;
    assert t != null;

    switch(t)
    {
      case transaction:
      {
        return e.response(put(r, t), t);
      }
      default:
        return e.error(format(Locale.US, "Cannot put %s!", t));
    }
  }

  protected Response put(Decoder.Request r, Transport.Target t)
  {
    return responder.put(r.parameters(), r.fields(), t);
  }
  static final Logger log = LoggerFactory.getLogger(ReceiverNov2016.class);
  final Responder responder;
}
