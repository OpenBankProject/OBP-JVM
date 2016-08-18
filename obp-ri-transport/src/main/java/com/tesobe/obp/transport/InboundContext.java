/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

public class InboundContext {
    public Source source;
    public String message;

    public InboundContext(Source source, String message){
        this.source = source;
        this.message = message;
    }
}




