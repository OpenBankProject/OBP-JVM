/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") class Network
{
  /**
   * Trust all parameters not to be null.
   *
   * @param v version
   * @param e encoder
   * @param d decoder
   * @param s sender
   */
  public Network(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    assert d != null;
    assert e != null;
    assert s != null;
    assert v != null;

    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  /**
   * todo what is a default session?
   *
   * @return a default session
   */
  public Session session()
  {
    return session(new NullPager());
  }

  public Session session(Pager p)
  {
    return new Session(p);
  }

  protected <T extends Id> Decoder.Response<T> get(Session s, String caller,
    Transport.Target t, Class<T> type, String userId, String bankId,
    String accountId, String transactionId) throws InterruptedException
  {
    assert s != null;
    assert t != null;
    assert type != null;

    Encoder.Request request = encoder.get(caller, t, s.pager, s.pager.state(),
      userId, bankId, accountId, transactionId);
    String id = UUID.randomUUID().toString();
    Message message = new Message(id, request.toString());

    String response = sender.send(message);
    Decoder.Response<T> result = decoder.get(type, response);

    log.trace("{}, {}", request, response);

    return result;
  }

  protected <T extends Token> T put(Session s, String caller,
    Transport.Target t, Class<T> type, Map<String, String> fields,
    Map<String, BigDecimal> money, Map<String, Temporal> timestamps)
  {
    assert s != null;
    assert t != null;
    assert type != null;

    Encoder.Request request = encoder.put(caller, t, fields, money, timestamps);

    String id = UUID.randomUUID().toString();
    Message message = new Message(id, request.toString());

    log.trace("{}", request);

    try // todo why try here and no try in get, above?
    {
      String response = sender.send(message);
      Optional<Token> token = decoder.token(response);

      log.trace("{}", response);

      return token.map(type::cast)
        .orElseGet(() -> type.cast(new ErrorToken("Cannot decode response!")));
    }
    catch(InterruptedException e)
    {
      log.error("{} {}", id, e);

      return type.cast(new ErrorToken(e.getMessage()));
    }
  }

  protected static final Logger log = LoggerFactory.getLogger(Network.class);
  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  /**
   * Holds the network internal information - and the user data.
   */
  public class Session
  {
    public Session()
    {
      this(new NullPager());
    }

    public Session(Pager p)
    {
      pager = p instanceof DefaultPager
              ? DefaultPager.class.cast(p)
              : new DefaultPager(p);
    }

    /**
     * Modifies the session to hold the network related information and returns
     * the user data.
     *
     * @param caller caller's name: information only: null allowed
     * @param t the kind of data requested: null not allowed
     * @param type type of the returned data: null not allowed
     * @param userId request filter: null allowed
     * @param bankId request filter: null allowed
     * @param accountId request filter: null allowed
     * @param transactionId request filter: null allowed
     * @param <T> user data type
     *
     * @return A wrapper of result data or error
     *
     * @throws InterruptedException     network trouble
     * @throws IllegalArgumentException target or type is null
     */
    public <T extends Id> Decoder.Response<T> get(String caller,
      Transport.Target t, Class<T> type, String userId, String bankId,
      String accountId, String transactionId) throws InterruptedException
    {
      if(t == null)
      {
        throw new IllegalArgumentException("Target must not be null!");
      }

      if(type == null)
      {
        throw new IllegalArgumentException("Type must not be null!");
      }

      Decoder.Response<T> response = Network.this.get(this, caller, t, type,
        userId, bankId, accountId, transactionId);

      pager.more(response.state(), response.count(), response.hasMorePages());

      return response;
    }

    /**
     * @param <T> the return type
     * @param caller caller's name: information only: null allowed
     * @param t the action requested: null not allowed
     * @param type the return type
     * @param fields string fields
     * @param money big decimal fields
     * @param timestamps temporal fields
     *
     * @return a token
     *
     * @throws IllegalArgumentException target or type is null
     */
    public <T extends Token> T put(String caller, Transport.Target t,
      Class<T> type, Map<String, String> fields, Map<String, BigDecimal> money,
      Map<String, Temporal> timestamps)
    {
      if(t == null)
      {
        throw new IllegalArgumentException("Target must not be null!");
      }

      if(type == null)
      {
        throw new IllegalArgumentException("Type must not be null!");
      }

      return Network.this.put(this, caller, t, type, fields, money, timestamps);
    }

    DefaultPager pager;
  }

  static
  {
    for(Transport.Target t : Transport.Target.values())
    {
      Transport.targets.put(t.name(), t);
    }
  }
}
