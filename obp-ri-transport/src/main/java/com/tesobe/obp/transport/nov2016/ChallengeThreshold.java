/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * The fields and parameters used to request a challenge threshold.
 */
public interface ChallengeThreshold
{
  String amount();

  String currency();

  default List<String> fields()
  {
    return FIELDS;
  }

  String amount = "amount";
  String currency = "currency";

  List<String> FIELDS = asList(amount, currency);

  interface Parameters
  {
    String accountId = com.tesobe.obp.transport.nov2016.Parameters.accountId;
    String userId = com.tesobe.obp.transport.nov2016.Parameters.userId;
    String type = "type";
    String currency = "currency";
  }
}
