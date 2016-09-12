/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

/**
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public interface Sender
{
  /**
   * @param request may be null
   *
   * @return response may be null
   *
   * @throws InterruptedException network
   * @since 2016.9
   */
  String send(Message request) throws InterruptedException;
}
