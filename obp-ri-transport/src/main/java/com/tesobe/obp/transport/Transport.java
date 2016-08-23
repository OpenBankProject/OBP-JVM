/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.json.DecoderV0;
import com.tesobe.obp.transport.json.EncoderV0;
import com.tesobe.obp.transport.spi.Connector;
import com.tesobe.obp.transport.spi.Decoder;
import com.tesobe.obp.transport.spi.Encoder;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.Transport.Encoding.json;

/**
 * Transport manages the different versions of the transport api.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public abstract class Transport
{
  /**
   * Uses {@link Version#v0}, {@link Encoding#json}.
   *
   * @return a factory that is always available
   *
   * @since 2016.0
   */
  public static Factory defaultFactory()
  {
    //noinspection OptionalGetWithoutIsPresent
    return factory(Version.v0, Encoding.json).get();
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
      @Override public com.tesobe.obp.transport.spi.Connector connector(Sender s)
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

  static com.tesobe.obp.transport.spi.Connector connector(Version v, Encoder e, Decoder d, Sender s)
  {
    return new Connector(v, e, d, s);
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
     * @param s not null
     *
     * @return Connector
     *
     * @throw RuntimeException is sender is null
     * @since 2016.0
     */
    com.tesobe.obp.transport.spi.Connector connector(Sender s);

    Decoder decoder();

    Encoder encoder();
  }

  public enum Version
  {
    v0
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

    ds.put(Version.v0, new DecoderV0(Version.v0));
    es.put(Version.v0, new EncoderV0(Version.v0));

    decoders.put(json, ds);
    encoders.put(json, es);
  }
}
