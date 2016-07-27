/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.util;

/**
 * To be done. {@code throw tbd();} is set as Intellij's default for new
 * messages with a return value.
 *
 * @author ub@kassapo.com
 * @since 2016.0
 */
public class tbd extends RuntimeException
{
  public tbd()
  {
    super("Not implemented yet!");
  }

  public tbd(String message)
  {
    super("Not implemented yet: " + message);
  }

  public tbd(String message, Throwable cause)
  {
    super("Not implemented yet: " + message, cause);
  }

  public tbd(Throwable cause)
  {
    super("Not implemented yet!", cause);
  }

  protected tbd(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace)
  {
    super("Not implemented yet: " + message, cause, enableSuppression,
      writableStackTrace);
  }
}
