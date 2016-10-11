/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Id;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.util.Utils;
import j2html.TagCreator;
import j2html.tags.ContainerTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h5;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.p;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;
import static java.lang.String.format;
import static java.util.stream.StreamSupport.stream;

@SuppressWarnings("WeakerAccess") public class Html
{
  public Html(String ip, int port)
  {
    url = format("http://%s:%s/obp/demo", ip, port);

    accountsUrl = "/bank/%s/accounts";
    bankDetailUrl = "/bank/%s";
    banksUrl = "/banks";
    banksLink = a("Banks").withHref(format(url, banksUrl)).withAlt(banksUrl);
    transactionsUrl = "/bank/%s/account/%s/transactions";
    userUrl = "/user/%s%s";

    bottom = div().withClass("clear");
  }

  protected ContainerTag breadcrumb(String userId, String item,
    String accountId, String transactionId, Class<? extends Id> type,
    boolean isList, List<User> users)
  {
    assert type != null;

    StringBuilder title = new StringBuilder();
    Optional<User> user = userId == null
      ? Optional.empty()
      : users.stream().filter(u -> userId.equals(u.id())).findFirst();

    String name = typeName(type, isList);

    title.append(name);

    user.map(u ->
    {
      title.append(" • ");
      title.append(u.displayName());
      return Void.TYPE;
    });

    for(String i : new String[]{item, accountId, transactionId})
    {
      if(i != null)
      {
        title.append(" • ");
        title.append(i);
      }
    }

    return h5(title.toString());
  }

  protected Optional<ContainerTag> details(String userId, String bankId,
    String accountId, String transactionId, Class<? extends Id> type,
    String parentId)
  {
    assert type != null;

    String name = typeName(type, false);
    String href = Utils
      .map(replace(parentId, type, userId, bankId, accountId, transactionId),
        Stream.of("user", "bank", "account", "transaction"),
        (id, path) -> id != null ? "/" + path + "/" + id : null)
      .filter(Objects::nonNull)
      .collect(() -> new StringBuilder(url), StringBuilder::append,
        StringBuilder::append)
      .toString();

    return Optional.of(a(name).withAlt(href).withHref(href));
  }

  protected Optional<ContainerTag> drillDown(String userId, String bankId,
    String accountId, String transactionId, Class<? extends Id> type,
    String parentId)
  {
    assert type != null;

    return drillDown(type).map(s ->
    {
      String name = typeName(s, true);
      String href = Utils
        .map(replace(parentId, type, userId, bankId, accountId, transactionId),
          Stream.of("user", "bank", "account", "transaction"),
          (id, path) -> id != null ? "/" + path + "/" + id : null)
        .filter(Objects::nonNull)
        .collect(() -> new StringBuilder(url), StringBuilder::append,
          StringBuilder::append)
        .append('/')
        .append(name.toLowerCase())
        .toString();

      return a(name).withAlt(href).withHref(href);
    });
  }

  protected Stream<String> replace(String parentId, Class<? extends Id> type,
    String userId, String bankId, String accountId, String transactionId)
  {
    assert type != null;

    if(type.equals(Account.class))
    {
      return Stream.of(userId, bankId, parentId, transactionId);
    }
    else if(type.equals(Bank.class))
    {
      return Stream.of(userId, parentId, accountId, transactionId);
    }
    else if(type.equals(Transaction.class))
    {
      return Stream.of(userId, bankId, accountId, parentId);
    }
    else
    {
      return Stream.empty();
    }
  }

  protected Optional<Class<? extends Id>> drillDown(Class<? extends Id> type)
  {
    if(type == Account.class)
    {
      return Optional.of(Transaction.class);
    }
    else if(type == Bank.class)
    {
      return Optional.of(Account.class);
    }
    else
    {
      return Optional.empty();
    }
  }

  protected String typeName(Class<? extends Id> type, boolean isList)
  {
    String name = "";

    if(type != null)
    {
      switch(type.getSimpleName())
      {
        case "Account":
          name = isList ? "Accounts" : "Account";
          break;
        case "Bank":
          name = isList ? "Banks" : "Bank";
          break;
        case "Transaction":
          name = isList ? "Transactions" : "Transaction";
          break;
        case "User":
          name = isList ? "Users" : "User";
          break;
        default:
          name = type.getSimpleName();
      }
    }

    return name;
  }

  public String error(Exception e)
  {
    assert e != null;

    return html()
      .with(head().with(title("OBP Demo"),
        body().with(h1("OBP Demo Error").withText(e.getMessage()))))
      .render();
  }

  protected ContainerTag headElement()
  {
    return head().with(title("OBP Demo"), link()
        .withHref("https://fonts.googleapis.com/css?family=Open+Sans")
        .withRel("stylesheet"),
      link().withHref("/demo.css").withRel("stylesheet"));
  }

