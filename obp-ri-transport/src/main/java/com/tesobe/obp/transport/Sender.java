/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.transport;

@SuppressWarnings("WeakerAccess") public interface Sender
{
  String send(Message request) throws InterruptedException;
}
