package com.tesobe.obp.transport;

/**
 * Created by sorinmanole on 15/08/2016.
 */
public class ViewContext {
    public ViewContext(String viewId, Boolean isPublic)
    {
        this.viewId = viewId;
        this.isPublic = isPublic;
    }

    public final String viewId;
    public final Boolean isPublic;
}
