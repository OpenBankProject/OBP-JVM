/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Bank;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class DemoBank
  implements Bank, Accessible, Open
{
  public DemoBank(String id, String shortName, String fullName, String logo,
    String url, boolean open)
  {
    this.id = id;
    this.shortName = shortName;
    this.fullName = fullName;
    this.logo = logo;
    this.url = url;
    this.open = open;
  }


  @Override public String id()
  {
    return id;
  }

  @Override public String shortName()
  {
    return shortName;
  }

  @Override public String fullName()
  {
    return fullName;
  }

  @Override public String logo()
  {
    return logo;
  }

  @Override public String url()
  {
    return url;
  }

  @Override public boolean isPublic()
  {
    return open;
  }

  @Override public String toString()
  {
    return id + ":" + shortName;
  }

  @Override public List<DemoUser> customers()
  {
    return customers;
  }

  final String id;
  final String shortName;
  final String fullName;
  final String logo;
  final String url;
  final boolean open;
  final List<DemoAccount> accounts = new ArrayList<>();
  final List<DemoUser> customers = new ArrayList<>();
}
