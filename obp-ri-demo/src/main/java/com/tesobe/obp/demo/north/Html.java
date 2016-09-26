package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.util.tbd;
import j2html.tags.ContainerTag;

import java.util.Collections;
import java.util.List;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h5;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.title;
import static java.lang.String.format;

@SuppressWarnings("WeakerAccess") public class Html
{
  public Html(String ip, int port, List<User> us)
  {
    url = format("http://%s:%s/obp/demo%%s", ip, port);
    users = us;

    accountsUrl = "/bank/%s/accounts";
    bankDetailUrl = "/bank/%s";
    banksUrl = "/banks";
    banksLink = a("Banks").withHref(format(url, banksUrl)).withAlt(banksUrl);
    transactionsUrl = "/bank/%s/account/%s/transactions";
    userUrl = "/user/%s%s";

    bottom = div().withClass("clear");

    Collections.sort(users);
  }

  //  public String emit(ContainerTag data)
//  {
//    ContainerTag left = div().withId("left").with(h5("Users"));
//    ContainerTag right = div().withId("right");
//
//    right.with(data);
//    users(users, left);
//
//    return html()
//      .with(header(), body().withId("container").with(left, right, bottom))
//      .render();
//  }
//
//  public String index(User u)
//  {
//    ContainerTag left = div().withId("left").with(h5("Users"));
//    ContainerTag right = div().withId("right").with(h5("Accounts"));
//
//    users(users, left);
//
//    return html()
//      .with(header(), body().withId("container").with(left, right, bottom))
//      .render();
//  }
//
//  public String account()
//  {
//    return emit(accountToTable());
//  }
//
//  public String account(Account a)
//  {
//    return emit(accountToTable(a));
//  }
//
//  public String account(Account account, User user)
//  {
//    return emit(accountToTable(account, user));
//  }
//
//  public String account(List<User> users)
//  {
//    return emit(accountToTable(user));
//  }
//
//  public String accounts(Iterable<Account> accounts)
//  {
//    return emit(accountsToTable(accounts));
//  }
//
//  public String accounts(Iterable<Account> accounts, String bank)
//  {
//    ContainerTag left = div().withId("left").with(h5("Users"));
//    ContainerTag right = div().withId("right");
//
//    data(accounts, null, right, Account.class);
//    users(users, left);
//
//    return html()
//      .with(header(), body().withId("container").with(left, right, bottom))
//      .render();
//  }
//
//  public String accounts(Iterable<Account> accounts, User user)
//  {
//    ContainerTag left = div().withId("left").with(h5("Users"));
//    ContainerTag right = div().withId("right");
//
//    data(accounts, user, right, Account.class);
//    users(users, left);
//
//    return html()
//      .with(header(), body().withId("container").with(left, right, bottom))
//      .render();
//  }
//
//  public String accounts(Iterable<Account> accounts, String bank, User user)
//  {
//    return emit(accountsToTable(accounts, bank, user));
//  }
//
//  public String bank()
//  {
//    throw new tbd();
//  }
//
//  public String bank(User u)
//  {
//    throw new tbd();
//  }
//
//  public String bank(Bank b)
//  {
//    throw new tbd();
//  }
//
//  public String bank(Bank b, User u)
//  {
//    throw new tbd();
//  }
//
//  public String banks(Iterable<Bank> banks)
//  {
//    return emit(banksToTable(banks));
//  }
//
//  public String banks(Iterable<Bank> banks, User user)
//  {
//    return emit(banksToTable(banks, user));
//  }
//
//  protected ContainerTag header()
//  {
//    return head().with(title("OBP Demo"), TagCreator.link()
//        .withHref("https://fonts.googleapis.com/css?family=Open+Sans")
//        .withRel("stylesheet"),
//      TagCreator.link().withHref("/demo.css").withRel("stylesheet"));
//  }
//
//  protected ContainerTag row(Id data, User user, Class<? extends Id> type)
//  {
//    assert data != null;
//
//    ContainerTag row = tr().with(td().with(bankDetailsLink(data.id())),
//      td().with(link(data.id(), user, type)));
//
//    data.fields().stream().map(f -> valueOf(data, f))
//      .forEach(value -> row.with(td(value != null ? value.toString() : "")));
//
//    return row;
//  }
//
//  protected DomContent link(String id, User u, Class<? extends Id> type)
//  {
//    String href;
//
//    switch(type.getSimpleName())
//    {
//      case "Account":
//        href = u.id() == null
//               ? format(url, format(accountsUrl, id))
//               : format(url, format(userUrl, u.id(), format(accountsUrl, id)));
//        break;
//      case "Transaction":
//        href = u.id() == null
//               ? format(url, transactionsUrl)
//               : format(url, format(userUrl, u.id(), transactionsUrl));
//        break;
//      default:
//        href = "xxx";
//    }
//
//    return a("Accounts").withHref(href).withAlt(href);
//  }
//
//  protected DomContent bankDetailsLink(String id)
//  {
//    String detail = format(bankDetailUrl, id);
//
//    return a("Details").withHref(format(url, detail)).withAlt(detail);
//  }
//
//  protected void data(Iterable<? extends Id> items, User user,
//    ContainerTag accumulator, Class<? extends Id> type)
//  {
//    assert type != null;
//
//    String empty;
//    String title;
//
//    switch(type.getSimpleName())
//    {
//      case "Account":
//        empty = "No accounts found!";
//        title = user == null
//                ? "Accounts"
//                : user.displayName() + " • " + "Accounts";
//        break;
//      case "Bank":
//        empty = "No banks found!";
//        title = user == null ? "Banks" : user.displayName() + " • " + "Banks";
//        break;
//      case "Transaction":
//        empty = "No transactions found!";
//        title = user == null
//                ? "Transactions"
//                : user.displayName() + " • " + "Transactions";
//        break;
//      default:
//        empty = "Nothing found!";
//        title = user == null ? "" : user.displayName();
//    }
//
//    accumulator.with(h5(title));
//
//    if(nonNull(items))
//    {
//      List<ContainerTag> rows = new ArrayList<>();
//      ContainerTag table = table();
//
//      stream(items.spliterator(), false).map(i -> row(i, user, type))
//        .filter(Objects::nonNull).forEach(rows::add);
//
//      if(rows.isEmpty())
//      {
//        accumulator.with(p(empty));
//      }
//      else
//      {
//        rows.forEach(table::with);
//
//        accumulator.with(table);
//      }
//    }
//    else
//    {
//      accumulator.with(p(empty));
//    }
//  }
//
  public String error(Exception e)
  {
    return html().with(head().with(title("OBP Demo"),
      body().with(h1("OBP Demo Error").withText(e.getMessage())))).render();
  }

