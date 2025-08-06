package com.starcom.ui.model;

public class Color
{
  public static Color BLUE = new Color(0, 0, 255, 255);
  public static Color GRAY = new Color(50, 50, 50, 255);

  public int r;
  public int g;
  public int b;
  public int a;

  public Color(int r, int g, int b, int a)
  {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

}
