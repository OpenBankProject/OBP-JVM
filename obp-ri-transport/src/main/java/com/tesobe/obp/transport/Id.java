package com.tesobe.obp.transport;

import java.util.List;

public interface Id extends Comparable<Id>
{
  String id();

  List<String> fields();

  /**
   * Must handle null well.
   *
   * @param other null allowed
   *
   * @return null is less than non null
   */
  @SuppressWarnings("NullableProblems") @Override default int compareTo(
    Id other)
  {
    if(other == null)
    {
      return 1;
    }

    if(id() == null)
    {
      return other.id() == null ? 0 : -1;
    }
    else if(other.id() == null)
    {
      return 1;
    }
    else
    {
      return id().compareTo(other.id());
    }
  }
}
