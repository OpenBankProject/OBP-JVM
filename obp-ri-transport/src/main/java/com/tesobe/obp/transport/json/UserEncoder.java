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

@SuppressWarnings("WeakerAccess") class UserEncoder
{
  public UserEncoder(User u)
  {
    user = u;
  }

  public JSONObject toJson()
  {
    // @formatter:off
    @SuppressWarnings("UnnecessaryLocalVariable")
    JSONObject json = new JSONObject()
      .put("name", user.displayName())
      .put("email", user.email())
      .put("password", user.password());
    // @formatter:on

    return json;
  }

  private final User user;
}
