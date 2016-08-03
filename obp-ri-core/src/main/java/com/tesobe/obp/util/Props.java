/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @since 2016.0
 */
public class Props
{
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
    return properties;
  }

  private Properties properties = new Properties();
}
