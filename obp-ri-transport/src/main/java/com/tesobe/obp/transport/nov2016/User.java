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
 * @since 2016.11
 */
public interface User extends Id
{
  String id();

  String displayName();

  String email();

  String password();

  default List<String> fields()
  {
    return FIELDS;
  }

  String id = "id";
  String displayName = "displayName";
  String email = "email";
  String password = "password";

  List<String> FIELDS = asList(id, displayName, email, password);
}
