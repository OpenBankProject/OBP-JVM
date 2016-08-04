/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.spi;

public class DecoderException extends RuntimeException
{
  public DecoderException()
  {
  }

  public DecoderException(String message)
  {
    super(message);
  }

  public DecoderException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public DecoderException(Throwable cause)
  {
    super(cause);
  }

  public DecoderException(String message, Throwable cause,
    boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
