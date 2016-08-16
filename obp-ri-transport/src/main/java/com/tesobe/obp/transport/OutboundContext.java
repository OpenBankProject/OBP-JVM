package com.tesobe.obp.transport;

/**
 * Created by sorinmanole on 15/08/2016.
 */
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
