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
 * Matches KafkaInboundBank in KafkaMappedConnector.
 * <pre>case class KafkaInboundBank(
 * id : String,
 * short_name : String,
 * full_name : String,
 * logo : String,
 * website : String)</pre>
 */
public interface Bank extends Id
{
  String id();

  String shortName();

  String fullName();

  String logo();

  String url();

  default List<String> fields()
  {
    return FIELDS;
  }

  List<String> FIELDS = asList("id", "fullName", "logo", "shortName", "url");
}
