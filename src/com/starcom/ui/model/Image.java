package com.starcom.ui.model;

public interface Image
{
  public Point getSize();
  public Image getScaledInstance(Point newSize);
}
