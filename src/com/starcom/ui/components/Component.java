package com.starcom.ui.components;

import com.starcom.ui.frame.IActionListener;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;

public abstract class Component
{
  Point size = new Point();
  Point location = new Point();
  Container parent;
  IActionListener al;

  public void attachTo(Container container)
  {
    parent = container;
  }

  /** Sets the IActionListener that can be used by implementing class. */
  public void setActionListener(IActionListener al) { this.al = al; }
  public IActionListener getActionListener() { return al; }

  /** @return true, if coordinate is inside of this component. */
  public boolean intersect(int x, int y)
  {
    if (x < getPos().x) { return false; }
    if (y < getPos().y) { return false; }
    if (x > (getPos().x + getSize().x)) { return false; }
    if (y > (getPos().y + getSize().y)) { return false; }
    return true;
  }
  public Container getParent() { return parent; }
  /** Returns true, if render necessary, for example initial draw or something changed. */
  public abstract boolean shouldRender();
  public abstract void setShouldRender();
  public abstract void render(IFrame frame);
  public abstract boolean onAction(Action action, int xShift, int yShift);
  public Point getSize() { return size; }
  public Point getPos() { return location; }
}
