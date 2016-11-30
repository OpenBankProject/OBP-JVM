/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

/**
 * Result of a put.
 *
 * @since 2016.11
 */
public interface Token
{
  default Optional<String> id()
  {
    return Optional.empty();
  }

  default String error()
  {
    return "";
  }

  default boolean isValid()
  {
    return id().isPresent();
  }
}
