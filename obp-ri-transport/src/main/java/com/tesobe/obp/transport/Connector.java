/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport;

@SuppressWarnings("WeakerAccess") public interface Connector
{
  Iterable<Bank> getPublicBanks() throws InterruptedException;
  Iterable<Bank> getPrivateBanks(String userId) throws InterruptedException;

  public class Bank
  {
    public Bank(String id, String name)
    {
      this.id = id;
      this.name = name;
    }

    @Override public String toString()
    {
      return "Bank{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }

    public final String id;
    public final String name;
  }
}
/*
   api.put("getBank", request -> getBank(valueOf(request), getJSONObject(request, "getBank")));
    api.put("getBankAccount", request -> getBankAccount(valueOf(request), getJSONObject(request, "getBankAccount")));
    api.put("getBankAccounts", request -> getBankAccounts(valueOf(request), getJSONObject(request, "getBankAccounts")));
    api.put("getBanks", request -> getBanks(valueOf(request), getJSONObject(request, "getBanks")));
    api.put("getTransaction", request -> getTransaction(valueOf(request), getJSONObject(request, "getTransaction")));
    api.put("getTransactions", request -> getTransactions(valueOf(request), getJSONObject(request, "getTransactions")));
    api.put("getUser", request -> getUser(valueOf(request), getJSONObject(request, "getUser")));
    api.put("getUserAccounts", request -> getUserAccounts(valueOf(request), getJSONObject(request, "getUserAccounts")));
    api.put("getPublicAccounts",
      request -> getPublicAccounts(valueOf(request), getJSONObject(request, "getPublicAccounts")));
 */
