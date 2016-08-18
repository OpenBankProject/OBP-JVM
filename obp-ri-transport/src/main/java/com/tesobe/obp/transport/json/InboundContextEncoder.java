/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.InboundContext;
import org.json.JSONObject;

/**
 * Todo error handling
 */
@SuppressWarnings("WeakerAccess") class InboundContextEncoder
{
    public InboundContextEncoder(InboundContext inboundContext)
    {
        assert inboundContext != null;

        this.inboundContext = inboundContext;
    }

    public JSONObject toJson()
    {
        // @formatter:off
        @SuppressWarnings("UnnecessaryLocalVariable")
        JSONObject json = new JSONObject()
                .put("message", inboundContext.message)
                .put("source", new JSONObject().put("timestamp", inboundContext.source.timestamp.toString())
                    .put("originatingSource", inboundContext.source.originatingSource));
        // @formatter:on

        return json;
    }

    private final InboundContext inboundContext;
}
