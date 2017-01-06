/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tesobe.obp.util.Utils.asMap;
import static java.util.Collections.singletonList;

/**
 * Immutable.
 */
@SuppressWarnings("WeakerAccess") public class DefaultResponse
  implements Response, Serializable
{
  public DefaultResponse()
  {
    this(null, null);
  }

  public DefaultResponse(List<? extends Map<String, ?>> data)
  {
    this(data, null);
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

  /**
   * Add, or overwrite with fields from argument.
   *
   * @param r null allowed
   *
   * @return merge
   */
  public Response add(Response r)
  {
    if(r == null)
    {
      return this;
    }

    List<? extends Map<String, ?>> d1 = r.data();
    List<? extends Map<String, ?>> d2 = d1 == null ? data : d1;
    Map<String, ?> m1 = r.meta();

    if(m1 == null || m1.isEmpty())
    {
      return new DefaultResponse(d2, meta);
    }

    if(meta == null || meta.isEmpty())
    {
      return new DefaultResponse(d2, m1);
    }

    Map<String, Object> m2 = new HashMap<>(meta);

    m2.putAll(m1);

    return new DefaultResponse(d2, m2);
  }

  public static DefaultResponse from(Map<String, ?> data, Map<String, ?> meta)
  {
    return new DefaultResponse(data != null ? singletonList(data) : null, meta);
  }

  public static DefaultResponse fromData(Map<String, ?> data)
  {
    return from(data, null);
  }

  public static DefaultResponse fromMeta(Map<String, ?> meta)
  {
    return from(null, meta);
  }

  public static DefaultResponse error(String message)
  {
    return DefaultResponse.fromMeta(asMap("error", message));
  }

  static final long serialVersionUID = 42L;
  final List<? extends Map<String, ?>> data;
  final Map<String, ?> meta;
}
