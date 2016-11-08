/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.SortedMap;

/**
 * @since 2016.11
 */
public interface Pager
{
  /**
   * When true a call to {@link #nextPage()} followed by another request
   * will proved more data.
   *
   * @return more?
   */
  boolean hasMorePages();

  /**
   * Required before requesting more data.
   */
  void nextPage();

  /**
   * Counts the requests made against the same result set. The response returns
   * the same number. Should the response return a different number (mostly
   * zero) then the south side has lost the state and all further data must be
   * discarded!
   *
   * @return greater than zero for subsequent requests against the same result
   */
  int count();

  int offset();

  int size();

  Filter<?> filter();

  Sorter sorter();

  String state();

  void more(String state, int count, boolean more);

  default boolean isPaged()
  {
    return false;
  }

  int DEFAULT_OFFSET = 0;
  int DEFAULT_SIZE = Integer.MAX_VALUE;

  enum FilterType
  {
    timestamp
  }

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
  }
}
