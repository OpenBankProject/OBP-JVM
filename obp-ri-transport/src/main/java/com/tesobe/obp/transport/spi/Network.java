/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 */
@SuppressWarnings("WeakerAccess") public class Network
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
    return new Session();
  }

  protected <T extends Id> Decoder.Response<T> get(Session s, String caller,
    Target t, Class<T> type, String userId, String bankId, String accountId,
    String transactionId) throws InterruptedException
  {
    assert s != null;
    assert t != null;
    assert type != null;

    Encoder.Request request = encoder.get(caller, t, userId, bankId, accountId,
      transactionId);
    String id = UUID.randomUUID().toString();
    Message message = new Message(id, request.toString());

    log.trace("{}", request);

    String response = sender.send(message);
    Decoder.Response<T> result = decoder.get(type, response);

    log.trace("\u2192 {}", response);

    return result;
  }


  protected <T extends Token> T put(Session s, String caller, Target t,
    Class<T> type, Map<String, String> fields, Map<String, BigDecimal> money)
  {
    assert s != null;
    assert t != null;
    assert type != null;

    Encoder.Request request = encoder.put(caller, t, fields, money);

    String id = UUID.randomUUID().toString();
    Message message = new Message(id, request.toString());

    log.trace("{}", request);

    try
    {
      String response = sender.send(message);
      Optional<Token> token = decoder.token(response);

      log.trace("\u2192 {}", response);

      return token
        .map(type::cast)
        .orElseGet(() -> type.cast(new ErrorToken("Cannot decode response!")));
    }
    catch(InterruptedException e)
    {
      log.error("\u2192 {}", id, e);

      return type.cast(new ErrorToken(e.getMessage()));
    }
  }

  public static Target target(String target)
  {
    return targets.get(target);
  }

  protected static final Map<String, Target> targets = new HashMap<>();
  protected static final Logger log = LoggerFactory.getLogger(Network.class);
  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  public enum Target
  {
    account, accounts, bank, banks, transaction, transactions, user, users
  }

  /**
   * Holds the network internal information - and the user data.
   */
  public class Session
  {
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
    public <T extends Id> Decoder.Response<T> get(String caller, Target t,
      Class<T> type, String userId, String bankId, String accountId,
      String transactionId) throws InterruptedException
    {
      if(t == null)
      {
        throw new IllegalArgumentException("Target must not be null!");
      }

      if(type == null)
      {
        throw new IllegalArgumentException("Type must not be null!");
      }

      return Network.this.get(this, caller, t, type, userId, bankId, accountId,
        transactionId);
    }

    /**
     * @param <T> the return type
     * @param caller caller's name: information only: null allowed
     * @param t the action requested: null not allowed
     * @param type the return type
     * @param fields string fields
     * @param money big decimal fields
     *
     * @return a token
     *
     * @throws IllegalArgumentException target or type is null
     */
    public <T extends Token> T put(String caller, Target t, Class<T> type,
      Map<String, String> fields, Map<String, BigDecimal> money)
    {
      if(t == null)
      {
        throw new IllegalArgumentException("Target must not be null!");
      }

      if(type == null)
      {
        throw new IllegalArgumentException("Type must not be null!");
      }

      return Network.this.put(this, caller, t, type, fields, money);
    }
  }

  @SuppressWarnings("WeakerAccess") public class DefaultPager
    implements Pager, Serializable
  {
    public DefaultPager()
    {
      this(0, 50, "completed", SortOrder.descending, null, null);
    }

    public DefaultPager(int offset, int size, String sortField,
      Pager.SortOrder so, ZonedDateTime earliest, ZonedDateTime latest)
    {
      this.offset = offset;
      this.size = size;
      this.field = sortField;
      this.sortOrder = so;
      this.earliest = earliest;
      this.latest = latest;
    }

//    public List<Transaction> getTransactions(String bankId, String accountId)
//      throws InterruptedException
//    {
//      String id = UUID.randomUUID().toString();
//      String request = encoder
//        .getTransactions(this, bankId, accountId)
//        .toString();
//      String response = sender.send(new Message(id, request));
//      ArrayList<Transaction> page = new ArrayList<>();
//      Decoder.ResponseOld r = decoder.transactions(response);
//
//      more = r.more();
//
//      log.trace("{} \u2192 {}", request, response);
//
//      return r.transactions();
//    }

//    public List<Transaction> getTransactions(String bankId, String accountId,
//      String userId) throws InterruptedException
//    {
//      String id = UUID.randomUUID().toString();
//      String request = encoder
//        .getTransactions(this, bankId, accountId, userId)
//        .toString();
//      String response = sender.send(new Message(id, request));
//      Decoder.ResponseOld r = decoder.transactions(response);
//
//      more = r.more();
//
//      log.trace("{} \u2192 {}", request, response);
//
//      return r.transactions();
//    }

    @Override public boolean hasMorePages()
    {
      return more;
    }

    @Override public Pager nextPage()
    {
      return new DefaultPager(size, size, field, sortOrder, earliest, latest);
    }

    static final long serialVersionUID = 42L;
    public final int offset;
    public final int size;
    public final String field;
    public final Pager.SortOrder sortOrder;
    public final ZonedDateTime earliest;
    public final ZonedDateTime latest;
    protected boolean more;
  }

  static
  {
    for(Target t : Target.values())
    {
      targets.put(t.name(), t);
    }
  }
}
