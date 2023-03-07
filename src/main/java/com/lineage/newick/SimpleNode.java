package com.lineage.newick;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.regex.Pattern;

public class SimpleNode extends Node<SimpleNode> {
  private static final Pattern RESERVED_IN_COMMENTS = Pattern.compile("[\\[\\]']");

  private String comment;

  public SimpleNode() {
  }

  public SimpleNode(String label) {
    super(label);
  }

  public SimpleNode(String label, Double length) {
    super(label, length);
  }

  public SimpleNode(String label, Double length, String comment) {
    super(label, length);
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  protected void writeComments(Writer w) throws IOException {
    if (comment != null) {
      w.append("[");
      w.append(escapeComment(comment));
      w.append("]");
    }
  }

  private static String escapeComment(String value) {
    // need for quoting?
    if (RESERVED_IN_COMMENTS.matcher(value).find()) {
      return "'" + QUOTE.matcher(value).replaceAll("''") + "'";
    }
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SimpleNode)) return false;
    if (!super.equals(o)) return false;
    SimpleNode that = (SimpleNode) o;
    return Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), comment);
  }
}
