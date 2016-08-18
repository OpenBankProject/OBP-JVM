/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.Optional;

public class UserWrapper {
    public Optional<User> user;
    public Optional<InboundContext> inboundContext;

    public UserWrapper(Optional<User> user, Optional<InboundContext> inboundContext) {
        this.user = user;
        this.inboundContext = inboundContext;
    }
}