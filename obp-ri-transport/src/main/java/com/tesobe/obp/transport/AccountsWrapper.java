/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class AccountsWrapper {
    public Iterable<Account> accounts;
    public Optional<InboundContext> inboundContext;

    public AccountsWrapper(Iterable<Account> accounts, Optional<InboundContext> inboundContext) {
        this.accounts = accounts;
        this.inboundContext = inboundContext;
    }
}
