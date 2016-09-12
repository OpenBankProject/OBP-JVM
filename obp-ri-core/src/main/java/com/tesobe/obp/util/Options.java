/*
 * Copyright (c) TESOBE Ltd. 2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license
 * that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.util;

import joptsimple.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Command line interface. Use like this:
 * <pre>
 * public static void main(String[] theArguments) throws IOException
 * {
 *   if(options.parse(theArguments))
 *   {
 *     InputStream is = options.input(input);
 *     ...
 *   }
 * }
 *
 * static Options options = new Options("This message.", "?", "h", "help");
 * static OptionSpec<File> input = options.acceptsAll("Input", "i", "input")
 *   .withRequiredArg().ofType(File.class).describedAs("INPUT");
 * </pre>
 *
 * @author ub@kassapo.com
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class Options
{
  public Options(String helpDescription, String... helpFlags)
  {
    parser.posixlyCorrect(false);

    help = acceptsAll(helpDescription, helpFlags).forHelp();
  }

  public Options()
  {
    this("This message.", "?", "h", "help");
  }

  public boolean isValid()
  {
    return options != null;
  }

  public StringBuilder nonOptionArgumentBuffer()
  {
    StringBuilder buffer = new StringBuilder();
    Iterator<?> i = nonOptionArguments().iterator();

    if(i.hasNext())
    {
      buffer.append(i.next());

      while(i.hasNext())
      {
        buffer.append(' ');
        buffer.append(i.next());
      }
    }

    return buffer;
  }

  @Override public String toString()
  {
    StringBuilder buffer = new StringBuilder();

    if(isValid())
    {
      List<OptionSpec<?>> specs = options.specs();

      for(OptionSpec<?> flag : specs)
      {
        if(flag instanceof OptionDescriptor)
        {
          buffer.append(((OptionDescriptor)flag).description());
        }
        else
        {
          buffer.append(flag);
        }

        if(options.hasArgument(flag))
        {
          buffer.append(": ");
          buffer.append(options.valueOf(flag));
        }

        buffer.append('\n');
      }

      buffer.append(nonOptionArgumentBuffer());
    }

    return buffer.toString();
  }

  public <T> T valueOf(OptionSpec<T> option)
  {
    return option.value(options);
  }

  public <T> List<T> valuesOf(OptionSpec<T> option)
  {
    return option.values(options);
  }

  public <T> boolean isPresent(OptionSpec<T> option)
  {
    return options.specs().contains(option);
  }

  public OutputStream output(OptionSpec<File> option)
    throws FileNotFoundException
  {
    return new BufferedOutputStream(new FileOutputStream(valueOf(option)));
  }

  public Writer fileWriter(OptionSpec<File> option, String charset)
    throws FileNotFoundException
  {
    return new OutputStreamWriter(output(option), Charset.forName(charset));
  }

  public InputStream input(OptionSpec<File> option) throws FileNotFoundException
  {
    return new BufferedInputStream(new FileInputStream(valueOf(option)));
  }

  public String password(OptionSpec<Password> option)
  {
    return password(options.valueOf(option));
  }

  public String password(Password aPassword)
  {
    return aPassword.value;
  }

  public Reader fileReader(OptionSpec<File> file, OptionSpec<String> charset)
    throws FileNotFoundException, UnsupportedEncodingException
  {
    return new InputStreamReader(input(file), valueOf(charset));
  }

  public OptionSpecBuilder acceptsAll(String description, String... flags)
  {
    return parser.acceptsAll(asList(flags), description);
  }

  public <T> boolean has(OptionSpec<T> option)
  {
    return options.has(option);
  }

  public List<?> nonOptionArguments()
  {
    return options.nonOptionArguments();
  }

  public boolean parse(String[] theArguments) throws IOException
  {
    try
    {
      options = parser.parse(theArguments);

      if(help != null && options.has(help))
      {
        printHelp();

        options = null;
      }

      return isValid();
    }
    catch(RuntimeException e)
    {
      printHelp();

      throw e;
    }
  }

  public void printHelp() throws IOException
  {
    printHelp(System.out);
  }

  public void printHelp(PrintStream out) throws IOException
  {
    for(String d : description)
    {
      out.println(d);
    }

    parser.printHelpOn(out);
  }

  public void setDescription(String... aDescription)
  {
    description = aDescription;
  }

  OptionSet options;
  final OptionParser parser = new OptionParser();
  OptionSpec<Void> help;
  String[] description = new String[0];

  /**
   * An OptionSpec type that does not print its value. Get it via {@link
   * #password(OptionSpec)} or {@link #password(Password)} .
   */
  public static class Password
  {
    public Password(String aValue)
    {
      value = aValue;
    }

    @Override public String toString()
    {
      return "xxxx";
    }

    String value;
  }
}
