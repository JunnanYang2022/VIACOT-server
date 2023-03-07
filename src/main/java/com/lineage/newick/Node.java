package com.lineage.newick;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Node<T extends Node<T>> {
  private static final String WS_REPLACEMENT = "_";
  static final Pattern QUOTE = Pattern.compile("'");
  private static final Pattern WHITESPACE = Pattern.compile("\\s");
  private static final Pattern RESERVED = Pattern.compile("[()\\[\\],:;']");

  private String label;
  private Double length;
  private List<T> children;

  public Node() {
  }

  public Node(String label) {
    this.label = label;
  }

  public Node(String label, Double length) {
    this.label = label;
    this.length = length;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public List<T> getChildren() {
    return children;
  }

  public void setChildren(List<T> children) {
    this.children = children;
  }

  public void addChild(T child) {
    if (children == null) {
      children = new ArrayList<>();
    }
    children.add(child);
  }

  public boolean hasLength() {
    return length != null;
  }

  /**
   * Prints the tree in simple Newick format.
   * @param w writer to print to
   */
  public void printTree(Writer w) throws IOException {
    print(w);
    w.append(";");
  }

  void print(Writer w) throws IOException {
    if (children != null && !children.isEmpty()) {
      w.append("(");
      boolean first = true;
      for (T c : children) {
        if (!first) {
          w.append(",");
        }
        c.print(w);
        first=false;
      }
      w.append(")");
    }
    w.append(escape(label));
    if (hasLength()) {
      w.append(":");
      w.append(String.valueOf(length));
    }
    writeComments(w);
  }

  /**
   * Generate comments per node.
   */
  abstract void writeComments(Writer w) throws IOException;

  /**
   * Potentially quotes a string to be Newick compliant.
   * If the string contains characters reserved by the Newick grammar it will be quoted in single quotes and any existing quotes escaped by doubling them.
   * @param value to escape
   */
  public static String escape(String value) {
    if (value == null) {
      return "";

    } else {
      // need for quoting?
      if (RESERVED.matcher(value).find()) {
        return "'" + QUOTE.matcher(value).replaceAll("''") + "'";
      } else {
        Matcher ws = WHITESPACE.matcher(value);
        if (ws.find()) {
          return ws.replaceAll(WS_REPLACEMENT);
        }
      }
      return value;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Node)) return false;
    Node node = (Node) o;
    return Objects.equals(label, node.label) && Objects.equals(length, node.length);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, length);
  }
}
