/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Map;

/**
 * North side API.
 *
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public interface Connector
{
  /**
   * Request information about the south.
   *
   * @return JSON describing the versions, verbs, targets, and fields south
   * supports.
   *
   * @throws Exception network trouble
   */
  String describe() throws Exception;

  /**
   * Fetch data from the south without paging.
   * <p>
   * Todo: it is not clearly specified which request parameters are hashes!
   *
   * @param caller label for the message, not required
   * @param target select data source, required
   * @param parameters request parameters, not required
   *
   * @return a response
   *
   * @throws InterruptedException network trouble
   */
  default Decoder.Response get(String caller, Transport.Target target,
    Map<String, ?> parameters) throws Exception
  {
    return get(caller, target, pager(), parameters);
  }

  /**
   * Fetch data from the south with optional paging.
   * <p>
   * Todo: it is not clearly specified which request parameters are hashes!
   *
   * @param caller label for the message, not required
   * @param target select data source, required
   * @param pager how to filter, sort and page
   * @param parameters request parameters, not required
   *
   * @return a response todo fix
   *
   * @throws Exception network trouble
   */
  Decoder.Response get(String caller, Transport.Target target, Pager pager,
    Map<String, ?> parameters) throws Exception;

  /**
   * North: Send data to the south.
   * <p>
   * Todo: it is not clearly specified which request parameters are hashes!
   *
   * @param caller label for the message, not required
   * @param target select data sink, required
   * @param parameters
   * @param fields input parameters
   *
   * @return a response
   *
   * @throws Exception network trouble
   */
  Decoder.Response put(String caller, Transport.Target target,
    Map<String, ?> parameters, Map<String, ?> fields) throws Exception;

  /**
   * North: fetch asynchronous results from previous puts.
   *
   * @return a map of transactionId -> result
   *
   * @throws Exception network trouble
   */
  Decoder.Response fetch() throws Exception;

  /**
   * A pager in source sort order with offset zero, infinite page size and no
   * constraints.
   *
   * @return a pager
   */
  Pager pager();

  /**
   * The result set produced on the south side is split into pages that are
   * sent individually. First any filter is applied, then the result
   * is sorted. Then, starting at offset, pageSize many items are sent upon
   * request.
   * <p>
   * If following request do not occur in a timely manner, the south may
   * discard
   * the result set. If this happens, you can detect it by looking at {@link
   * Pager#count()}. When it is reset to zero, the state was lost south side.
   * The default implementation checks for this.
   *
   * @param pageSize the maximum number of items sent. Set to zero to return
   * all items.
   * @param offset the index into the result set of the first item to send
   * @param f a filter, not required
   * @param s a sorter, not required
   *
   * @return a pager
   */
  Pager pager(int pageSize, int offset, Pager.Filter f, Pager.Sorter s);

  /**
   * @return the underlying transport
   */
  Sender sender();
}
