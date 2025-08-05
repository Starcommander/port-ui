package com.starcom.ui.components;

import java.util.ArrayList;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.model.Action;

public abstract class Container extends Component
{
  ArrayList<Component> components = new ArrayList<>();

  public void layout()
  {
//TODO: Layout all components
  }
  protected void renderComponents(IFrame frame, IFrameRenderer frameRenderer, int xShift, int yShift)
  {
    for (Component c : components)
    {
      c.render(frame, frameRenderer, getPos().x + xShift, getPos().y + yShift);
    }
  }
  public boolean shouldRenderComponents() {
    for (Component c : components)
    {
        if (c.shouldRender()) { return true; }
    }
    return false;
  }
  public boolean onActionComponents(Action action, int xShift, int yShift) {
    for (Component c : components)
    {
      if (c.onAction(action, xShift - getPos().x, yShift - getPos().y)) { return true; }
    }
    return false;
  }

  public void setActionListener(String todo) //Close, Click, Touch/DnD, Key
  {
    //TODO: on action execute it on component
  }
  public void addComponent(Component c)
  {
    if (c.getParent() != null) { throw new IllegalStateException("Component already inserted into other container."); }
    if (c == this) { throw new IllegalStateException("Cannot insert same container into itshelf."); }
    components.add(c);
    c.setParent(this);
  }
  public boolean clearComponent(Component c)
  {
    return components.remove(c);
  }
  public ArrayList<Component> getComponents() { return components; }
  public void setLayoutManager(String todo)
  {
    //TODO
  }

}
