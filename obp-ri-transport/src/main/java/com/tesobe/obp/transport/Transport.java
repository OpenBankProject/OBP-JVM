/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.DefaultConnector;
import com.tesobe.obp.transport.spi.Encoder;
import com.tesobe.obp.util.DefaultMetrics;
import com.tesobe.obp.util.Metrics;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.Sep2016;

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
   * @since 2016.9
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
   * @since 2016.9
   */
  public static Factory defaultFactory(Metrics m)
  {
    //noinspection OptionalGetWithoutIsPresent
    return factory(Version.Sep2016, Encoding.json).get();
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
    });
  }

  static Connector connector(Version v, Encoder e, Decoder d, Sender s)
  {
    return new DefaultConnector(v, e, d, s);
  }

  static Decoder decoder(Version v, Encoding e)
  {
    return decoders.get(e).get(v);
  }

  static Encoder encoder(Version v, Encoding e)
  {
    return encoders.get(e).get(v);
  }

  public interface Factory
  {
    /**
     * @param s sender, must not be null
     *
     * @return Connector
     *
     * @throws RuntimeException sender is null
     * @since 2016.9
     */
    Connector connector(Sender s);

    Decoder decoder();

    Encoder encoder();
  }

  public enum Version
  {
    Sep2016
  }

  public enum Encoding
  {
    json
  }

  static final EnumMap<Encoding, Map<Version, Decoder>> decoders
    = new EnumMap<>(Encoding.class);
  static final EnumMap<Encoding, Map<Version, Encoder>> encoders
    = new EnumMap<>(Encoding.class);

  static
  {
    EnumMap<Version, Decoder> ds = new EnumMap<>(Version.class);
    EnumMap<Version, Encoder> es = new EnumMap<>(Version.class);

    ds.put(Sep2016, new com.tesobe.obp.transport.json.Decoder(Sep2016));

    es.put(Sep2016, new com.tesobe.obp.transport.json.Encoder(Sep2016));

    decoders.put(json, ds);
    encoders.put(json, es);
  }
}
