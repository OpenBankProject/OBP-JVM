/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

public class UserContext {
    public UserContext(String userId)
    {
        this.userId = userId;
    }

    public final String userId;
}
