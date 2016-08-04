/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.util;

import java.util.Optional;

@SuppressWarnings("WeakerAccess") public final class Strings
{
  public static boolean isWhite(String s)
  {
    if(s != null)
    {
      for(int i = 0; i < s.length(); ++i) // alternative to trim().isEmpty()
      {
        if(!Character.isWhitespace(s.codePointAt(i))) // better than charAt?
        {
          return false;
        }
      }
    }

    return true;
  }

  public static boolean nonWhite(String s)
  {
    return !isWhite(s);
  }

  public static Optional<String> white(String s)
  {
    return nonWhite(s) ? Optional.of(s) : Optional.empty();
  }
}
