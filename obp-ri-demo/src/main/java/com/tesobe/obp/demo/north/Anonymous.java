package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.User;

@SuppressWarnings("WeakerAccess") public class Anonymous implements User
{
  @Override public String id()
  {
    return null;
  }

  @Override public String displayName()
  {
    return "Anonymous";
  }

  @Override public String email()
  {
    return "anonymous@example.org";
  }

  @Override public String password()
  {
    return "secret";
  }
}