  public String render(String userId, String bankId, String accountId,
    String transactionId, Id datum, Class<? extends Id> type, List<User> users)
  {
    ContainerTag left = div().withId("left").with(h5("Users"));
    ContainerTag right = div().withId("right");
    ContainerTag breadcrumb = breadcrumb(userId, bankId, accountId,
      transactionId, type, false, users);

    users(left, users);
    right.with(breadcrumb);

    if(datum == null)
    {
      if(type != null)
      {
        right.with(p("Nothing found."));
      }
    }
    else
    {
      ContainerTag headers = tr().with(th("Details")).with(th("Drill Down"));
      ContainerTag table = table().with(headers);
      ContainerTag row = tr();

      datum.fields().forEach(f -> headers.with(th(localize(f))));

      row.with(
        details(userId, bankId, accountId, transactionId, type, datum.id())
          .map(link -> td().with(link))
          .orElseGet(TagCreator::td));
      row.with(
        drillDown(userId, bankId, accountId, transactionId, type, datum.id())
          .map(link -> td().with(link))
          .orElseGet(TagCreator::td));

      // data fields are interface methods
      datum
        .fields()
        .stream()
        .map(f -> Utils.invoke(datum, f))
        .map(value -> td(value != null ? value.toString() : ""))
        .collect(Collectors.toList())
        .forEach(row::with);

      right.with(table.with(row));
    }

    return html()
      .with(headElement(), body().withId("container").with(left, right, bottom))
      .render();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public String render(String userId, String bankId, String accountId,
    String transactionId, List<Id> data, Class<? extends Id> type,
    List<User> users)
  {
    assert type != null;

    ContainerTag left = div().withId("left").with(h5("Users"));
    ContainerTag right = div().withId("right");
    ContainerTag breadcrumb = breadcrumb(userId, bankId, accountId,
      transactionId, type, true, users);

    users(left, users);
    right.with(breadcrumb);

    List<Id> rows = data == null
      ? null
      : stream(data.spliterator(), false)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    if(rows == null || rows.isEmpty())
    {
      right.with(p("Nothing found."));
    }
    else
    {
      ContainerTag headers = tr().with(th("Details")).with(th("Drill Down"));
      ContainerTag table = table().with(headers);

      rows.get(0).fields().forEach(f -> headers.with(th(localize(f))));
      rows.forEach(datum ->
      {
        ContainerTag row = tr();

        row.with(
          details(userId, bankId, accountId, transactionId, type, datum.id())
            .map(link -> td().with(link))
            .orElse(td()));
        row.with(
          drillDown(userId, bankId, accountId, transactionId, type, datum.id())
            .map(link -> td().with(link))
            .orElse(td()));

        // data fields are interface methods
        datum
          .fields()
          .stream()
          .map(f -> Utils.invoke(datum, f))
          .map(value -> td(value != null ? value.toString() : ""))
          .collect(Collectors.toList())
          .forEach(row::with);

        table.with(row);
      });

      right.with(table);
    }

    return html()
      .with(headElement(), body().withId("container").with(left, right, bottom))
      .render();
  }

  protected String localize(String name)
  {
    String s = LOCALIZED.get(name);

    return s == null ? name : s;
  }

  protected void users(ContainerTag accumulator, List<User> users)
  {
    if(users != null)
    {
      users.forEach(u ->
      {
        String href = u.id() == null
          ? url + banksUrl
          : url + format(userUrl, u.id(), banksUrl);

        accumulator.with(div().with(a(u.displayName()).withHref(href)));
      });
    }
  }

  protected final String accountsUrl;
  protected final String bankDetailUrl;
  protected final String banksUrl;
  protected final String transactionsUrl;
  protected final String url;
  protected final String userUrl;

  protected final ContainerTag banksLink;
  protected final ContainerTag bottom;

  protected static final Map<String, String> LOCALIZED = new HashMap<>();

  static
  {
    // Account: id amount	bank	currency	iban	label	number	type
    // Bank: id	fullName	logo	shortName	url
    // Transaction: account	balance	bank	completed	description	otherAccount
    //   otherId	posted type	value
    LOCALIZED.put("account", "Account");
    LOCALIZED.put("amount", "Balance");
    LOCALIZED.put("balance", "Balance");
    LOCALIZED.put("bank", "Bank");
    LOCALIZED.put("completed", "Completed");
    LOCALIZED.put("currency", "Currency");
    LOCALIZED.put("iban", "IBAN");
    LOCALIZED.put("id", "Id");
    LOCALIZED.put("fullName", "Full Name");
    LOCALIZED.put("label", "Label");
    LOCALIZED.put("logo", "Logo");
    LOCALIZED.put("number", "Number");
    LOCALIZED.put("otherAccount", "Counterparty Account Number");
    LOCALIZED.put("otherId", "Counterparty Name");
    LOCALIZED.put("posted", "Posted");
    LOCALIZED.put("shortName", "Short Name");
    LOCALIZED.put("type", "Type");
    LOCALIZED.put("url", "Website");
    LOCALIZED.put("value", "Value");
  }
}
