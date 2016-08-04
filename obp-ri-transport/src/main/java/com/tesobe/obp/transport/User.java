/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

/**
 * Matches KafkaMappedConnector's KafkaInboundUser.
 * <pre>
 * case class KafkaInboundUser(
 *   email : String,
 *   password : String,
 *   display_name : String)
 * </pre>
 */
public interface User
{
  String displayName();

  String email();

  String password();
}
