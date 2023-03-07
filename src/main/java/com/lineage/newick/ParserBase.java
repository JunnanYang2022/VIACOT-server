package com.lineage.newick;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public abstract class ParserBase<T extends Node<T>> {
  protected static final char QUOTE = '\'';
  final private BufferedReader r;
  final protected StringBuilder text = new StringBuilder();
  final private LinkedList<T> parents = new LinkedList<>();
  protected T curr;
  protected boolean inQuote;
  protected boolean inLength;
  protected boolean inComment;
  protected boolean stop;
  protected int position = 0;
  protected char last;
  protected char c = '^';

  public ParserBase(InputStream stream) {
    this(new InputStreamReader(stream, StandardCharsets.UTF_8));
  }

  public ParserBase(Reader reader) {
    this.r = new BufferedReader(reader);
  }

  // (B,(A,C,E)G,D)F;
  public T parse() throws IOException, IllegalArgumentException {
    try {
      int intch;
      curr = newNode(); // root node
      while (!stop && (intch = r.read()) != -1) {
        position++;
        last = c;
        c = (char) intch;

        // single quote
        if (c == QUOTE) {
          if (inQuote) {
            // escaped quote ?
            if (last == QUOTE) {
              // two subsequent quotes are the escape sequence for a single quote
              text.append(QUOTE);
              c=' '; // reset to avoid another pair with next quote
            }
            continue;
          } else if (last != QUOTE) {
            inQuote = true; // start new quoting
            c = ' '; // hide that we just had a quote
            continue;
          }
        } else if (inQuote) {
          if (last == QUOTE) {
            // the last quote was an ending quote
            inQuote = false;
          } else {
            appendText();
            continue;
          }
        }

        // ignore special chars inside a comment
        if (inComment && c != '[' && c != ']') {
          appendText();
          continue;
        }

        // whitespace
        if (Character.isWhitespace(c)) {
          continue;
        }

        // we deal with all reserved chars but quote & whitespace here
        switch (c) {
          case '(':
            parents.add(curr);
            newCurr();
            break;
          case ')':
            applyText2curr();
            curr = parents.removeLast();
            break;
          case ',':
            applyText2curr();
            newCurr();
            break;
          case ':':
            applyText2curr();
            inLength = true;
            break;
          case '[':
            applyText2curr();
            inComment = true;
            break;
          case ']':
            if (!inComment) {
              throw new IllegalArgumentException("Closing comment found at position "+position+", but not in comment");
            }
            applyText2curr();
            inComment = false;
            break;
          case ';':
            // we stop parsing at the end of tree marker, even if there is more text which we will ignore
            stop = true;
            applyText2curr();
            break;
          default:
            appendText();
        }
      }
    } catch (IllegalArgumentException e){
      throw e;
    } catch (RuntimeException e){
      throw new IllegalArgumentException("Failed to parse on position "+position, e);
    } finally {
      r.close();
    }

    // make sure we have an empty stack - otherwise the file was invalid!
    if (!parents.isEmpty()) {
      throw new IllegalArgumentException("Missing closing tag (");
    }
    if (inComment) {
      throw new IllegalArgumentException("Missing comments closing tag");
    }
    if (inQuote) {
      throw new IllegalArgumentException("Missing comments single quote");
    }
    return curr;
  }

  abstract protected void parseComments(String comment);

  abstract protected T newNode();

  public void appendText() {
    text.append(c);
  }

  public void newCurr() {
    curr = newNode();
    parents.getLast().addChild(curr);
  }

  private void applyText2curr() {
    if (text.length() > 0) {
      if (inLength) {
        Double len = Double.parseDouble(text.toString());
        curr.setLength(len);
      } else if (inComment) {
        parseComments(text.toString());
      } else {
        curr.setLabel(text.toString());
      }
      text.setLength(0);
    }
    inLength = false;
  }
}
