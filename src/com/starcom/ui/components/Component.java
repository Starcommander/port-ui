package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.model.Point;

public abstract class Component
{
  Point size = new Point();
  Point location = new Point();
  Container parent;
  public void attachTo(Container container)
  {
    parent = container;
  }
  public Container getParent() { return parent; }
  /** Returns true, if render necessary, for example initial draw or something changed. */
  public abstract boolean shouldRender();
  public abstract void setShouldRender();
  public abstract void render(IFrame frame);
  public Point getSize() { return size; }
  public Point getPos() { return location; }
}
