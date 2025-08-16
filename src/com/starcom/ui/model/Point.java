package com.starcom.ui.model;

public class Point
{
  public int x;
  public int y;
  public Point() {}
  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public boolean equals(Point other)
  {
    if (x != other.x) { return false; }
    if (y != other.y) { return false; }
    return true;
  }
}
