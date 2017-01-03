/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Utils
{
  public static <K, V> Map<K, V> merge(K key, V value)
  {
    return merge(new HashMap<K, V>(), key, value);
  }

  public static <K, V> Map<K, V> merge(Map<K, V> map, K key, V value)
  {
    map.put(key, value);

    return map;
  }

  /**
   * Invoke zero parameter method name.
   *
   * @param o not null
   * @param name not null
   *
   * @return o.name()
   */
  public static Object invoke(Object o, String name)
  {
    try
    {
      Method method = o.getClass().getMethod(name);

      method.setAccessible(true);

      return method.invoke(o);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /**
   * Non-lazy clone of clojure's <tt>(map f c1 c2)</tt>.
   * Ignores stream characteristics.
   * Todo rm when in java.
   * <pre>
   *   ([f c1 c2]
   *     (lazy-seq
   *       (let [s1 (seq c1) s2 (seq c2)]
   *         (when (and s1 s2)
   *           (cons (f (first s1) (first s2))
   *                 (map f (rest s1) (rest s2)))))))
   * </pre>
   */
  public static <S, T, R> Stream<R> map(Stream<S> c1, Stream<T> c2,
    BiFunction<S, T, R> f)
  {
    Stream.Builder<R> builder = Stream.builder();
    Iterator<S> i = c1.iterator();
    Iterator<T> j = c2.iterator();

    while(i.hasNext() && j.hasNext())
    {
      builder.accept(f.apply(i.next(), j.next()));
    }

    return builder.build();
  }
}
