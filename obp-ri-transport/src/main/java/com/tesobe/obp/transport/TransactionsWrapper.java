/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class TransactionsWrapper {
    public Iterable<Transaction> transaction;
    public Optional<InboundContext> inboundContext;

    public TransactionsWrapper(Iterable<Transaction> transaction, Optional<InboundContext> inboundContext) {
        this.transaction = transaction;
        this.inboundContext = inboundContext;
    }
}
