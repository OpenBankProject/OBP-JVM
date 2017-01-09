/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.json.DecoderNov2016;
import com.tesobe.obp.transport.json.EncoderNov2016;
import com.tesobe.obp.transport.spi.ConnectorNov2016;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.util.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.Nov2016;
import static java.util.function.Function.identity;

/**
 * Transport manages the different versions of the transport api.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public abstract class Transport
{
  /**
   * Uses {@link Version#Nov2016}, {@link Encoding#json}.
   *
   * @return a factory that is always available
   */
  public static Factory defaultFactory()
  {
    //noinspection OptionalGetWithoutIsPresent
    return factory(Version.Nov2016, Encoding.json).get();
  }

  /**
   * Try to provide a factory for a version / encoding combination.
   *
   * @param v the API / SPI version
   * @param e the encoding used by the transport layer
   *
   * @return empty if the version / encoding combination is not available
   */
  public static Optional<Transport.Factory> factory(Version v, Encoding e)
  {
    return Optional.of(new Factory()
    {
      @Override public Connector connector(Sender s)
      {
        if(s == null)
        {
          throw new IllegalArgumentException("Sender is required!");
        }

        return Transport.connector(v, encoder(), decoder(), s);
      }

      @Override public Decoder decoder()
      {
        return Transport.decoder(v, e);
      }

      @Override public Encoder encoder()
      {
        return Transport.encoder(v, e);
      }

      @Override public Receiver.Codecs codecs()
      {
        return Transport.codecs();
      }
    });
  }

  static Connector connector(Version v, Encoder e, Decoder d, Sender s)
  {
    switch(v)
    {
      default:
        return new ConnectorNov2016(v, e, d, s);
    }
  }

  static Decoder decoder(Version v, Encoding e)
  {
    return decoders.get(e).get(v);
  }

  static Encoder encoder(Version v, Encoding e)
  {
    return encoders.get(e).get(v);
  }

  static Receiver.Codecs codecs()
  {
    return codecs;
  }

  static final Receiver.Codecs codecs;
  static final EnumMap<Encoding, Map<Version, Decoder>> decoders
    = new EnumMap<>(Encoding.class);
  static final EnumMap<Encoding, Map<Version, Encoder>> encoders
    = new EnumMap<>(Encoding.class);

  public enum Version
  {
    Nov2016
  }

  public enum Encoding
  {
    json
  }

  public enum Target
  {
    account, accounts, bank, banks, challengeThreshold, transaction,
    transactions, user, users;

    /**
     * Use the ids to create an unique key for cache lookups.
     *
     * @param ps source of the ids
     * @param s separator inserted between parts of key
     *
     * @return null when unable to create an unique key
     */
    public String toKey(Decoder.Parameters ps, String s)
    {
      String key;

      switch(this)
      {
        case account:
          key = ps.bankId()
            .flatMap(b -> ps.accountId()
              .flatMap(a -> ps.userId().map(u -> u + s + b + s + a)))
            .orElse(null);
          break;
        case accounts:
        case bank:
          key = ps.bankId()
            .flatMap(b -> ps.userId().map(u -> u + s + b))
            .orElse(null);
          break;
        case banks:
          key = "banks";
          break;
        case challengeThreshold:
          key = ps.accountId().flatMap(a -> ps.userId().map(u -> u + s + a))
            .orElse(null);
          break;
        case transaction:
          key = ps.bankId()
            .flatMap(b -> ps.accountId()
              .flatMap(a -> ps.transactionId()
                .flatMap(t -> ps.userId().map(u -> u + s + b + s + a + s + t))))
            .orElse(null);
          break;
        case transactions:
          key = ps.bankId()
            .flatMap(b -> ps.accountId()
              .flatMap(a -> ps.userId().map(u -> u + s + b + s + a)))
            .orElse(null);
          break;
        case user:
          key = ps.userId().map(identity()).orElse(null);
          break;
        case users:
          key = "users";
          break;
        default:
          key = null;
      }

      return key == null ? null : this.toString() + s + key;
    }
  }

  public interface Factory
  {
    /**
     * @param s sender, must not be null
     *
     * @return Connector
     *
     * @throws RuntimeException sender is null
     */
    Connector connector(Sender s);

    Decoder decoder();

    Encoder encoder();

    Receiver.Codecs codecs();
  }

  static
  {
    EnumMap<Version, Decoder> ds = new EnumMap<>(Version.class);
    EnumMap<Version, Encoder> es = new EnumMap<>(Version.class);

    ds.put(Nov2016, new DecoderNov2016(Nov2016));
    es.put(Nov2016, new EncoderNov2016(Nov2016));

    codecs = new Receiver.Codecs(es.get(Nov2016), ds.get(Nov2016));

    codecs.put(Nov2016, new Pair<>(es.get(Nov2016), ds.get(Nov2016)));

    decoders.put(json, ds);
    encoders.put(json, es);
  }
}
