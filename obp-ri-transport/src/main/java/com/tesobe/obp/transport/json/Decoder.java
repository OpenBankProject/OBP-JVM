/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Internal JSON decoder. Only called by trusted code.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class Decoder
  implements com.tesobe.obp.transport.spi.Decoder
{
  public Decoder(Transport.Version v)
  {
    version = v;
  }

  @Override public Optional<Request> request(String request)
  {
    return Optional.of(new Request()
    {
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

      /**
       * @return null if absent or without value
       */
      @Override public String name()
      {
        return name;
      }

      @Override public String version() { return version; }

      @Override public String raw()
      {
        return request;
      }

      @Override public String toString()
      {
        return json.toString();
      }

      JSONObject json = new JSONObject(request);
      String name = json.optString("name", null);
      String version = json.optString("version", null);
    });
  }

  @Override public Optional<Account> account(String response)
  {
    log.trace("{} {}", version, String.valueOf(response));

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

  @Override public Iterable<Account> accounts(String response)
  {
    log.trace("{} {}", version, String.valueOf(response));

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }


    return () -> new Iterator<Account>()
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

  @Override public Optional<Bank> bank(String response)
  {
    log.trace("{} {}", version, String.valueOf(response));

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
    log.trace("{} {}", version, String.valueOf(response));

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }

    return () -> new Iterator<Bank>()
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
    log.trace("{} {}", version, String.valueOf(response));

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
    log.trace("{} {}", version, String.valueOf(response));

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

  @Override public Iterable<Transaction> transactions(String response)
  {
    log.trace("{} {}", version, String.valueOf(response));

    if(isNull(response) || response.equals("null"))
    {
      return Collections::emptyIterator;
    }

    JSONArray array = array(response);

    if(array == null)
    {
      return Collections::emptyIterator;
    }

    return () ->
    {
      return new Iterator<Transaction>()
      {
        @Override public boolean hasNext()
        {
          return iterator.hasNext();
        }

        @Override public Transaction next()
        {
          Object next = iterator.next();

          if(!(next instanceof JSONObject))
          {
            log.error("{}", next);

            return null;
          }

          return new TransactionDecoder(JSONObject.class.cast(next));
        }

        final Iterator<Object> iterator = array.iterator();
      };
    };
  }

  @Override public Optional<User> user(String response)
  {
    log.trace("{} {}", version, String.valueOf(response));

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject bank = new JSONObject(response);

        return Optional.of(new UserDecoder(bank));
      }
      catch(JSONException e)
      {
        log.error("{}", response);
      }
    }

    return Optional.empty();
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

  final Transport.Version version;
  static final Logger log = LoggerFactory.getLogger(Decoder.class);
}
