/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport;

import java.util.List;

import static java.util.Arrays.asList;

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

public interface Account extends Id
{
  String id();

  String amount();

  String bank();

  String currency();

  String iban();

  String label();

  String number();

  String type();

  default List<String> fields()
  {
    return FIELDS;
  }

  List<String> FIELDS = asList("id", "amount", "bank", "currency", "iban",
    "label", "number", "type");
}
