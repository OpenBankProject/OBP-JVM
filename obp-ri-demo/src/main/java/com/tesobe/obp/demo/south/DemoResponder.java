// Copyright
package com.tesobe.obp.demo.south;

import com.tesobe.obp.demo.data.Banks;
import com.tesobe.obp.transport.spi.*;

import static com.tesobe.obp.demo.data.Accounts.charles_CRESGIGI_1;
import static com.tesobe.obp.demo.data.Banks.CRESGIGI;
import static com.tesobe.obp.demo.data.Users.charles;

/**
 * @since 2016.0
 */
class DemoResponder extends LegacyResponder
{
  DemoResponder(Decoder d, Encoder e)
  {
    super(d, e);
  }

  @Override
  protected String getPrivateAccount(Decoder.Request request, Encoder e)
  {
    return request.userId().equals(charles)
           ? e.account(charles_CRESGIGI_1) : e.account();
  }

  @Override
  protected String getPrivateBanks(String packet, Decoder.Request request,
    Encoder e)
  {
    return request.userId().equals(charles) ? e.banks(CRESGIGI) : e.banks();
  }

  @Override protected String getPublicBanks(String packet, Encoder e)
  {
    return e.banks(Banks.PUBLIC_BANKS);
  }
}
