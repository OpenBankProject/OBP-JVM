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

import static java.util.Objects.nonNull;

/**
 * Todo error handling
 */
public final class InboundContextDecoder
{
    public static InboundContext inboundContextFromJsonObject(JSONObject response)
    {
        if(nonNull(response))
        {
            JSONObject responseObject = response.optJSONObject("response");
            JSONObject inboundContextObject = responseObject.optJSONObject("inboundContext");
            JSONObject sourceObject = inboundContextObject.optJSONObject("source");

            return new InboundContext(SourceDecoder.sourceFromJsonObject(sourceObject), inboundContextObject.optString("message"));
        }

        return null;
    }
}
