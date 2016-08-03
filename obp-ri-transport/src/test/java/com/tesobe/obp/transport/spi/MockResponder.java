/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Responses for the test cases.
 */
@SuppressWarnings("WeakerAccess") public class MockResponder
  extends LegacyResponder
{
  public MockResponder(Decoder d, Encoder e)
  {
    super(d, e);
  }

  @Override
  protected String getPrivateAccount(Decoder.Request request, Encoder e)
  {
    assertThat(request.userId(), notNullValue());
//    assertThat(request.accountId(), notNullValue());
//    assertThat(request.bankId(), notNullValue());

    return e.account(charles_CRESGIGI_1);
  }


  @Override
  protected String getPrivateBanks(String packet, Decoder.Request request,
    Encoder e)
  {
    assertThat(request.userId(), notNullValue());

    List<Connector.Bank> banks = PRIVATE_BANKS.get(request.userId());

    return banks == null ? e.banks() : e.banks(banks);
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    return e.banks(ALL_BANKS);
  }

  public static final Connector.Account charles_CRESGIGI_1
    = new Connector.Account("CRESGIGI-1", "label", "CRESGIGI", "number", "type",
    "EUR", "amount", "iban");
  public static final Connector.Bank AAIAATW1 = new Connector.Bank("AAIAATW1",
    "AIBC ANGLO IRISH BANK (AUSTRIA) KAG",
    "AIBC ANGLO IRISH BANK (AUSTRIA) KAG", "logo", "website");
  public static final Connector.Bank CRESGIGI = new Connector.Bank("CRESGIGI",
    "CREDIT SUISSE (GIBRALTAR) LTD", "CREDIT SUISSE (GIBRALTAR) LTD", "logo",
    "website");
  public static final List<Connector.Bank> ALL_BANKS;
  public static final Map<String, List<Connector.Bank>> PRIVATE_BANKS;
  public static final List<Connector.Bank> PUBLIC_BANKS;

  public static final String charles = "charles.swann@example.org";
  public static final String odette = "Odette de Crécy";
  public static final String hacker = "J. Random Hacker";

  static
  {
    List<Connector.Bank> charlesBanks = new ArrayList<>();
    Map<String, List<Connector.Bank>> privateBanks = new HashMap<>();

    charlesBanks.add(CRESGIGI);
    privateBanks.put(MockResponder.charles, unmodifiableList(charlesBanks));

    ALL_BANKS =  unmodifiableList(asList(AAIAATW1, CRESGIGI));
    PRIVATE_BANKS = unmodifiableMap(privateBanks);
    PUBLIC_BANKS = ALL_BANKS;
  }
}
