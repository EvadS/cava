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
package net.consensys.cava.toml;

import net.consensys.cava.toml.internal.TomlParser.QuotedKeyContext;
import net.consensys.cava.toml.internal.TomlParser.UnquotedKeyContext;
import net.consensys.cava.toml.internal.TomlParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;

final class KeyVisitor extends TomlParserBaseVisitor<List<String>> {

  private final List<String> keys = new ArrayList<>();

  @Override
  public List<String> visitUnquotedKey(UnquotedKeyContext ctx) {
    keys.add(ctx.getText());
    return keys;
  }

  @Override
  public List<String> visitQuotedKey(QuotedKeyContext ctx) {
    StringBuilder builder = ctx.accept(new QuotedStringVisitor());
    keys.add(builder.toString());
    return keys;
  }

  @Override
  protected List<String> aggregateResult(List<String> aggregate, List<String> nextResult) {
    return aggregate == null ? null : nextResult;
  }

  @Override
  protected List<String> defaultResult() {
    return keys;
  }
}
