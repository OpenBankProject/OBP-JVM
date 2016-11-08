/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.nov2016;

import com.tesobe.obp.transport.Data;

/**
 * Convert data to an account.
 */
public class AccountReader implements Account
{
    public AccountReader(Data account)
    {
        data = account;
    }

    @Override public String balanceAmount()
    {
        return data.text(balanceAmount);
    }

    @Override public String bankId()
    {
        return data.text(bankId);
    }

    @Override public String balanceCurrency()
    {
        return data.text("balanceCurrency");
    }

    @Override public String iban()
    {
        return data.text(iban);
    }

    @Override public String label()
    {
        return data.text(label);
    }

    @Override public String number()
    {
        return data.text(number);
    }

    @Override public String type()
    {
        return data.text(type);
    }

    @Override public String userId()
    {
        return data.text(userId);
    }

    @Override public String id()
    {
        return data.text(accountId);
    }

    protected final Data data;
}
