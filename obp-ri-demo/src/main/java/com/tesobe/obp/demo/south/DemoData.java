/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.transport.spi.LegacyResponder;
import com.tesobe.obp.util.tbd;

public class DemoData extends LegacyResponder
{
  public DemoData(Decoder d, Encoder e)
  {
    super(d, e);
  }

  @Override protected String getPrivateAccount(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateBank(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateBanks(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPrivateUser(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPublicAccount(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override protected String getPublicAccounts(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPublicBank(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPublicTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPublicTransactions(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String getPublicUser(String packet, Decoder.Request r, Encoder e)
  {
    throw new tbd();
  }

  @Override
  protected String savePrivateTransaction(String packet, Decoder.Request r,
    Encoder e)
  {
    throw new tbd();
  }
}
