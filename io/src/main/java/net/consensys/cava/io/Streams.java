/*
 * Copyright 2018, ConsenSys Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.cava.io;

import static java.util.Objects.requireNonNull;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utilities for working with streams.
 */
public final class Streams {
  private Streams() {}

  private static final PrintStream NULL_PRINT_STREAM = new PrintStream(NullOutputStream.INSTANCE);

  /**
   * @return An {@link OutputStream} that discards all input.
   */
  public static OutputStream nullOutputStream() {
    return NullOutputStream.INSTANCE;
  }

  /**
   * @return A {@link PrintStream} that discards all input.
   */
  public static PrintStream nullPrintStream() {
    return NULL_PRINT_STREAM;
  }

  /**
   * Stream an {@link Enumeration}.
   *
   * @param enumeration The enumeration.
   * @param <T> The type of objects in the enumeration.
   * @return A stream over the enumeration.
   */
  public static <T> Stream<T> enumerationStream(Enumeration<T> enumeration) {
    requireNonNull(enumeration);
    return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
      @Override
      public boolean tryAdvance(Consumer<? super T> action) {
        if (enumeration.hasMoreElements()) {
          action.accept(enumeration.nextElement());
          return true;
        }
        return false;
      }

      @Override
      public void forEachRemaining(Consumer<? super T> action) {
        while (enumeration.hasMoreElements()) {
          action.accept(enumeration.nextElement());
        }
      }
    }, false);
  }
}
