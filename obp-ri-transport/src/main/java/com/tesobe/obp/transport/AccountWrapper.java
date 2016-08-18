/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class AccountWrapper {
    public Optional<Account> account;
    public Optional<InboundContext> inboundContext;

    public AccountWrapper(Optional<Account> account, Optional<InboundContext> inboundContext) {
        this.account = account;
        this.inboundContext = inboundContext;
    }
}
