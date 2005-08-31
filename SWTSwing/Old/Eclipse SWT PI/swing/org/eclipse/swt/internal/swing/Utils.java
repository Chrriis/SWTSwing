/*
 * @(#)Utils.java
 * 
 * Swing port: Christopher Deckers (chrriis@brainlex.com)
 * http://chrriis.brainlex.com/swtswing
 */
package org.eclipse.swt.internal.swing;

public class Utils {

  private Utils() {}

  protected static final String getReplacementString(String s) {
    if(s.equals("<")) {
      return "&lt;";
    } else if(s.equals(">")) {
      return "&gt;";
    } else if(s.equals("&")) {
      return "&amp;";
    } else if(s.equals("\n")) {
      return "<br>";
    } else if(s.equals("\t")) {
      return " &nbsp;&nbsp;&nbsp;&nbsp;";
    }
    return "";
  }

  protected static java.util.regex.Pattern pattern = null;

  /**
   * Escape an HTML string.
   * @param s The string to escape.
   * @return The escaped string.
   */
  public static final String escapeHTML(String s){
    if(s == null) {
      return null;
    }
    if(pattern == null) {
      pattern = java.util.regex.Pattern.compile("<|>|&|\n|\t");
    }
    java.util.regex.Matcher m = pattern.matcher(s);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, "");
      sb.append(getReplacementString(m.group()));
    }
    m.appendTail(sb);
    return sb.toString();
  }

}