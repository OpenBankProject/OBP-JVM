// Copyright
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Compatible to mid 2016 OBP-API.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class LegacyConnector
  implements Connector
{
  public LegacyConnector(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  @Override
  public Optional<Account> getPrivateAccount(String userId, String bankId,
    String accountId) throws InterruptedException
  {
    String request = encoder.getPrivateAccount(userId, bankId, accountId)
      .toString();
    String response = sender.send(new Message("id", request));

    log.trace("{} {}", request, response);

    return decoder.account(response);
  }

  @Override public Iterable<Bank> getPublicBanks() throws InterruptedException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getPublicBanks().toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.banks(response);
  }

  @Override public Iterable<Bank> getPrivateBanks(String userId)
    throws InterruptedException
  {
    if(isNull(userId))
    {
      return decoder.banks();
    }

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

  protected static final Logger log = LoggerFactory
    .getLogger(LegacyConnector.class);
}
