/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.function.Function.identity;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.ipAddress;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

@SuppressWarnings("WeakerAccess") public class Rest
{
  public Rest(Connector c, String ip, int port)
  {
    connector = c;
    html = new Html(ip, port);

    staticFileLocation(getClass().getPackage().getName().replace('.', '/'));
    ipAddress(ip);
    port(port);

    get("/", this::index);
    get("/obp", this::index);
    get("/obp/", this::index);
    get("/obp/demo", this::index);
    get("/obp/demo/", this::index);
    get("/obp/demo/bankOld/:bankOld", this::getBank);
    get("/obp/demo/bankOld/:bankOld/account/:account", this::getAccount);
    get("/obp/demo/bankOld/:bankOld/accounts", this::getAccounts);
    get("/obp/demo/bankOld/:bankOld/account/:account/transaction/:transaction",
      this::getTransaction);
    get("/obp/demo/bankOld/:bankOld/account/:account/transactions",
      this::getTransactions);
    get("/obp/demo/banks", this::getBanks);
    get("/obp/demo/user/:user", this::getUser);
    get("/obp/demo/user/:user/banks", this::getBanks);
    get("/obp/demo/user/:user/bankOld/:bankOld", this::getBank);
    get("/obp/demo/user/:user/bankOld/:bankOld/account/:account",
      this::getAccount);
    get("/obp/demo/user/:user/bankOld/:bankOld/accounts", this::getAccounts);
    get("/obp/demo/user/:user/bankOld/:bankOld/account/:account/transaction"
        + "/:transaction",
      this::getTransaction);
    get("/obp/demo/user/:user/bankOld/:bankOld/account/:account/transactions",
      this::getTransactions);

    awaitInitialization();
  }

  protected Object getUser(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String accountId = request.params(":account");
      String userId = request.params(":user");
      Optional<Account> account;

      if(isNull(userId))
      {
        account = connector.getAccount(bankId, accountId);
      }
      else
      {
        account = connector.getAccount(bankId, accountId, userId);
      }

      return html.render(userId, bankId, accountId, null,
        account.map(identity()).orElse(null), User.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object index(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    String userId = request.params(":user");

    return html.render(userId, null, null, null, (Id)null, null, users());
  }

  protected Object getAccount(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String accountId = request.params(":account");
      String userId = request.params(":user");
      Optional<Account> account;

      if(isNull(userId))
      {
        account = connector.getAccount(bankId, accountId);
      }
      else
      {
        account = connector.getAccount(bankId, accountId, userId);
      }

      return html.render(userId, bankId, accountId, null,
        account.map(identity()).orElse(null), Account.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object getAccounts(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String userId = request.params(":user");
      Iterable<Account> accounts;
      List<Id> items = new ArrayList<>();

      if(isNull(userId))
      {
        accounts = connector.getAccounts(bankId);
      }
      else
      {
        accounts = connector.getAccounts(bankId, userId);
      }

      accounts.forEach(items::add);

      return html.render(userId, bankId, null, null, items, Account.class,
        users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object getBank(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String userId = request.params(":user");
      Optional<Bank> bank;

      if(isNull(userId))
      {
        bank = connector.getBank(bankId);
      }
      else
      {
        bank = connector.getBank(bankId, userId);
      }

      return html.render(userId, bankId, null, null,
        bank.map(identity()).orElse(null), Bank.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object getBanks(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String userId = request.params(":user");
      Iterable<Bank> banks;
      List<Id> items = new ArrayList<>();

      if(isNull(userId))
      {
        banks = connector.getBanks();
      }
      else
      {
        banks = connector.getBanks(userId);
      }

      banks.forEach(items::add);

      return html.render(userId, null, null, null, items, Bank.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object getTransaction(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String accountId = request.params(":account");
      String transactionId = request.params(":transaction");
      String userId = request.params(":user");
      Optional<Transaction> transaction;

      if(isNull(userId))
      {
        transaction = connector.getTransaction(bankId, accountId,
          transactionId);
      }
      else
      {
        transaction = connector.getTransaction(bankId, accountId, transactionId,
          userId);
      }

      return html.render(userId, bankId, accountId, transactionId,
        transaction.map(identity()).orElse(null), Transaction.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected Object getTransactions(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bankOld");
      String accountId = request.params(":account");
      String userId = request.params(":user");

      Iterable<Transaction> transactions;
      List<Id> items = new ArrayList<>();

      if(isNull(userId))
      {
        transactions = connector.getTransactions(bankId, accountId);
      }
      else
      {
        transactions = connector.getTransactions(bankId, accountId, userId);
      }

      transactions.forEach(items::add);

      return html.render(userId, bankId, accountId, null, items,
        Transaction.class, users());
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  private List<User> users()
  {
    try
    {
      List<User> users = new ArrayList<>();

      connector.getUsers().forEach(users::add);

      users.add(new Anonymous());

      Collections.sort(users);

      return users;
    }
    catch(InterruptedException e)
    {
      log.error(e.getMessage(), e);

      return Collections.emptyList();
    }
  }

  final static Logger log = LoggerFactory.getLogger(Rest.class);
  protected final Connector connector;
  protected final Html html;

  public static class Anonymous implements User
  {
    @Override public String id()
    {
      return null;
    }

    @Override public String displayName()
    {
      return "Anonymous";
    }

    @Override public String email()
    {
      return "anonymous@example.org";
    }

    @Override public String password()
    {
      return "secret";
    }
  }

  public static class Unkown implements User
  {
    public Unkown(String id)
    {
      this.id = id;
    }

    @Override public String id()
    {
      return id;
    }

    @Override public String displayName()
    {
      return id;
    }

    @Override public String email()
    {
      return null;
    }

    @Override public String password()
    {
      return null;
    }

    public final String id;
  }
}
