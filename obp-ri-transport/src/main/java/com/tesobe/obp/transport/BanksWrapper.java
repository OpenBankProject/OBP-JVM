/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class BanksWrapper {
    public Iterable<Bank> banks;
    public Optional<InboundContext> inboundContext;

    public BanksWrapper(Iterable<Bank> banks, Optional<InboundContext> inboundContext) {
        this.banks = banks;
        this.inboundContext = inboundContext;
    }
}
