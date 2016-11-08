/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Arrays.asList;

/**
 */
public interface Transaction extends Id
{
  String id();

  String account();

  String bank();

  String otherId();

  String otherAccount();

  String type();

  String description();

  ZonedDateTime posted();

  ZonedDateTime completed();

  String balance();

  String value();

  default List<String> fields()
  {
    return FIELDS;
  }

  List<String> FIELDS = asList("id", "account", "balance", "bank", "completed",
    "description", "otherAccount", "otherId", "posted", "type", "value");
}
