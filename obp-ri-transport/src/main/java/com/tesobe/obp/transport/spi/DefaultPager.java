/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Default implementation of {@link Pager}.
 */
@SuppressWarnings("WeakerAccess") class DefaultPager
  implements Pager, Serializable
{
  public DefaultPager(int pageSize, int offset, String sortField,
    Pager.SortOrder so, String timestampField, ZonedDateTime earliest,
    ZonedDateTime latest)
  {
    this.offset = offset;
    this.size = pageSize;
    this.sortField = sortField;
    this.sortOrder = so;
    this.timestamp = timestampField;
    this.earliest = earliest;
    this.latest = latest;
  }

  public DefaultPager(int pageSize, int offset)
  {
    this(pageSize, offset, null, null, null, null, null);
  }

//    public List<Transaction> getTransactions(String bankId, String accountId)
//      throws InterruptedException
//    {
//      String id = UUID.randomUUID().toString();
//      String request = encoder
//        .getTransactions(this, bankId, accountId)
//        .toString();
//      String response = sender.send(new Message(id, request));
//      ArrayList<Transaction> page = new ArrayList<>();
//      Decoder.ResponseOld r = decoder.transactions(response);
//
//      more = r.more();
//
//      log.trace("{} \u2192 {}", request, response);
//
//      return r.transactions();
//    }

//    public List<Transaction> getTransactions(String bankId, String accountId,
//      String userId) throws InterruptedException
//    {
//      String id = UUID.randomUUID().toString();
//      String request = encoder
//        .getTransactions(this, bankId, accountId, userId)
//        .toString();
//      String response = sender.send(new Message(id, request));
//      Decoder.ResponseOld r = decoder.transactions(response);
//
//      more = r.more();
//
//      log.trace("{} \u2192 {}", request, response);
//
//      return r.transactions();
//    }

  @Override public boolean hasMorePages()
  {
    return more;
  }

  @Override public Pager nextPage()
  {
    return new DefaultPager(size, offset + size);
  }

  static final long serialVersionUID = 42L;
  public final int offset;
  public final int size;
  public final String sortField;
  public final String timestamp;
  public final SortOrder sortOrder;
  public final ZonedDateTime earliest;
  public final ZonedDateTime latest;
  protected boolean more;
}
