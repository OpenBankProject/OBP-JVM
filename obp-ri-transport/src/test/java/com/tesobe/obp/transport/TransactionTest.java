/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import org.junit.Test;

import java.util.ArrayList;

public class TransactionTest
{
  @Test public void fields() throws Exception
  {
    ArrayList<String> sorted = new ArrayList<>(Transaction.FIELDS);

    sorted.sort(null);
    sorted.forEach(f -> System.out.println("\"" + f + "\","));
  }

}
