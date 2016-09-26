package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.User;

@SuppressWarnings("WeakerAccess") public class Nemo implements User
{
  public Nemo(String id)
  {
    this.id = id;
  }

  @Override public String id()
  {
    return id;
  }

  @Override public String displayName()
  {
    return null;
  }

  @Override public String email()
  {
    return null;
  }

  @Override public String password()
  {
    return null;
  }

  private final String id;
}
