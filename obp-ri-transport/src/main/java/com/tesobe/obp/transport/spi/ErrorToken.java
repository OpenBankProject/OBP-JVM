/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Token;

import java.util.Optional;

class ErrorToken implements Token
{
  ErrorToken(String message)
  {
    this.message = message;
  }

  @Override public Optional<String> id()
  {
    return Optional.empty();
  }

  @Override public String error()
  {
    return message;
  }

  final String message;
}
