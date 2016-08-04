// Copyright
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import com.tesobe.obp.util.tbd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

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
  public Optional<Account> getPrivateAccount(String bankId, String accountId,
    String userId) throws InterruptedException, DecoderException
  {
    String request = encoder.getPrivateAccount(userId, bankId, accountId)
      .toString();
    String response = sender.send(new Message("id", request));

    log.trace("{} {}", request, response);

    return decoder.account(response);
  }

  @Override
  public Iterable<Account> getPrivateAccounts(String bankId, String userId)
    throws InterruptedException, DecoderException
  {
    String request = encoder.getPrivateAccounts(userId, bankId).toString();
    String response = sender.send(new Message("id", request));

    log.trace("{} {}", request, response);

    return decoder.accounts(response);
  }

  @Override public Optional<Bank> getPrivateBank(String bankId, String userId)
    throws InterruptedException, DecoderException
  {
    String request = encoder.getPrivateBank(userId, bankId).toString();
    String response = sender.send(new Message("id", request));

    log.trace("{} {}", request, response);

    return decoder.bank(response);
  }


  @Override public Iterable<Bank> getPrivateBanks(String userId)
    throws InterruptedException, DecoderException
  {
    String id = UUID.randomUUID().toString();
    String request = encoder.getPrivateBanks(userId).toString();
    String response = sender.send(new Message(id, request));

    log.trace("{} {}", request, response);

    return decoder.banks(response);
  }

  @Override public Optional<Transaction> getPrivateTransaction(String bankId,
    String accountId, String userId)
    throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override public Iterable<Transaction> getPrivateTransactions(String bankId,
    String accountId, String userId)
    throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override
  public Optional<Account> getPublicAccount(String bankId, String accountId)
    throws InterruptedException, DecoderException
  {
//    String request = encoder.getPublicAccount1(bankId, accountId)
//      .toString();
//    String response = sender.send(new Message("id", request));
//
//    log.trace("{} {}", request, response);
//
//    return decoder.account1(response);
    throw new tbd();
  }

  @Override
  public Iterable<Account> getPublicAccounts(String bankId, String accountId)
    throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override public Optional<Bank> getPublicBank() throws InterruptedException
  {
    throw new tbd();
  }

  @Override public Iterable<Bank> getPublicBanks()
    throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override public Optional<Transaction> getPublicTransaction(String bankId,
    String accountId) throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override public Iterable<Transaction> getPublicTransactions(String bankId,
    String accountId) throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  @Override
  public Optional<User> getUser(String bankId, String accountId, String userId)
    throws InterruptedException, DecoderException
  {
    throw new tbd();
  }

  protected final Transport.Version version;
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Sender sender;

  protected static final Logger log = LoggerFactory
    .getLogger(LegacyConnector.class);
}
