package com.starcom.ui.components;

import java.util.ArrayList;

import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.layout.ILayoutManager;
import com.starcom.ui.layout.NullLayout;
import com.starcom.ui.layout.ILayoutManager.ILayoutConf;
import com.starcom.ui.model.Action;

public abstract class Container extends Component
{
  ArrayList<Component> components = new ArrayList<>();
  ILayoutManager layoutManager = new NullLayout();

  /**
   * Renders the containing components.
   * @param c The container of which to render his components.
   * @param frameGraphics The graphics.
   * @param xShift The xShift got from render function (do not add pos.x)
   * @param yShift The yShift got from render function (do not add pos.y) */
  public static void renderComponents(Container c, IFrameGraphics frameGraphics, int xShift, int yShift)
  {
    for (Component child : c.components.toArray(new Component[0]))
    {
      child.getRenderer().render(child, frameGraphics, c.getPos().x + xShift, c.getPos().y + yShift);
    }
  }
  public boolean shouldRenderComponents() {
    for (Component c : components.toArray(new Component[0]))
    {
        if (c.shouldRender()) { return true; }
    }
    return false;
  }
  public boolean onActionComponents(Action action, int xShift, int yShift) {
    for (Component c : components.toArray(new Component[0]))
    {
      if (c.onAction(action, xShift - getPos().x, yShift - getPos().y)) { return true; }
    }
    return false;
  }

  /** Adds the component.
   * @param c The component to add.
   * @param layoutConf The layoutConf that belongs to current LayoutManager, may be null if LayoutManager is NullLayout.
   */
  public void addComponent(Component c, ILayoutConf layoutConf)
  {
    if (c.getParent() != null) { throw new IllegalStateException("Component already inserted into other container."); }
    if (c == this) { throw new IllegalStateException("Cannot insert same container into itshelf."); }
    layoutManager.checkLayoutConf(layoutConf);

    components.add(c);
    c.setLayoutConf(layoutConf);
    c.setParent(this);
  }
  public boolean removeComponent(Component c)
  {
    boolean has = components.remove(c);
    if (has) { c.parent = null; }
    return has;
  }
  public ArrayList<Component> getComponents() { return components; }
  public void clearComponents()
  {
    for (Component c : components.toArray(new Component[0]))
    {
      removeComponent(c);
    }
  }
  public void setLayoutManager(ILayoutManager layoutManager)
  {
    this.layoutManager = layoutManager;
  }
  public ILayoutManager getLayoutManager() { return layoutManager; }

}
