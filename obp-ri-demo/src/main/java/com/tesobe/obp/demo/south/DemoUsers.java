package com.tesobe.obp.demo.south;

import com.tesobe.obp.transport.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class DemoUsers
{
  public static final List<User> USERS;

  static
  {
    List<User> list = new ArrayList<>();

    list.add(new U("1", "Franz", "franz@example.com", "geheim"));
    list.add(new U("2", "Иван", "Иван@example.com", "секрет"));
    list.add(new U("3", "John", "john@example.com", "secret"));
    list.add(new U("4", "子", "zi@example.com", "秘密"));

    USERS = Collections.unmodifiableList(list);
  }

  static class U implements User
  {
    U(String id, String displayName, String email, String password)
    {
      this.id = id;
      this.displayName = displayName;
      this.email = email;
      this.password = password;
    }

    @Override public String id()
    {
      return id;
    }

    @Override public String displayName()
    {
      return displayName;
    }

    @Override public String email()
    {
      return email;
    }

    @Override public String password()
    {
      return password;
    }

    final String id;
    final String displayName;
    final String email;
    final String password;
  }
}
