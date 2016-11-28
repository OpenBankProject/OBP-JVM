/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.List;
import java.util.SortedMap;

public interface Pager
{
  boolean hasMorePages();

  void nextPage();

  int offset();

  int size();

  Filter<?> filter();

  Sorter sorter();

  int DEFAULT_OFFSET = 0;
  int DEFAULT_SIZE = Integer.MAX_VALUE;

  enum SortOrder
  {
    ascending, descending, source
  }

  interface Filter<T>
  {
    String fieldName();

    Class<T> type();

    T lowerBound();

    T higherBound();
  }

  interface Sorter
  {
    /**
     * Fields in the map are sorted in the order they appear in the map.
     * If a field name in the map does not exist as a sortable field in the data
     * it is skipped without warning.
     *
     * @return maps a field name to how it is sorted
     */
    SortedMap<String, SortOrder> fields();

    <T> List<T> sort(List<T> items, Class<T> type);
  }
}
