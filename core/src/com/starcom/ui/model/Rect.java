package com.starcom.ui.model;

public class Rect
{
  public Point pos;
  public Point size;

  public Rect()
  {
    pos = new Point();
    size = new Point();
  }

  public Rect(int x, int y, int w, int h)
  {
    pos = new Point(x,y);
    size = new Point(w,h);
  }

  public void set(int x, int y, int w, int h)
  {
    pos.x = x;
    pos.y = y;
    size.x = w;
    size.y = h;
  }

  public boolean equals(Rect other)
  {
    return pos.equals(other.pos) && size.equals(other.size);
  }

  @Override
  public String toString()
  {
    return super.toString() + " X=" + pos.x + " Y=" + pos.y + " W=" + size.x + " H=" + size.y;
  }
}
