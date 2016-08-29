/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.json;

import org.junit.Test;

import java.util.Optional;

public class DecoderTest
{
  @Test public void foo()
  {
    System.out.println(foo(Optional.of(42), Optional.of("foo")));
    System.out.println(foo(Optional.empty(), Optional.of("foo")));
    System.out.println(foo(Optional.of(42), Optional.empty()));
    System.out.println(foo(Optional.empty(), Optional.empty()));
  }

  Optional<String> foo(Optional<Integer> a, Optional<String> b)
  {
    return a.flatMap(i -> b.map(s -> s + i));
  }

//  @Test public void banks() throws Exception
//  {
//    Connector.Bank bank = new Connector.Bank("id", "1", "2", "3", "4");
//    Connector.Bank decoded = deL.banks(encL.banks(bank)).iterator().next();
//
//    assertTrue(deepEquals(bank, decoded));
//    assertFalse(deL.banks(encL.banks()).iterator().hasNext());
//    assertFalse(deL.banks(encL.banks(noBank)).iterator().hasNext());
//    assertFalse(deL.banks(encL.banks(noBank, noBank)).iterator().hasNext());
//  }
//
//  @Test public void accounts() throws Exception
//  {
//    Connector.Account account = new Connector.Account("id", "1", "2", "3",
// "4",
//      "5", "6", "7");
//    Connector.Account decoded = deL.account(encL.account(account))
//      .orElseThrow(RuntimeException::new);
//
//    assertTrue(deepEquals(account, decoded));
//  }
//
//  private Encoder encL = new Encoder(Transport.Version.legacy);
//  private Decoder deL = new Decoder(Transport.Version.legacy);
//  private static Connector.Bank noBank = null;
}
