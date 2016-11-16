/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Internal JSON decoder. Only called by trusted code.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class DecoderSep2016 implements Decoder
{
  public DecoderSep2016(Transport.Version v)
  {
    version = v;
  }

  @Override public Transport.Version version()
  {
    return version;
  }

  @Override public Optional<Request> request(String request)
  {
    return Optional.of(new DefaultRequest(request));
  }

  @Override public Optional<Account> account(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject account = new JSONObject(response);

        return Optional.of(new AccountDecoder(account));
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
  }

  @Override public Optional<Token> token(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject token = new JSONObject(response);

        return Optional.of(new TokenDecoder(token));
      }
      catch(JSONException e)
      {
        log.error("{}", response); // todo error return
      }
    }

    return Optional.empty();
  }


  @Override public Iterable<Account> accounts(String response)
  {
    log.trace("{} {}", version, response);

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }

    return () -> new Iterator<Account>() // Iterable as lambda
    {
      @Override public boolean hasNext()
      {
        return iterator.hasNext();
      }

      @Override public Account next()
      {
        Object next = iterator.next();

        if(!(next instanceof JSONObject))
        {
          log.error("{}", next);

          return null;
        }

        return new AccountDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array.iterator();
    };
  }

  @Override public Optional<Bank> bankOld(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject bank = new JSONObject(response);

        return Optional.of(new BankDecoder(bank));
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
  }

  @Override public Iterable<Bank> banks(String response)
  {
    log.trace("{} {}", version, response);

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }

    return () -> new Iterator<Bank>() // Iterable as lambda
    {
      @Override public boolean hasNext()
      {
        return iterator.hasNext();
      }

      @Override public Bank next()
      {
        Object next = iterator.next();

        if(!(next instanceof JSONObject))
        {
          log.error("{}", next);

          return null;
        }

        return new BankDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array.iterator();
    };
  }

  @Override public Optional<String> transactionId(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        return Optional.of(response);
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
  }

  @Override public Optional<Transaction> transaction(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject bank = new JSONObject(response);

        return Optional.of(new TransactionDecoder(bank));
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
  }

  @Override public ResponseOld transactions(String response)
  {
    return new DefaultResponse(response);
  }

  @Override public Optional<User> user(String response)
  {
    log.trace("{} {}", version, response);

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject user = new JSONObject(response);

        return Optional.of(new UserDecoder(user));
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
  }

  @Override public Iterable<User> users(String response)
  {
    log.trace("{} {}", version, response);

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }

    return () -> new Iterator<User>() // Iterable as lambda
    {
      @Override public boolean hasNext()
      {
        return iterator.hasNext();
      }

      @Override public User next()
      {
        Object next = iterator.next();

        if(!(next instanceof JSONObject))
        {
          log.error("{}", next);

          return null;
        }

        return new UserDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array.iterator();
    };
  }

  protected JSONArray array(String json)
  {
    try
    {
      return new JSONArray(json);
    }
    catch(JSONException e)
    {
      log.error("{}", json);

      return null;
    }
  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  static final Logger log = LoggerFactory.getLogger(DecoderSep2016.class);
  final Transport.Version version;

  protected class DefaultResponse implements ResponseOld
  {
    public DefaultResponse(String response)
    {
      this.response = response;

      log.trace("{} {}", version, response);

      if(isNull(response) || response.equals("null"))
      {
        data = Collections.emptyList();
        more = false;
      }
      else
      {
        Object wrapper = scan(response);

        if(wrapper instanceof JSONArray)
        {
          more = false;
          data = collect(JSONArray.class.cast(wrapper));
        }
        else if(wrapper instanceof JSONObject)
        {
          JSONObject json = JSONObject.class.cast(wrapper);

          more = json.optBoolean("more");
          data = collect(json.optJSONArray("data"));
        }
        else
        {
          data = Collections.emptyList();
          more = false;
        }
      }
    }

    protected Object scan(String response)
    {
      try
      {
        JSONTokener scanner = new JSONTokener(response);

        switch(scanner.nextClean())
        {
          case '[':
            scanner.back();
            return new JSONArray(scanner);
          case '{':
            scanner.back();
            return new JSONObject(scanner);
          default:
            return null;
        }
      }
      catch(JSONException e)
      {
        return null;
      }
    }

    protected ArrayList<Transaction> collect(JSONArray array)
    {
      ArrayList<Transaction> items = new ArrayList<>();

      if(array != null)
      {
        for(Object entry : array)
        {
          try
          {
            if(entry instanceof JSONObject)
            {
              items.add(new TransactionDecoder(JSONObject.class.cast(entry)));
            }
          }
          catch(Exception e)
          {
            // skip
            log.warn("Not a transaction: {}", entry);
          }
        }
      }

      return items;
    }

    @Override public boolean more()
    {
      return more;
    }

    @Override public List<Transaction> transactions()
    {
      return data;
    }

    final String response;
    final List<Transaction> data;
    final boolean more;
  }

  protected static class DefaultRequest implements Request
  {
    public DefaultRequest(String request)
    {
      this.request = request;
      json = new JSONObject(request);
      name = json.optString("name", "");
      version = json.optString("version", "");
    }

    @Override public Optional<String> accountId()
    {
      return Optional.ofNullable(json.optString("account", null));
    }

    @Override public Optional<String> bankId()
    {
      return Optional.ofNullable(json.optString("bank", null));
    }

    @Override public Optional<String> transactionId()
    {
      return Optional.ofNullable(json.optString("transaction", null));
    }

    @Override public Optional<String> userId()
    {
      return Optional.ofNullable(json.optString("user", null));
    }

    @Override public Optional<String> amount()
    {
      return Optional.ofNullable(json.optString("amount", null));
    }

    @Override public Optional<String> currency()
    {
      return Optional.ofNullable(json.optString("currency", null));
    }

    @Override public Optional<String> otherAccountId()
    {
      return Optional.ofNullable(json.optString("otherId", null));
    }

    @Override public Optional<String> otherAccountCurrency()
    {
      return Optional.ofNullable(json.optString("otherCurrency", null));
    }

    @Override public Optional<String> transactionType()
    {
      return Optional.ofNullable(json.optString("transactionType", null));
    }

    @Override public Optional<Network.Target> target()
    {
      return Optional.ofNullable(
        Network.target(json.optString("target", null)));
    }

    @Override public int offset()
    {
      return json.optInt("offset", 0);
    }

    @Override public int size()
    {
      return json.optInt("size", 0);
    }

    @Override public Optional<String> field()
    {
      return Optional.ofNullable(json.optString("field", null));
    }

    @Override public Optional<com.tesobe.obp.transport.Pager.SortOrder> sort()
    {
      return Optional.ofNullable(
        json.optEnum(com.tesobe.obp.transport.Pager.SortOrder.class, "sort"));
    }

    @Override public Optional<ZonedDateTime> earliest()
    {
      return Optional.ofNullable(
        Json.zonedDateTimeFromJson(json.optString("earliest", null)));
    }

    @Override public Optional<ZonedDateTime> latest()
    {
      return Optional.ofNullable(
        Json.zonedDateTimeFromJson(json.optString("latest", null)));
    }

    /**
     * @return empty string if absent or without value
     */
    @Override public String name()
    {
      return name;
    }

    /**
     * @return empty string if absent or without value
     */
    @Override public String version()
    {
      return version;
    }

    @Override public String raw()
    {
      return request;
    }

    @Override public String toString()
    {
      return json.toString();
    }

    private final String request;

    JSONObject json;
    String name;
    String version;
  }
}
