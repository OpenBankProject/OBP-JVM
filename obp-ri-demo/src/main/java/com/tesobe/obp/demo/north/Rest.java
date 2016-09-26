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
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.ipAddress;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

@SuppressWarnings("WeakerAccess") class Rest
{
  Rest(Connector c, String ip, int port, Iterable<User> namedUsers)
  {
    connector = c;
    users = new Users(namedUsers);
    html = new Html(ip, port, users.users());

    staticFileLocation(getClass().getPackage().getName().replace('.', '/'));
    ipAddress(ip);
    port(port);

    get("/", this::index);
    get("/obp", this::index);
    get("/obp/", this::index);
    get("/obp/demo", this::index);
    get("/obp/demo/", this::index);
    get("/obp/demo/bank/:bank", this::getBank);
    get("/obp/demo/bank/:bank/account/:account", this::getAccount);
    get("/obp/demo/bank/:bank/accounts", this::getAccounts);
    get("/obp/demo/bank/:bank/account/:account/transaction/:transaction",
      this::getTransaction);
    get("/obp/demo/bank/:bank/account/:account/transactions",
      this::getTransactions);
    get("/obp/demo/banks", this::getBanks);
    get("/obp/demo/user/:user", this::getUser);
    get("/obp/demo/user/:user/banks", this::getBanks);
    get("/obp/demo/user/:user/bank/:bank", this::getBank);
    get("/obp/demo/user/:user/bank/:bank/account/:account", this::getAccount);
    get("/obp/demo/user/:user/bank/:bank/accounts", this::getAccounts);
    get(
      "/obp/demo/user/:user/bank/:bank/account/:account/transaction/:transaction",
      this::getTransaction);
    get("/obp/demo/user/:user/bank/:bank/account/:account/transactions",
      this::getTransactions);

    awaitInitialization();
  }

  protected Object getUser(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bank");
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

      if(account.isPresent())
      {
        return html.render(userId, bankId, accountId, null, account.get());
      }
      else
      {
        return html.render(userId, bankId, accountId, null, (Id)null);
      }
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

    return html.render(userId, null, null, null, (Id)null);
  }

  protected Object getAccount(Request request, Response response)
  {
    response.type("text/html;charset=UTF-8");

    try
    {
      String bankId = request.params(":bank");
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

      if(account.isPresent())
      {
        return html.render(userId, bankId, accountId, null, account.get());
      }
      else
      {
        return html.render(userId, bankId, accountId, null, (Id)null);
      }
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
      String bankId = request.params(":bank");
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

      if(!items.isEmpty())
      {
        return html.render(userId, bankId, null, null, items);
      }
      else
      {
        return html.render(userId, bankId, null, null, (List<Id>)null);
      }
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
      String bankId = request.params(":bank");
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

      if(bank.isPresent())
      {
        return html.render(userId, bankId, null, null, bank.get());
      }
      else
      {
        return html.render(userId, bankId, null, null, (Id)null);
      }
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

      if(!items.isEmpty())
      {
        return html.render(userId, null, null, null, items);
      }
      else
      {
        return html.render(userId, null, null, null, (List<Id>)null);
      }
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
      String bankId = request.params(":bank");
      String accountId = request.params(":account");
      String transactionId = request.params(":user");
      String userId = request.params(":user");
      Optional<Transaction> transaction;

      if(isNull(userId))
      {
        transaction = connector
          .getTransaction(bankId, accountId, transactionId);
      }
      else
      {
        transaction = connector
          .getTransaction(bankId, accountId, transactionId, userId);
      }

      if(transaction.isPresent())
      {
        return html
          .render(userId, bankId, accountId, transactionId, transaction.get());
      }
      else
      {
        return html.render(userId, bankId, accountId, transactionId, (Id)null);
      }
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
      String bankId = request.params(":bank");
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

      if(!items.isEmpty())
      {
        return html.render(userId, bankId, null, null, items);
      }
      else
      {
        return html.render(userId, bankId, null, null, (List<Id>)null);
      }
    }
    catch(Exception e)
    {
      return html.error(e);
    }
  }

  protected final Connector connector;
  protected final Html html;
  protected final Users users;

  final static Logger log = LoggerFactory.getLogger(Rest.class);
}
