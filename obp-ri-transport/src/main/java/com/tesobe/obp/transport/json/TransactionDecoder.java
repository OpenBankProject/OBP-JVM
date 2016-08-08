/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.util.tbd;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess") class TransactionDecoder implements
  Transaction
{
  public TransactionDecoder(JSONObject transaction)
  {
    this.transaction = transaction;
  }

  @Override public String id()
  {
    return transaction.optString("id", null);
  }

  @Override public String account()
  {
    throw new tbd();
  }

  @Override public String bank()
  {
    throw new tbd();
  }

  @Override public String name()
  {
    throw new tbd();
  }

  @Override public String account_number()
  {
    throw new tbd();
  }

  @Override public String type()
  {
    throw new tbd();
  }

  @Override public String description()
  {
    throw new tbd();
  }

  @Override public String posted()
  {
    throw new tbd();
  }

  @Override public String completed()
  {
    throw new tbd();
  }

  @Override public String new_balance()
  {
    throw new tbd();
  }

  @Override public String value()
  {
    throw new tbd();
  }

  @Override public String toString()
  {
    return transaction.toString();
  }

  private final JSONObject transaction;
}
