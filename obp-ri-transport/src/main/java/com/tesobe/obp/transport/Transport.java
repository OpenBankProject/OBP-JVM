/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import com.tesobe.obp.transport.spi.*;

import java.util.*;

import static com.tesobe.obp.transport.Transport.Encoding.json;
import static com.tesobe.obp.transport.Transport.Version.legacy;

/**
 * Transport manages the different versions of the transport api.
 *
 * @since 2016.0
 */
@SuppressWarnings("WeakerAccess") public abstract class Transport
{
  /**
   * @return version.legacy, encoding JSON
   * @since 2016.0
   */
  public static Optional<Factory> defaultFactory()
  {
    return Optional.of(new Factory()
    {
      @Override public Connector connector(Sender s)
      {
        if(s == null)
        {
          throw new IllegalArgumentException("Sender is required!");
        }

        return Transport.connector(legacy, encoder(), decoder(), s);
      }

      @Override public Decoder decoder()
      {
        return Transport.decoder(legacy, json);
      }

      @Override public Encoder encoder()
      {
        return Transport.encoder(legacy, json);
      }
    });
  }

  static Connector connector(Version v, Encoder e, Decoder d, Sender s)
  {
    return new LegacyConnector(v, e, d, s);
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
     * @return Connector
     * @throw RuntimeException is sender is null
     * @since 2016.0
     */
    Connector connector(Sender s);

    Decoder decoder();

    Encoder encoder();
  }

  public enum Version
  {
    legacy
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

    ds.put(legacy, new com.tesobe.obp.transport.json.Decoder(legacy));
    es.put(legacy, new com.tesobe.obp.transport.json.Encoder(legacy));

    decoders.put(json, ds);
    encoders.put(json, es);
  }
}