  //
  protected void users(List<User> users, ContainerTag accumulator)
  {
    if(users != null)
    {
      users.forEach(u ->
      {
        String href = u.id() == null
                      ? format(url, banksUrl)
                      : format(url, format(userUrl, u.id(), banksUrl));

        accumulator.with(div().with(a(u.displayName()).withHref(href)));
      });
    }
  }
//
//  protected ContainerTag accountsToTable(Iterable<Account> accounts)
//  {
//    return table();
//  }
//
//  protected ContainerTag accountsToTable(Iterable<Account> accounts,
//    String bank, User user)
//  {
//    return table();
//  }
//
//  protected ContainerTag banksToTable(Iterable<Bank> banks)
//  {
//    return table();
//  }
//
//  protected ContainerTag banksToTable(Iterable<Bank> banks, User user)
//  {
//    return table();
//  }

  public String render(String userId, String bankId, String accountId,
    Object transactionId, Id data)
  {
    throw new tbd();
  }

  public String render(String userId, String bankId, String accountId,
    Object transactionId, List<Id> data)
  {
    ContainerTag left = div().withId("left").with(h5("Users"));
    ContainerTag right = div().withId("right");

    users(users, left);


    return html()
      .with(header(), body().withId("container").with(left, right, bottom))
      .render();
  }

  //    switch(type.getSimpleName())
//    {
//      case "Account":
//        empty = "No accounts found!";
//        title = user == null
//                ? "Accounts"
//                : user.displayName() + " • " + "Accounts";
//        break;
//      case "Bank":
//        empty = "No banks found!";
//        title = user == null ? "Banks" : user.displayName() + " • " + "Banks";
//        break;
//      case "Transaction":
//        empty = "No transactions found!";
//        title = user == null
//                ? "Transactions"
//                : user.displayName() + " • " + "Transactions";
//        break;
//      default:
//        empty = "Nothing found!";
//        title = user == null ? "" : user.displayName();
//    }

  //  protected ContainerTag row(Id data, User user, Class<? extends Id> type)
//  {
//    assert data != null;
//
//    ContainerTag row = tr().with(td().with(bankDetailsLink(data.id())),
//      td().with(link(data.id(), user, type)));
//
//    data.fields().stream().map(f -> valueOf(data, f))
//      .forEach(value -> row.with(td(value != null ? value.toString() : "")));
//
//    return row;
//  }

  protected final String accountsUrl;
  protected final String bankDetailUrl;
  protected final String banksUrl;
  protected final String transactionsUrl;
  protected final String url;
  protected final String userUrl;

  protected final ContainerTag banksLink;
  protected final ContainerTag bottom;

  protected final List<User> users;
}
