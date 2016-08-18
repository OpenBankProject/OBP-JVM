/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class BankWrapper {
    public Optional<Bank> bank;
    public Optional<InboundContext> inboundContext;

    public BankWrapper(Optional<Bank> bank, Optional<InboundContext> inboundContext) {
        this.bank = bank;
        this.inboundContext = inboundContext;
    }
}
