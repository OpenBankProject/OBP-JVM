/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

public class OutboundContext {
    public OutboundContext(UserContext user, ViewContext view, TokenContext token)
    {
        this.user = user;
        this.view = view;
        this.token = token;
    }

    public final UserContext user;
    public final ViewContext view;
    public final TokenContext token;
}
