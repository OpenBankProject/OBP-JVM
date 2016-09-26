package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Collections.unmodifiableList;

@SuppressWarnings("WeakerAccess") public class Users
{
  public Users(Iterable<User> namedUsers)
  {
    List<User> all = new ArrayList<>();

    all.add(new Anonymous());

    if(namedUsers != null)
    {
      StreamSupport.stream(namedUsers.spliterator(), false)
        .filter(Objects::nonNull)
        .sorted((o1, o2) -> o1.displayName().compareTo(o2.displayName()))
        .forEach(u ->
        {
          all.add(u);
          users.put(u.id(), u);
        });
    }

    sorted = unmodifiableList(all);
  }

  public User user(String id)
  {
    User user = null;

    if(id != null)
    {
      user = users.get(id);

      if(user == null)
      {
        user = new Nemo(id);
      }
    }

    return user;
  }

  public List<User> users()
  {
    return sorted;
  }

  protected final List<User> sorted;
  protected final Map<String, User> users = new HashMap<>();
}
