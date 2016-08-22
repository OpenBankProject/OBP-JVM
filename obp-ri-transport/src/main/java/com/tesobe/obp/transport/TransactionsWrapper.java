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
    public Iterable<Transaction> transactions;
    public Optional<InboundContext> inboundContext;

    public TransactionsWrapper(Iterable<Transaction> transactions, Optional<InboundContext> inboundContext) {
        this.transactions = transactions;
        this.inboundContext = inboundContext;
    }
}
