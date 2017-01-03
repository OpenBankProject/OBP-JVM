/*
 * Copyright (c) TESOBE Ltd.  2017. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.util.List;
import java.util.Map;

/**
 * todo document
 */
public interface Response
{
  List<? extends Map<String, ?>> data();

  Map<String, ?> meta();
}
