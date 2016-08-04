/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

/**
 * Matches these classes in KafkaMappedConnector.
 * <pre>
 * case class KafkaInboundTransaction(
 *   id : String,
 *   this_account : KafkaInboundAccountId,
 *   counterparty : Option[KafkaInboundTransactionCounterparty],
 *   details : KafkaInboundTransactionDetails)
 *
 * case class KafkaInboundTransactionCounterparty(
 *   name : Option[String],  // Also known as Label
 *   account_number : Option[String])
 *
 * case class KafkaInboundAccountId(
 *   id : String,
 *   bank : String)
 *
 * case class KafkaInboundTransactionDetails(
 *   `type` : String,
 *   description : String,
 *   posted : String,
 *   completed : String,
 *   new_balance : String,
 *   value : String)
 * </pre>
 */
public interface Transaction
{
}
