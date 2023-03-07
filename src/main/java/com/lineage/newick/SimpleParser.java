package com.lineage.newick;

import java.io.InputStream;
import java.io.Reader;

public class SimpleParser extends ParserBase<SimpleNode> {

  public SimpleParser(InputStream stream) {
    super(stream);
  }

  public SimpleParser(Reader reader) {
    super(reader);
  }

  @Override
  protected void parseComments(String comment) {
    curr.setComment(comment);
  }

  @Override
  protected SimpleNode newNode() {
    return new SimpleNode();
  }

}
