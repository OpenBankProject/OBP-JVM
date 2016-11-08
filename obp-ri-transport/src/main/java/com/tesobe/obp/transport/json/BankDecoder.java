/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Bank;
import org.json.JSONObject;

/**
 * Todo error handling
 */
class BankDecoder implements Bank
{
  BankDecoder(JSONObject bank)
  {
    assert bank != null;

    json = bank;
  }

  @Override public String id()
  {
    return json.optString("id", null);
  }

  @Override public String shortName()
  {
    return json.optString("short", null);
  }

  @Override public String fullName()
  {
    return json.optString("name", null);
  }

  @Override public String logo()
  {
    return json.optString("logo", null);
  }

  @Override public String url()
  {
    return json.optString("url", null);
  }

  @Override public String toString()
  {
    return json.toString();
  }

  private final JSONObject json;
}
