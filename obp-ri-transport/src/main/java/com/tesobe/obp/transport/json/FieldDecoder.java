/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @since 2016.11
 */
class FieldDecoder extends AbstractMap<String, Object>
{
  FieldDecoder(JSONObject fields)
  {
    assert fields != null;

    json = fields;
  }

  @Override public Set<Entry<String, Object>> entrySet()
  {
    return json.keySet()
      .stream()
      .map(key -> new SimpleImmutableEntry<>(key, json.get(key)))
      .collect(Collectors.toSet());
  }

  final JSONObject json;
}
