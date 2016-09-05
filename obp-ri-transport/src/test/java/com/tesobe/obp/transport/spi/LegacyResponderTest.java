/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
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

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.legacy;
import static com.tesobe.obp.util.MethodMatcher.returns;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class LegacyResponderTest
{
  @Before public void setup()
  {
    Transport.Factory factory = Transport.factory(legacy, json)
      .orElseThrow(RuntimeException::new);

    decoder = factory.decoder();
    responder = new MockLegacyResponder(decoder, factory.encoder());
  }

  @Test public void getPrivateAccount() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "id-x";
    String userId = "user-x";
    String request = new JSONObject().put("getBankAccount",
      new JSONObject().put("bankId", bankId).put("accountId", accountId)
        .put("username", userId)).toString();
    String response = responder.respond(new Message(id, request));
    Optional<Account> account = decoder.account(response);

    assertThat(account, hasValue(returns("id", "account-x")));
  }

  @Test public void getPrivateAccounts() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-x";
    String userId = "user-x";
    String request = new JSONObject().put("getBankAccounts",
      new JSONObject().put("bankId", bankId).put("username", userId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Iterable<Account> accounts = decoder.accounts(response);
    List<String> ids = new ArrayList<>();

    accounts.forEach(account -> assertThat(account.bank(), is(bankId)));
    accounts.forEach(account -> ids.add(account.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPrivateBank() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-1";
    String userId = "user-1";
    String request = new JSONObject().put("getBank",
      new JSONObject().put("bankId", bankId).put("username", userId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Bank> bank = decoder.bank(response);

    assertThat(bank, hasValue(returns("id", "id-1")));
  }

  @Test public void getPrivateBanks() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-1";
    String request = new JSONObject()
      .put("getBanks", new JSONObject().put("username", userId)).toString();
    String response = responder.respond(new Message(id, request));
    Iterable<Bank> bank1s = decoder.banks(response);
    List<String> ids = new ArrayList<>();

    bank1s.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPrivateTransaction() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";
    String transactionId = "transaction-x";
    String request = new JSONObject().put("getTransaction",
      new JSONObject().put("bankId", bankId).put("username", userId)
        .put("accountId", accountId).put("transactionId", transactionId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Transaction> transaction = decoder.transaction(response);

    assertThat(transaction, hasValue(returns("id", "transaction-x")));
  }

  @Test public void getPrivateTransactions() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bank-x";
    String userId = "user-x";
    String request = new JSONObject().put("getTransactions",
      new JSONObject().put("username", userId).put("bankId", bankId)
        .put("accountId", accountId)).toString();
    String response = responder.respond(new Message(id, request));
    Iterable<Transaction> transactions = decoder.transactions(response);
    List<String> ids = new ArrayList<>();

    transactions.forEach(t -> ids.add(t.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  /**
   * Username is the user requested. The session user <b>should</b> be sent
   * but the north side does not implement this.
   *
   * @throws Exception aua
   */
  @Test public void getPrivateUser() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-x@example.com";
    String request = new JSONObject()
      .put("getUser", new JSONObject().put("username", userId)).toString();
    String response = responder.respond(new Message(id, request));
    Optional<User> user = decoder.user(response);

    assertThat(user, hasValue(returns("email", userId)));
  }

  @Test public void getPublicAccount() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "id-x";
    String request = new JSONObject().put("getBankAccount",
      new JSONObject().put("bankId", bankId).put("accountId", accountId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Optional<Account> account = decoder.account(response);

    assertThat(account, hasValue(returns("id", "account-x")));
  }

  @Test public void getPublicAccounts() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-x";
    String request = new JSONObject()
      .put("getBankAccounts", new JSONObject().put("bankId", bankId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Iterable<Account> accounts = decoder.accounts(response);
    List<String> ids = new ArrayList<>();

    accounts.forEach(account -> assertThat(account.bank(), is(bankId)));
    accounts.forEach(account -> ids.add(account.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPublicBank() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String bankId = "id-1";
    String request = new JSONObject()
      .put("getBank", new JSONObject().put("bankId", bankId)).toString();
    String response = responder.respond(new Message(id, request));
    Optional<Bank> bank = decoder.bank(response);

    assertThat(bank, hasValue(returns("id", "id-1")));
  }

  @Test public void getPublicBanks() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String request = "{\"getBanks\":{\"username\":\"\"}}";
    String response = responder.respond(new Message(id, request));
    Iterable<Bank> bank1s = decoder.banks(response);
    List<String> ids = new ArrayList<>();

    bank1s.forEach(bank -> ids.add(bank.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  @Test public void getPublicTransaction() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bank-x";
    String transactionId = "transaction-x";
    String request = new JSONObject().put("getTransaction",
      new JSONObject().put("bankId", bankId).put("accountId", accountId)
        .put("transactionId", transactionId)).toString();
    String response = responder.respond(new Message(id, request));
    Optional<Transaction> transaction = decoder.transaction(response);

    assertThat(transaction, hasValue(returns("id", "transaction-x")));
  }

  @Test public void getPublicTransactions() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String accountId = "account-x";
    String bankId = "bank-x";
    String request = new JSONObject().put("getTransactions",
      new JSONObject().put("bankId", bankId).put("accountId", accountId))
      .toString();
    String response = responder.respond(new Message(id, request));
    Iterable<Transaction> transactions = decoder.transactions(response);
    List<String> ids = new ArrayList<>();

    transactions.forEach(t -> ids.add(t.id()));

    assertThat(ids, equalTo(Arrays.asList("id-1", "id-2")));
  }

  /**
   * Username is the user requested. The session user should not be sent, as
   * this is the anonymous variant.
   *
   * @throws Exception aua
   */
  @Test public void getPublicUser() throws Exception
  {
    String id = UUID.randomUUID().toString();
    String userId = "user-x@example.com";
    String request = new JSONObject()
      .put("getUser", new JSONObject().put("username", userId)).toString();
    String response = responder.respond(new Message(id, request));
    Optional<User> user = decoder.user(response);

    assertThat(user, hasValue(returns("email", userId)));
  }

  @Test public void savePrivateTransaction() throws Exception
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
      .put("saveTransaction", new JSONObject()
        .put("username", userId)
        .put("accountId", accountId)
        .put("currency", currency)
        .put("amount", amount)
        .put("otherAccountId", otherAccountId)
        .put("otherAccountCurrency", otherAccountCurrency)
        .put("transactionType", transactionType)
      ).toString();
    String response = responder.respond(new Message(id, request));
    Optional<String> tid = decoder.transactionId(response);

    assertThat(tid, hasValue("tid-x"));
  }


  private Decoder decoder;
  private Receiver responder;
}