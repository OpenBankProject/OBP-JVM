/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Decoder;
import com.tesobe.obp.transport.Encoder;
import com.tesobe.obp.transport.Message;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.util.Pair;

import java.util.EnumMap;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public interface Receiver
{
  /**
   * Respond to a request. On error, please respond with an error message.
   *
   * @param request anything
   *
   * @return should not return null to avoid exceptions
   */
  String respond(Message request);

  /**
   *
   */
  class Codecs extends EnumMap<Transport.Version, Pair<Encoder, Decoder>>
  {
    public Codecs(Encoder errorEncoder, Decoder requestDecoder)
    {
      super(Transport.Version.class);

      this.requestDecoder = requestDecoder;
      this.errorEncoder = errorEncoder;
    }

    public final Decoder requestDecoder;
    public final Encoder errorEncoder;
  }
}
