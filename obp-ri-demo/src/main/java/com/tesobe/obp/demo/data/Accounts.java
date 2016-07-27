// Copyright
package com.tesobe.obp.demo.data;

import com.tesobe.obp.transport.Connector;

import java.util.*;

/**
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class Accounts
{
  /**
   * All accounts
   */
  public static final List<Connector.Account> ALL_ACCOUNTS;
  /**
   * Owner -> account
   */
  public static final Map<String, Connector.Account> PRIVATE_ACCOUNTS;
  /**
   * Public accounts
   */
  public static final List<Connector.Account> PUBLIC_ACCOUNTS;
  /**
   * {@link Users#charles}'s account at {@link Banks#CRESGIGI}.
   */
  public static final Connector.Account charles_CRESGIGI_1
    = new Connector.Account("CRESGIGI-1", "label", "CRESGIGI", "number", "type",
    "EUR", "amount", "iban");

  static
  {
    ArrayList<Connector.Account> accounts = new ArrayList<>();
    HashMap<String, Connector.Account> owners = new HashMap<>();

    accounts.add(charles_CRESGIGI_1);
    owners.put(Users.charles, charles_CRESGIGI_1);

    ALL_ACCOUNTS = Collections.unmodifiableList(accounts);
    PRIVATE_ACCOUNTS = Collections.unmodifiableMap(owners);
    PUBLIC_ACCOUNTS = ALL_ACCOUNTS;
  }
}
