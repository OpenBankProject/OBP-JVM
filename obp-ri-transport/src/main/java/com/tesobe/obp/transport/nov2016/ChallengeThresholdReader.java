/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import com.tesobe.obp.transport.Data;

/**
 * Read the {@link ChallengeThreshold} from the {@link Data}.
 */
public class ChallengeThresholdReader implements ChallengeThreshold
{
  public ChallengeThresholdReader(Data bank)
  {
    data = bank;
  }

  @Override public String amount()
  {
    return data.text(amount);
  }

  @Override public String currency()
  {
    return data.text(currency);
  }

  protected final Data data;
}
