// Copyright
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  public AccountWrapper getAccount(String bankId, String accountId,
                                      OutboundContext outboundContext) throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccount(outboundContext, bankId, accountId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new AccountWrapper(decoder.account(response), decoder.inboundContext(response));
  }

  @Override public AccountsWrapper getAccounts(String bankId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getAccounts(outboundContext, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new AccountsWrapper(decoder.accounts(response), decoder.inboundContext(response));
  }

  @Override public BankWrapper getBank(String bankId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBank(outboundContext, bankId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new BankWrapper(decoder.bank(response), decoder.inboundContext(response));
  }


  @Override public BanksWrapper getBanks(OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getBanks(outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new BanksWrapper(decoder.banks(response), decoder.inboundContext(response));
  }

  @Override public TransactionWrapper getTransaction(String bankId,
                                                        String accountId, String transactionId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getTransaction(bankId, accountId, transactionId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new TransactionWrapper(decoder.transaction(response), decoder.inboundContext(response));
  }

  @Override public TransactionsWrapper getTransactions(String bankId,
                                                         String accountId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getTransactions(bankId, accountId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new TransactionsWrapper(decoder.transactions(response), decoder.inboundContext(response));
  }

  @Override public UserWrapper getUser(String userId, OutboundContext outboundContext)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getUser(userId, outboundContext).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return new UserWrapper(decoder.user(response), decoder.inboundContext(response));
  }

  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  protected static final Logger log = LoggerFactory
    .getLogger(Connector.class);
}
