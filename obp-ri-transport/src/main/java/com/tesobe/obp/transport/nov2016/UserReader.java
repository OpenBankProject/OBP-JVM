/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import com.tesobe.obp.transport.Data;

/**
 * Convert data to an user.
 */
public class UserReader implements User
{
  public UserReader(Data user)
  {
    data = user;
  }

  @Override public String id()
  {
    return data.text(id);
  }

  @Override public String displayName()
  {
    return data.text(displayName);
  }

  @Override public String email()
  {
    return data.text(email);
  }

  @Override public String password()
  {
    return data.text(password);
  }

  protected final Data data;
}
