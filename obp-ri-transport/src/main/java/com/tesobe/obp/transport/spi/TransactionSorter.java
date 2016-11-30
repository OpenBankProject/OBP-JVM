/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Pager;
import com.tesobe.obp.transport.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @since 2016.11
 */
@SuppressWarnings("WeakerAccess") public class TransactionSorter
  implements Comparator<Transaction>
{
  public TransactionSorter(String field)
  {
    this.field = field;
  }

  @Override public int compare(Transaction t1, Transaction t2)
  {
    switch(field)
    {
      case "accountId":
        return compare(t1.accountId(), t2.accountId());
      case "amount":
        return compare(t1.amount(), t2.amount());
      case "bankId":
        return compare(t1.bankId(), t2.bankId());
      case "completedDate":
        return compare(t1.completedDate(), t2.completedDate());
      case "counterpartyId":
        return compare(t1.counterpartyId(), t2.counterpartyId());
      case "counterpartyName":
        return compare(t1.counterpartyName(), t2.counterpartyName());
      case "currency":
        return compare(t1.currency(), t2.currency());
      case "description":
        return compare(t1.description(), t2.description());
      case "newBalanceAmount":
        return compare(t1.newBalanceAmount(), t2.newBalanceAmount());
      case "newBalanceCurrency":
        return compare(t1.newBalanceCurrency(), t2.newBalanceCurrency());
      case "postedDate":
        return compare(t1.postedDate(), t2.postedDate());
      case "transactionId":
        return compare(t1.transactionId(), t2.transactionId());
      case "type":
        return compare(t1.type(), t2.type());
      case "userId":
        return compare(t1.userId(), t2.userId());
    }

    return 0;
  }

  private int compare(ZonedDateTime v1, ZonedDateTime v2)
  {
    if(v1 == null)
    {
      return v2 == null ? 0 : 2;
    }
    else
    {
      return v2 == null ? 1 : v1.compareTo(v2);
    }
  }

  private int compare(BigDecimal v1, BigDecimal v2)
  {
    if(v1 == null)
    {
      return v2 == null ? 0 : 2;
    }
    else
    {
      return v2 == null ? 1 : v1.compareTo(v2);
    }
  }

  private int compare(String v1, String v2)
  {
    if(v1 == null)
    {
      return v2 == null ? 0 : 2;
    }
    else
    {
      return v2 == null ? 1 : v1.compareTo(v2);
    }
  }

  public static Comparator<Transaction> make(List<String> fields,
    List<Pager.SortOrder> orders)
  {
    assert fields != null;
    assert orders != null;

    class Rec
    {
      Comparator<Transaction> mk(int i)
      {
        if(i < fields.size())
        {
          Comparator<Transaction> d = get(i);

          if(d != null)
          {
            c = c == null ? d : c.thenComparing(d);
          }

          return mk(i + 1);
        }
        else
        {
          return c;
        }
      }

      private Comparator<Transaction> get(int i)
      {
        switch(orders.get(i))
        {
          case ascending:
            return new TransactionSorter(fields.get(i));
          case descending:
            return new TransactionSorter(fields.get(i)).reversed();
        }

        return null; // source order: do not sort
      }

      Comparator<Transaction> c;
    }

    Comparator<Transaction> comparator = new Rec().mk(0);

    // tricky edge case: all sort fields have sort order source - which means
    // do not sort - but a sorter is requested, best try is all are equal
    return comparator != null ? comparator : (t1, t2) -> 0;
  }

  private final String field;
}
