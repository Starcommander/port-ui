package com.starcom.ui.frame;

import com.starcom.ui.model.Point;

public interface Image
{
  public Point getSize();
  public Image getScaledInstance(Point newSize);
}
