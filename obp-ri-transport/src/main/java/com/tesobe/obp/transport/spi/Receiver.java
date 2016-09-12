/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Message;

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
}
