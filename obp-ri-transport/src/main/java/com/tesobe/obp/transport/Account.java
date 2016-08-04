/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

/**
 * Matches KafkaMappedConnector's KafkaInboundAccount.
 * <pre>KafkaInboundAccount(
 * id : String,
 * bank : String,
 * label : String,
 * number : String,
 * `type` : String,
 * balance : KafkaInboundBalance, = (currency, amount)
 * IBAN : String,
 * owners : List[String],
 * generate_public_view : Boolean,
 * generate_accountants_view : Boolean,
 * generate_auditors_view : Boolean)</pre>.
 */

public interface Account
{
  public String id();

  public String bank();

  public String label();

  public String number();

  public String type();

  public String currency();

  public String amount();

  public String iban();
}
