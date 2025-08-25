package com.starcom.ui.components;

import java.util.Properties;

import com.starcom.ui.frame.IActionListener;
import com.starcom.ui.layout.ILayoutManager.ILayoutConf;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.render.IRenderer;
import com.starcom.ui.render.RenderSystem;

public abstract class Component
{
  Point size = new Point();
  Point location = new Point();
  Properties properties = new Properties();
  Container parent;
  ILayoutConf layoutConf;
  IActionListener al;

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
    if (parent != null)
    { // Check, is this component visible on container
      return parent.intersect(x, y);
    }
    return true;
  }
  public IRenderer getRenderer()
  {
    IRenderer r = RenderSystem.get().get(this.getClass());
    if (r == null) { r = getFallbackRenderer(); }
    return r;
  }
  /** @return Specific properties for use in renderers or layoutManagers */
  public Properties getProperties() { return properties; }
  public Container getParent() { return parent; }
  public void setParent(Container parent) { this.parent = parent; }
  /** Returns true, if render necessary, for example initial draw or something changed.
   * Should only be requested by Implementatin of IFrame.
   * @see com.starcom.ui.frame.impl.SwingFrame */
  public abstract boolean shouldRender();
  /** The renderer of each component must set this to false when finish.
   * The component itshelf can set this to true, if an action makes render necessary.
   * @param shouldRender The value if render is necessary. */
  public abstract void setShouldRender(boolean shouldRender);
  /** Returns the renderer, if no matching renderer is defined in RenderSystem 
   * @see com.starcom.ui.render.RenderSystem */
  public abstract IRenderer getFallbackRenderer();
  public abstract boolean onAction(Action action, int xShift, int yShift);
  public Point getSize() { return size; }
  public Point getPos() { return location; }
  public ILayoutConf getLayoutConf() { return layoutConf; }
  public void setLayoutConf(ILayoutConf layoutConf) { this.layoutConf = layoutConf; }
}
