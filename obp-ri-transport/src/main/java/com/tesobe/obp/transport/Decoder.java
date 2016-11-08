/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @since 2016.9
 */
public interface Decoder
{
  /**
   * North: decode south's response to a get request.
   *
   * @param t target
   * @param response encoded response
   *
   * @return decoded response
   */
  Response get(Transport.Target t, String response);

  /**
   * North: decode south's response to a put request.
   *
   * @param t target
   * @param response encoded response
   *
   * @return decoded response
   */
  Response put(Transport.Target t, String response);

  Response fetch(String response);

  String describe(String response);

  Transport.Version version();

  /**
   * South: decode north's request.
   *
   * @param requestId unique id
   * @param request encoded payload
   *
   * @return decoded request
   */
  Optional<Request> request(String requestId, String request);

  interface Request
  {
    String raw();

    String name();

    String version();

    Optional<Transport.Target> target();

    Pager pager();

    Parameters parameters();

    Map<String, ?> fields();

    String requestId();
  }

  interface Pager
  {
    boolean isPaged();

    int count();

    int offset();

    int size();

    Optional<String> state();

    Optional<String> filterType();

    <T> Optional<com.tesobe.obp.transport.Pager.Filter<T>> filter(String name,
      Class<T> type);

    Optional<com.tesobe.obp.transport.Pager.Sorter> sorter();
  }

  /**
   * The parameters are version independent, their names are versioned.
   */
  interface Parameters
  {
    Optional<String> accountId();

    Optional<String> bankId();

    Optional<String> transactionId();

    Optional<String> userId();

    Optional<String> type();

    String requestId();

    Transport.Version version();
  }

  interface Response
  {
    List<Data> data();

    boolean hasMorePages();

    String state();

    int count();

    Optional<Error> error();
  }

  interface Error
  {
    String message();
  }
}
