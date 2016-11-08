/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

/**
 * Parameter names. Parameters are the values the north supplies to {@link
 * com.tesobe.obp.transport.Connector} calls.
 */
public interface Parameters
{
  String accountId = Account.accountId;
  String bankId = Bank.bankId;
  String transactionId = Transaction.transactionId;
  String userId = User.id;
  String type = "type";
}
