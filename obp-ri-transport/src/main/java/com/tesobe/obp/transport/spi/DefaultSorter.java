/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;

import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableSortedMap;

/**
 * @see Pager.Sorter
 * @since @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class DefaultSorter
  implements Pager.Sorter, Serializable
{
  public DefaultSorter(String fieldName, Pager.SortOrder so)
  {
    this(new TreeMap<>(singletonMap(fieldName, so)));
  }

  protected DefaultSorter(SortedMap<String, Pager.SortOrder> fields)
  {
    this.fields = unmodifiableSortedMap(fields);
  }

  @Override public SortedMap<String, Pager.SortOrder> fields()
  {
    return fields;
  }

  @Override public <T> List<T> sort(List<T> items, Class<T> type)
  {
    return items;
  }

  public static Builder build(String fieldName, Pager.SortOrder so)
  {
    return new Builder(fieldName, so);
  }
  static final long serialVersionUID = 42L;
  final SortedMap<String, Pager.SortOrder> fields;

  public static class Builder
  {
    public Builder(String fieldName, Pager.SortOrder so)
    {
      add(fieldName, so);
    }

    public Builder add(String fieldName, Pager.SortOrder so)
    {
      fields.put(fieldName, so);

      return this;
    }

    public Pager.Sorter toSorter()
    {
      return new DefaultSorter(fields);
    }

    TreeMap<String, Pager.SortOrder> fields = new TreeMap<>();
  }
}
