/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.DecoderException;

import java.util.Optional;

/**
 * North side API.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public interface Connector
{
  /**
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param accountId An invalid account id means an empty result.
   * An all white space account id is invalid.
   * @param context Context object containing information about user, view, token, etc..
   * An all white space user id is invalid.
   *
   * @return An empty result if the account is not explicitly linked to the
   * user.
   * If the account is public but not linked to the user, empty will be
   * returned.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   */
  Optional<Account> getAccount(String bankId, String accountId, Context context) throws InterruptedException, DecoderException;

  /**
   * All private accounts the user is explicitly linked to.
   * No public accounts that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the accounts returned may be {@code null}.
   * A {@link DecoderException} can be thrown at any time because the decoding
   * is done lazily during the iteration.
   *
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param context Context object containing information about user, view, token, etc..
   * An all white space user id is invalid.
   *
   * @return The user's private banks or an empty result.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   *                              The exception may be delayed until the
   *                              iterable is dereferenced.
   */
  Iterable<Account> getAccounts(String bankId, Context context) throws InterruptedException, DecoderException;

  /**
   * @param bankId An invalid bank id means an empty result.
   * An all white space bank id is invalid.
   * @param context Context object containing information about user, view, token, etc..
   * An all white space user id is invalid.
   *
   * @return An empty result if the bank is not explicitly linked to the user.
   * If the bank is public but not linked to the user, empty will be returned.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   */
  Optional<Bank> getBank(String bankId, Context context) throws InterruptedException, DecoderException;

  /**
   * All private banks the user is explicitly linked to.
   * No public banks that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the banks returned may be {@code null}.
   * A {@link DecoderException} can be thrown at any time because the decoding
   * is done lazily during the iteration.
   *
   * @param context Context object containing information about user, view, token, etc..
   * An all white space user id is invalid.
   *
   * @return The user's private banks or an empty result.
   *
   * @throws InterruptedException Network trouble
   * @throws DecoderException     Invalid content in the network packet.
   *                              The exception may be delayed until the
   *                              iterable is dereferenced.
   */
  Iterable<Bank> getBanks(Context context) throws InterruptedException, DecoderException;

  Optional<Transaction> getTransaction(String bankId, String accountId, String transactionId, Context context) throws InterruptedException, DecoderException;

  Iterable<Transaction> getTransactions(String bankId, String accountId, Context context) throws InterruptedException, DecoderException;

  Optional<User> getUser(String userId, Context context) throws InterruptedException, DecoderException;


//  public class AccountId implements Serializable
//  {
//    public AccountId(String id, String bank)
//    {
//      this.id = id;
//      this.bank = bank;
//    }
//
//    @Override public boolean equals(Object o)
//    {
//      if(this == o)
//      {
//        return true;
//      }
//
//      if(!(o instanceof AccountId))
//      {
//        return false;
//      }
//
//      AccountId accountId = (AccountId)o;
//
//      return Objects.equals(id, accountId.id) && Objects
//        .equals(bank, accountId.bank);
//    }
//
//    @Override public int hashCode()
//    {
//      return Objects.hash(id, bank);
//    }
//
//    public JSONObject toJson()
//    {
//      // @formatter:off
//    return new JSONObject()
//      .put("id", id)
//      .put("bank", bank);
//    // @formatter:on
//    }
//
//    public final String id;
//    public final String bank;
//    static final long serialVersionUID = 42L;
//  }
//
//  public class Bank implements Comparable<Bank>, Serializable
//  {
//    public Bank(String id, String name, String fullName, String logo,
//      String url)
//    {
//      this.id = id;
//      this.name = name;
//      this.fullName = fullName;
//      this.logo = logo;
//      this.url = url;
//    }
//
//    @Override public int compareTo(Bank b)
//    {
//      return this == b ? 1 : id == null ? -1 : id.compareTo(b.id);
//    }
//
//    @Override public boolean equals(Object bank)
//    {
//      return this == bank || ((bank instanceof Bank) && Objects
//        .equals(id, ((Bank)bank).id));
//    }
//
//    @Override public int hashCode()
//    {
//      return Objects.hash(id);
//    }
//
//    @Override public String toString()
//    {
//      return "Bank{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
//    }
//
//    public final String id;
//    public final String name;
//    public final String fullName;
//    public final String logo;
//    public final String url;
//    static final long serialVersionUID = 42L;
//  }
//
//  public class Transaction implements Serializable
//  {
//    public Transaction(String id, String accountId, String bankId,
//      String counterPartyName, String counterPartyAccountNumber, String type,
//      String description, String posted, String completed, String new_balance,
//      String value)
//    {
//      this.id = id;
//      this.this_account = new AccountId(accountId, bankId);
//      this.counterparty = new KafkaInboundTransactionCounterparty(
//        counterPartyName, counterPartyAccountNumber);
//      this.details = new KafkaInboundTransactionDetails(type, description,
//        posted, completed, new_balance, value);
//    }
//
//    public JSONObject toJson()
//    {
//      // @formatter:off
//    return new JSONObject()
//      .put("id", id)
//      .put("this_account", this_account.toJson())
//      .put("counterparty", counterparty.toJson())
//      .put("details", details.toJson());
//    // @formatter:on
//    }
//
//    public final String id;
//    public final AccountId this_account;
//    public final KafkaInboundTransactionCounterparty counterparty;
//    public final KafkaInboundTransactionDetails details;
//
//    public static class KafkaInboundTransactionCounterparty
//      implements Serializable
//    {
//      public KafkaInboundTransactionCounterparty(String name,
//        String account_number)
//      {
//        this.name = name;
//        this.account_number = account_number;
//      }
//
//      public JSONObject toJson()
//      {
//        // @formatter:off
//      return new JSONObject()
//        .put("name", name)
//        .put("account_number", account_number);
//      // @formatter:on
//      }
//
//      public final String name;
//      public final String account_number;
//    }
//
//    public static class KafkaInboundTransactionDetails implements Serializable
//    {
//      public KafkaInboundTransactionDetails(String type, String description,
//        String posted, String completed, String new_balance, String value)
//      {
//        this.type = type;
//        this.description = description;
//        this.posted = posted;
//        this.completed = completed;
//        this.new_balance = new_balance;
//        this.value = value;
//      }
//
//      public JSONObject toJson()
//      {
//        // @formatter:off
//      return new JSONObject()
//        .put("type", type)
//        .put("description", description)
//        .put("posted", posted)
//        .put("completed", completed)
//        .put("new_balance", new_balance)
//        .put("value", value);
//      // @formatter:on
//      }
//
//      public final String type;
//      public final String description;
//      public final String posted;
//      public final String completed;
//      public final String new_balance;
//      public final String value;
//    }
//
//    static final long serialVersionUID = 42L;
//  }
//
//  public class User implements Serializable
//  {
//    public User(String email, String password, String display_name)
//    {
//      this.email = email;
//      this.password = password;
//      this.display_name = display_name;
//    }
//
//    public JSONObject toJson()
//    {
//      // @formatter:off
//    return new JSONObject()
//      .put("email", email)
//      .put("password", password)
//      .put("display_name", display_name);
//    // @formatter:on
//    }
//
//    public final String email;
//    public final String password;
//    public final String display_name;
//    static final long serialVersionUID = 42L;
//  }
}
