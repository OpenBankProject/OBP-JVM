/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.Sep2016;
import static com.tesobe.obp.util.MethodMatcher.get;
import static com.tesobe.obp.util.MethodMatcher.optionallyReturns;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test compatibility with Scala.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent") public class ReceiverTest
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport
      .factory(Sep2016, json)
      .orElseThrow(RuntimeException::new);

    decoder = factory.decoder();
    responder = new MockReceiver(factory.codecs());
  }

  @Test public void getAccount() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "id-x";
    String userId = "user-x";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get account")
      .put("bank", bankId)
      .put("account", accountId)
      .put("user", userId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Account> account = decoder.account(response);

    assertThat(account, optionallyReturns("id", "account-x"));
  }

  @Test public void getBankAccounts() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-x";
    String userId = "user-x";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get accounts")
      .put("bank", bankId)
      .put("user", userId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Iterable<? extends Account> accounts = decoder.accounts(response);
    List<String> ids = new ArrayList<>();

    accounts.forEach(account -> assertThat(account.bank(), is(bankId)));
    accounts.forEach(account -> ids.add(account.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getBank() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-1";
    String userId = "user-1";
    String request = new JSONObject()
      .put("version", Sep2016).put("name", "get bankOld")
      .put("bank", bankId)
      .put("user", userId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Bank> bank = decoder.bankOld(response);

    assertThat(bank, optionallyReturns("id", "id-1"));
  }

  @Test public void getBanks() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-1";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get banks")
      .put("user", userId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Iterable<? extends Bank> bank1s = decoder.banks(response);
    List<String> ids = new ArrayList<>();

    bank1s.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getTransaction() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bankOld-x";
    String userId = "user-x";
    String transactionId = "transaction-x";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get transaction")
      .put("bank", bankId)
      .put("user", userId)
      .put("account", accountId)
      .put("transaction", transactionId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Transaction> transaction = decoder.transaction(response);

    assertThat(transaction, optionallyReturns("id", "transaction-x"));
  }

  @Test public void getTransactions() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bankOld-x";
    String userId = "user-x";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get transactions")
      .put("use", userId)
      .put("bank", bankId)
      .put("account", accountId)
      .put("size", 2)
      .toString();
    String response = responder.respond(new Message(id, request));
    Decoder.ResponseOld r = decoder.transactions(response);
    Iterable<? extends Transaction> transactions = r.transactions();
    List<String> ids = new ArrayList<>();

    transactions.forEach(t -> ids.add(t.id()));

    assertThat(ids, equalTo(Arrays.asList("id-0", "id-1")));
  }

  /**
   * Username is the user requested. The session user <b>should</b> be sent
   * but the north side does not implement this.
   *
   * @throws Exception aua
   */
  @Test public void getUser() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-x@example.com";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "get user")
      .put("user", userId)
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<User> user = decoder.user(response);

    assertThat(user, optionallyReturns("email", userId));
  }

  @Test public void saveTransaction() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-x";
    String accountId = "account-x";
    String currency = "currency-x";
    String amount = "amount-x";
    String otherAccountId = "account-y";
    String otherAccountCurrency = "currency-y";
    String transactionType = "type-x";
    String request = new JSONObject()
      .put("version", Sep2016)
      .put("name", "save transaction")
      .put("user", userId)
      .put("account", accountId)
      .put("currency", currency)
      .put("amount", amount)
      .put("otherId", otherAccountId)
      .put("otherCurrency", otherAccountCurrency)
      .put("transactionType", transactionType)
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<String> tid = decoder.transactionId(response);

    assertThat(tid, get("tid-x"));
  }

  private Decoder decoder;
  private Receiver responder;
}
