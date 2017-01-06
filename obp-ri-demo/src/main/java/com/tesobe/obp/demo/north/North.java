/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.north;

import com.tesobe.obp.kafka.Configuration;
import com.tesobe.obp.kafka.SimpleConfiguration;
import com.tesobe.obp.kafka.SimpleNorth;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.nov2016.Account;
import com.tesobe.obp.transport.nov2016.AccountReader;
import com.tesobe.obp.transport.nov2016.Bank;
import com.tesobe.obp.transport.nov2016.BankReader;
import com.tesobe.obp.transport.nov2016.User;
import com.tesobe.obp.transport.nov2016.UserReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class North
{
  public North(String consumerProps, String consumerTopic, String producerProps,
    String producerTopic) throws IOException
  {
    log.info("Starting TESOBE's OBP kafka north demo...");

    Configuration cfg = new SimpleConfiguration(this, consumerProps,
      consumerTopic, producerProps, producerTopic);

    connector = Transport.defaultFactory().connector(new SimpleNorth(cfg));
    transport = connector.sender();

    transport.receive();
  }

  /**
   * Alternative transport.
   *
   * @param c connector
   */
  public North(Connector c)
  {
    connector = c;
    transport = c.sender();
  }

  public List<Bank> getBanks() throws Exception
  {
    Decoder.Response response = connector.get("getBanks",
      Transport.Target.banks, null);

    return response.error()
      .map(e ->
      {
        log.error(e.message());

        return Collections.<Bank>emptyList();
      })
      .orElseGet(() -> response.data()
        .stream()
        .map(BankReader::new)
        .map(Bank.class::cast)
        .collect(toList()));
  }

  public List<User> getUsers() throws Exception
  {
    Decoder.Response response = connector.get("getUsers",
      Transport.Target.users, null);

    return response.error()
      .map(e ->
      {
        log.error(e.message());

        return Collections.<User>emptyList();
      })
      .orElseGet(() -> response.data()
        .stream()
        .map(UserReader::new)
        .map(User.class::cast)
        .collect(toList()));
  }

  public List<Account> getAccounts(Bank b, User u) throws Exception
  {
    HashMap<String, Object> parameters = new HashMap<>();

    parameters.put("bankId", b.bankId());
    parameters.put("userId", u.id());

    Decoder.Response response = connector.get("getAccounts",
      Transport.Target.accounts, parameters);

    return response.error()
      .map(e ->
      {
        log.error(e.message());

        return Collections.<Account>emptyList();
      })
      .orElseGet(() -> response.data()
        .stream()
        .map(AccountReader::new)
        .map(Account.class::cast)
        .collect(toList()));
  }

  public void shutdown()
  {
    transport.shutdown();
  }

  final static Logger log = LoggerFactory.getLogger(North.class);
  final Connector connector;
  final Sender transport;
}
