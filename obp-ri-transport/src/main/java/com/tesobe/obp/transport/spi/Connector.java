// Copyright
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

/**
 * Compatible to mid 2016 OBP-API.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public class Connector
  implements com.tesobe.obp.transport.Connector
{
  public Connector(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    decoder = d;
    encoder = e;
    sender = s;
    version = v;
  }

  @Override
  public Optional<Account> getAccount(String bankId, String accountId,
                                      OutboundContext outboundContext) throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccount(outboundContext, bankId, accountId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.account(response);
  }

  @Override
  public Iterable<Account> getAccounts(String bankId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccounts(outboundContext, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.accounts(response);
  }

  @Override public Optional<Bank> getBank(String bankId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBank(outboundContext, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.bank(response);
  }


  @Override public Iterable<Bank> getBanks(OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBanks(outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.banks(response);
  }

  @Override public Optional<Transaction> getTransaction(String bankId,
                                                        String accountId, String transactionId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getTransaction(bankId, accountId, transactionId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.transaction(response);
  }

  @Override public Iterable<Transaction> getTransactions(String bankId,
                                                         String accountId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getTransactions(bankId, accountId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.transactions(response);
  }

  @Override public Optional<User> getUser(String userId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getUser(userId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.user(response);
  }

  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  protected static final Logger log = LoggerFactory
    .getLogger(Connector.class);
}
