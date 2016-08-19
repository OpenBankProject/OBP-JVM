/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.*;
import com.tesobe.obp.transport.spi.DecoderException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import static com.tesobe.obp.util.Strings.white;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Internal JSON decoder. Only called by trusted code.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class DecoderV1
  implements com.tesobe.obp.transport.spi.Decoder
{
  public DecoderV1(Transport.Version v)
  {
    version = v;
  }

  @Override public Request request(String request)
  {
    return new Request()
    {
      @Override public Optional<String> accountId()
      {
        return arguments == null
               ? Optional.empty()
               : white(arguments.optString("accountId", null));
      }

      @Override public Optional<String> bankId()
      {
        return arguments == null
               ? Optional.empty()
               : white(arguments.optString("bankId", null));
      }

      @Override public Optional<String> transactionId()
      {
        return arguments == null
               ? Optional.empty()
               : white(arguments.optString("transactionId", null));
      }

      @Override public Optional<String> userId()
      {
        return arguments == null
               ? Optional.empty()
               : white(arguments.optString("username", null));
      }

      /**
       * @return null if absent or without value
       */
      @Override public String name()
      {
        return name;
      }

      @Override public String function()
      {
        return arguments.optString("function", null);
      }

      @Override public String raw()
      {
        return request;
      }

      @Override public String toString()
      {
        return json.toString();
      }

      JSONObject json = new JSONObject(request);
      JSONObject arguments;
      String name;

      {
        Iterator<String> keys = json.keys(); // Legacy api: only one key

        name = keys.hasNext() ? keys.next() : null;
        arguments = json.opt(name) instanceof JSONObject ? json
          .getJSONObject(name) : null;
      }
    };
  }

  @Override public Optional<Account> account(String response)
    throws DecoderException
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
        throw new DecoderException("Cannot decode: " + response);
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
          throw new DecoderException(String.valueOf(next));
        }

        return new AccountDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array(response).iterator();
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
        throw new DecoderException("Cannot decode: " + response);
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

    //get the banks objects from json
    JSONObject responseObject = new JSONObject(response);
    JSONObject responseObjectInner = responseObject.optJSONObject("response");
    JSONArray banksObject = responseObjectInner.optJSONArray("banks");
    String banks = banksObject.toString();

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
          throw new DecoderException(String.valueOf(next));
        }

        return new BankDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array(banks).iterator();
    };
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
        throw new DecoderException("Cannot decode: " + response);
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

    return () -> new Iterator<Transaction>()
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
          throw new DecoderException(String.valueOf(next));
        }

        return new TransactionDecoder(JSONObject.class.cast(next));
      }

      final Iterator<Object> iterator = array(response).iterator();
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
        throw new DecoderException("Cannot decode: " + response);
      }
    }

    return Optional.empty();
  }

  @Override public Optional<InboundContext> inboundContext(String response)
          throws DecoderException
  {
    log.trace("{} {}", version, String.valueOf(response));

    if(nonNull(response) && !response.equals("null"))
    {
      try
      {
        JSONObject responseObject = new JSONObject(response);

        return Optional.of(InboundContextDecoder.inboundContextFromJsonObject(responseObject));
      }
      catch(JSONException e)
      {
        throw new DecoderException("Cannot decode: " + response);
      }
    }

    return null;
  }

  protected JSONArray array(String json) throws DecoderException
  {
    try
    {
      return new JSONArray(json);
    }
    catch(JSONException e)
    {
      throw new DecoderException(json);
    }
  }

//  @Override public Iterable<Connector.Bank> banks(String response)
//  {
//    List<Connector.Bank> result = new ArrayList<>();
//
//    log.trace(String.valueOf(response));
//
//    if(nonNull(response))
//    {
//      JSONArray array = new JSONArray(response);
//
//      for(Object bank : array)
//      {
//        if(bank instanceof JSONObject)
//        {
//          JSONObject b = (JSONObject)bank;
//
//          // @formatter:off
//          result.add(new Connector.Bank(
//            b.optString("id", null),
//            b.optString("short_name", null),
//            b.optString("full_name", null),
//            b.optString("logo", null),
//            b.optString("website", null)));
//          // @formatter:on
//        }
//      }
//    }
//
//    return result;
//  }

//  /**
//   * @return en empty iterable
//   */
//  @Override public Iterable<Connector.Bank> banks()
//  {
//    return Collections.emptyList();
//  }

  @Override public String toString()
  {
    return getClass().getTypeName() + "-" + version;
  }

  final Transport.Version version;
  static final Logger log = LoggerFactory.getLogger(DecoderV1.class);
}
