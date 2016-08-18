/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.time.ZonedDateTime;

public class Source {
    public ZonedDateTime timestamp;
    public String originatingSource;

    public Source(ZonedDateTime timestamp, String originatingSource){
        this.timestamp = timestamp;
        this.originatingSource = originatingSource;
    }
}
