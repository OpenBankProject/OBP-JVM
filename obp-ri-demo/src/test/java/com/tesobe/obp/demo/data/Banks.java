/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.demo.data;

import com.tesobe.obp.transport.Connector;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@SuppressWarnings("WeakerAccess") public class Banks
{
  public static final Connector.Bank AAIAATW1 = new Connector.Bank("AAIAATW1",
    "AIBC ANGLO IRISH BANK (AUSTRIA) KAG");
  public static final Connector.Bank CRESGIGI = new Connector.Bank("CRESGIGI",
    "CREDIT SUISSE (GIBRALTAR) LTD");
  public static final List<Connector.Bank> ALL_BANKS = Collections
    .unmodifiableList(asList(AAIAATW1, CRESGIGI));
  public static final Map<String, Connector.Bank> PRIVATE_BANKS;

  static
  {
    Map<String, Connector.Bank> privateBanks = new HashMap<>();

    privateBanks.put("charles.swann@example.org", CRESGIGI);

    PRIVATE_BANKS = Collections.unmodifiableMap(privateBanks);
  }
}
