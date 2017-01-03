/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * todo document
 */
@SuppressWarnings("WeakerAccess") public class DefaultResponse
  implements Response
{
  public DefaultResponse()
  {
    this(null, null);
  }

  public DefaultResponse(List<? extends Map<String, ?>> data)
  {
    this(data, null);
  }

  public DefaultResponse(Map<String, ?> data)
  {
    this(Collections.singletonList(data), null);
  }

  public DefaultResponse(List<? extends Map<String, ?>> data,
    Map<String, ?> meta)
  {
    this.data = data;
    this.meta = meta;
  }

  @Override public List<? extends Map<String, ?>> data()
  {
    return data;
  }

  @Override public Map<String, ?> meta()
  {
    return meta;
  }

  final List<? extends Map<String, ?>> data;
  final Map<String, ?> meta;
}
