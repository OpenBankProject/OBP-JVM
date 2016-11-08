/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class DemoUser implements User, Open
{
  public DemoUser(String id, String displayName, String email, String password,
    boolean open)
  {
    this.id = id;
    this.displayName = displayName;
    this.email = email;
    this.password = password;
    this.open = open;
  }

  @Override public String id()
  {
    return id;
  }

  @Override public String displayName()
  {
    return displayName;
  }

  @Override public String email()
  {
    return email;
  }

  @Override public String password()
  {
    return password;
  }

  @Override public boolean isPublic()
  {
    return open;
  }

  @Override public String toString()
  {
    return id + ":" + displayName;
  }

  final String id;
  final String displayName;
  final String email;
  final String password;
  final boolean open;
  final List<Account> accounts = new ArrayList<>();
}
