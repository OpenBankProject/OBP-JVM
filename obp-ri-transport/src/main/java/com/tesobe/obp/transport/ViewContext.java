/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

public class ViewContext {
    public ViewContext(String viewId, Boolean isPublic)
    {
        this.viewId = viewId;
        this.isPublic = isPublic;
    }

    public final String viewId;
    public final Boolean isPublic;
}
