/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Internal JSON encoder. Only called by trusted code.
 * <p>
 * Todo make robust.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class EncoderV0
        implements com.tesobe.obp.transport.spi.Encoder
{
    public EncoderV0(Transport.Version v)
    {
        version = v;
    }

    @Override public Request getUser(String userId, OutboundContext context)
    {
        return request("getUser").arguments("username", userId);
    }

    @Override public Request getBanks(OutboundContext context)
    {
        if (context != null && context.user != null){
            return request("getBanks").arguments("username", context.user.userId);
        } else {
            return request("getBanks");
        }
    }

    @Override
    public Request getTransaction(String bankId, String accountId,
                                  String transactionId, OutboundContext context)
    {
        if (context != null && context.user != null){
            return request("getTransaction")
                    .arguments("username", context.user.userId, "accountId", accountId, "bankId", bankId,
                            "transactionId", transactionId);
        } else {
            return request("getTransaction")
                    .arguments("bankId", bankId, "accountId", accountId, "transactionId", transactionId);
        }
    }

    @Override
    public Request getTransactions(String bankId, String accountId,
                                   OutboundContext context)
    {
        if (context != null && context.user != null) {
            return request("getTransactions")
                    .arguments("bankId", bankId, "accountId", accountId, "userId", context.user.userId);
        } else {
            return request("getTransactions")
                    .arguments("bankId", bankId, "accountId", accountId);
        }
    }

    @Override public Request getAccount(OutboundContext context, String bankId,
                                        String accountId)
    {
        if (context != null && context.user != null) {
            return request("getBankAccount")
                    .arguments("username", context.user.userId, "bankId", bankId, "accountId", accountId);
        } else {
            return request("getBankAccount")
                    .arguments("bankId", bankId, "accountId", accountId);
        }
    }

    @Override public Request getAccounts(OutboundContext context, String bankId)
    {
        if (context != null && context.user != null) {
            return request("getBankAccounts")
                    .arguments("username", context.user.userId, "bankId", bankId);
        } else {
            return request("getBankAccounts").arguments("bankId", bankId);
        }
    }

    @Override public Request getBank(OutboundContext context, String bankId)
    {
        if (context != null && context.user != null) {
            return request("getBank").arguments("username", context.user.userId, "bankId", bankId);
        } else {
            return request("getBank").arguments("bankId", bankId);
        }
    }


    protected RequestBuilder request(String name)
    {
        return new RequestBuilder(name);
    }

    @Override public String account(Account a)
    {
        JSONObject json = json(a);

        return json != null ? json.toString() : JSONObject.NULL.toString();
    }

    @Override public String accounts(List<Account> accounts)
    {
        JSONArray result = new JSONArray();

        if(nonNull(accounts))
        {
            accounts.forEach(account -> json(account, result));
        }

        return result.toString();
    }

    @Override public String bank(Bank b)
    {
        JSONObject json = json(b);

        return json != null ? json.toString() : JSONObject.NULL.toString();
    }

    @Override public String inboundContext(InboundContext inboundContext)
    {
        JSONObject json = json(inboundContext);

        return json != null ? json.toString() : JSONObject.NULL.toString();
    }

    @Override public String banks(List<Bank> banks)
    {
        JSONArray result = new JSONArray();

        if(nonNull(banks))
        {
            banks.forEach(bank -> json(bank, result));
        }

        return result.toString();
    }

    protected JSONObject json(Account a)
    {
        if(nonNull(a))
        {
            return new AccountEncoder(a).toJson();
        }

        return null;
    }

    protected void json(Account a, JSONArray result)
    {
        if(nonNull(a))
        {
            JSONObject json = json(a);

            result.put(json != null ? json : JSONObject.NULL);
        }
        else
        {
            result.put(JSONObject.NULL);
        }
    }

    protected void json(Bank b, JSONArray result)
    {
        if(nonNull(b))
        {
            JSONObject json = json(b);

            result.put(json != null ? json : JSONObject.NULL);
        }
        else
        {
            result.put(JSONObject.NULL);
        }
    }

    protected JSONObject json(Bank b)
    {
        if(nonNull(b))
        {
            return new BankEncoder(b).toJson();
        }

        return null;
    }

    protected JSONObject json(InboundContext inboundContext)
    {
        if(nonNull(inboundContext))
        {
            return new InboundContextEncoder(inboundContext).toJson();
        }

        return null;
    }

    protected void json(InboundContext inboundContext, JSONArray result)
    {
        if(nonNull(inboundContext))
        {
            JSONObject json = json(inboundContext);

            result.put(json != null ? json : JSONObject.NULL);
        }
        else
        {
            result.put(JSONObject.NULL);
        }
    }

    private void json(Transaction t, JSONArray result)
    {
        if(nonNull(t))
        {
            JSONObject json = json(t);

            result.put(json != null ? json : JSONObject.NULL);
        }
        else
        {
            result.put(JSONObject.NULL);
        }
    }

    protected JSONObject json(Transaction t)
    {
        if(nonNull(t))
        {
            return new TransactionEncoder(t).toJson();
        }

        return null;
    }

    protected JSONObject json(User u)
    {
        if(nonNull(u))
        {
            return new UserEncoder(u).toJson();
        }

        return null;
    }


    @Override public String transaction(Transaction t)
    {
        JSONObject json = json(t);

        return json != null ? json.toString() : JSONObject.NULL.toString();
    }

    @Override public String transactions(List<Transaction> ts)
    {
        JSONArray result = new JSONArray();

        if(nonNull(ts))
        {
            ts.forEach(transaction -> json(transaction, result));
        }

        return result.toString();
    }

    @Override public String user(User u)
    {
        JSONObject json = json(u);

        return json != null ? json.toString() : JSONObject.NULL.toString();
    }

    @Override public String error(String message)
    {
        return new JSONObject().put("error", message).toString();
    }

    @Override public String notFound()
    {
        return JSONObject.NULL.toString();
    }


//  protected void put(JSONArray result, Connector.Bank b)
//  {
//    assert nonNull(result);
//
//    if(nonNull(b))
//    {
//      result.put(json(b));
//    }
//  }

    @Override public String toString()
    {
        return getClass().getTypeName() + "-" + version;
    }

    final Transport.Version version;

    static final Logger log = LoggerFactory.getLogger(EncoderV0.class);

    class RequestBuilder implements Request
    {
        public RequestBuilder(String name)
        {
            this.name = name;

            request.put(name, JSONObject.NULL);
        }

        @Override public String toString()
        {
            log.trace("{} {}", version, request);

            return request.toString();
        }

        public Request arguments(String... kv)
        {
            for(int i = 0; i < kv.length - 1; i += 2)
            {
                arguments.put(kv[i], kv[i + 1]);
            }

            if(request.opt(name) == JSONObject.NULL)
            {
                request.put(name, arguments);
            }

            return this;
        }

        final String name;
        final JSONObject arguments = new JSONObject();
        final JSONObject request = new JSONObject();
    }
}