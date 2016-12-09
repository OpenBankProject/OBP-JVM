/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.json.DecoderNov2016;
import com.tesobe.obp.transport.json.EncoderNov2016;
import com.tesobe.obp.transport.spi.ConnectorNov2016;
import com.tesobe.obp.transport.spi.Receiver;
import com.tesobe.obp.util.DefaultMetrics;
import com.tesobe.obp.util.Metrics;
import com.tesobe.obp.util.Pair;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.Nov2016;

/**
 * Transport manages the different versions of the transport api.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public abstract class Transport
{
  /**
   * Uses {@link Version#Sep2016}, {@link Encoding#json}.
   *
   * @return a factory that is always available
   *
   */
  public static Factory defaultFactory()
  {
    return defaultFactory(new DefaultMetrics());
  }

  /**
   * Uses {@link Version#Sep2016}, {@link Encoding#json}.
   *
   * @return a factory that is always available
   *
   */
  public static Factory defaultFactory(Metrics m)
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
    return factory(v, e, new DefaultMetrics());
  }

  /**
   * Try to provide a factory for a version / encoding combination.
   *
   * @param v the API / SPI version
   * @param e the encoding used by the transport layer
   * @param m metrics
   *
   * @return empty if the version / encoding combination is not available
   */
  public static Optional<Transport.Factory> factory(Version v, Encoding e,
    Metrics m)
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

  public static Target target(String target)
  {
    return targets.get(target);
  }

  public static final Map<String, Target> targets = new HashMap<>();

  static final Receiver.Codecs codecs;
  static final EnumMap<Encoding, Map<Version, Decoder>> decoders
    = new EnumMap<>(Encoding.class);
  static final EnumMap<Encoding, Map<Version, Encoder>> encoders
    = new EnumMap<>(Encoding.class);

  public enum Version
  {
    Sep2016, Nov2016
  }

  public enum Encoding
  {
    json
  }

  public enum Target
  {
    account, accounts, bank, banks, transaction, transactions, user, users
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

    for(Target t : Target.values())
    {
      targets.put(t.name(), t);
    }
  }
}
