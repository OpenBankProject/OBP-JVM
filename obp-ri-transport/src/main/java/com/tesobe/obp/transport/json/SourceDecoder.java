/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */

package com.tesobe.obp.transport.json;

import com.tesobe.obp.transport.Source;
import org.json.JSONObject;

import static com.tesobe.obp.util.Json.zonedDateTimeFromJson;
import static java.util.Objects.nonNull;

/**
 * Todo error handling
 */
public final class SourceDecoder
{
    public static Source sourceFromJsonObject(JSONObject source)
    {
        if(nonNull(source))
        {
            return new Source(zonedDateTimeFromJson(source.optString("timestamp")), source.optString("message"));
        }

        return null;
    }
}
