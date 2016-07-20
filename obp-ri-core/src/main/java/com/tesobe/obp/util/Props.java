/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 */
package com.tesobe.obp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Props
{
  public Props(String path) throws IOException
  {
    assert path != null;

    if(!path.startsWith("/"))
    {
      path = "/" + path;
    }

    try(InputStream props = getClass().getResourceAsStream(path))
    {
      properties.load(props);
    }
  }

  public Properties getProperties()
  {
    return properties;
  }

  private Properties properties = new Properties();
}
