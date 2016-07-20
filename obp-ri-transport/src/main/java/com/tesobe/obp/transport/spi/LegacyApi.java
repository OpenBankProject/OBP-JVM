// Copyright
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@SuppressWarnings("WeakerAccess") public class LegacyApi implements Connector
{
  public LegacyApi(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  @Override public Iterable<Bank> getPublicBanks() throws InterruptedException
  {
    String request = encoder.getPublicBanks().toString();
    String response = sender.send(new Message("id", request));

    log.trace("{} {}", request, response);

    return decoder.banks(response);
  }

  @Override public Iterable<Bank> getPrivateBanks(String userId)
    throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getPrivateBanks(userId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.banks(response);
  }

  protected final Transport.Version version;
  protected final Encoder encoder;
  protected final Decoder decoder;
  protected final Sender sender;

  protected static final Logger log = LoggerFactory.getLogger(LegacyApi.class);
}
