package com.starcom.ui.frame;

import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

public abstract class Font
{
  public enum Style {Plain, Bolt, Italic};

  int size = 16;
  String fontFamily = getFonts()[0];
  Style style = Style.Plain;

  public abstract String[] getFonts();
  /** Should return an empty image, if string empty. */
  public abstract Image genTextImage(String txt, Color color);
  public abstract Point calcTextSize(String txt);

  public void setSize(int size) { this.size = size; }
  public void setFont(String fontFamily) { this.fontFamily = fontFamily; }
  public void setStyle(Style style) { this.style = style; }
  public int getSize() { return size; }
  public String getFont() { return fontFamily; }
  public Style getStyle() { return style; }
}
