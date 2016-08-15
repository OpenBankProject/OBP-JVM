package com.tesobe.obp.transport;

/**
 * Created by sorinmanole on 15/08/2016.
 */
public class Context {
    public Context(UserContext user, ViewContext view)
    {
        this.user = user;
        this.view = view;
    }

    public final UserContext user;
    public final ViewContext view;
}
