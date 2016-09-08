/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.util.tbd;

public class DecoderSep1026 extends Decoder
{
  public DecoderSep1026(Transport.Version v)
  {
    super(v);
  }

  @Override public Request request(String request)
  {
    throw new tbd();
  }
}
