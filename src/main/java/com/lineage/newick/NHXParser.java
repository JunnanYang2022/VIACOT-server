package com.lineage.newick;

import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NHXParser extends ParserBase<NHXNode> {
  public static final String START = "&&NHX";
  private static final Pattern COMMENTS = Pattern.compile(":([A-Z][A-Za-z]*)=(.*?)(?=:[A-Z][A-Za-z]*=|$)");

  public NHXParser(InputStream stream) {
    super(stream);
  }

  public NHXParser(Reader reader) {
    super(reader);
  }

  @Override
  protected void parseComments(String comment) {
    parseComments(comment, curr);
  }

  // standalone method for easy testing of the comment parsing
  static void parseComments(String comment, NHXNode node) {
    if (comment != null && comment.startsWith(START)) {
      Matcher m = COMMENTS.matcher(comment.substring(START.length()));
      while (m.find()) {
        node.setValue(m.group(1), m.group(2));
      }
    }
  }

  @Override
  protected NHXNode newNode() {
    return new NHXNode();
  }

}
