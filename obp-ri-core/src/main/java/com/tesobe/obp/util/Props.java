/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Read a properties file.
 *
 * @since 2016.9
 */
public class Props
{
  /**
   * Read properties from the class path.
   *
   * @param root use this class' classloader
   * @param path lookup
   *
   * @throws IOException not found
   */
  public Props(Class root, String path) throws IOException
  {
    if(root != null && path != null)
    {
      try(InputStream props = root.getResourceAsStream(path))
      {
        properties.load(props);
      }
    }
  }

  public Properties getProperties()
  {
    return new Properties(properties);
  }

  public Map<String, Object> toMap()
  {
    HashMap<String, Object> map = new HashMap<>();

    properties.forEach((k, v) -> map.put(k.toString(), v));

    return map;
  }

  private Properties properties = new Properties();
}
