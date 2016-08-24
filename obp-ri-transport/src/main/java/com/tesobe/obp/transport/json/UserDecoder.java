/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.User;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess") class UserDecoder implements User
{
  public UserDecoder(JSONObject user)
  {
    json = user;
  }

  @Override public String displayName()
  {
    return json.optString("name", null);
  }

  @Override public String email()
  {
    return json.optString("email", null);
  }

  @Override public String password()
  {
    return json.optString("password", null);
  }

  @Override public String toString()
  {
    return json.toString();
  }

  private final JSONObject json;
}
