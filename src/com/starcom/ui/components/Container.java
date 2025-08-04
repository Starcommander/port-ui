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

  protected void renderComponents(IFrame frame)
  {
    for (Component c : components)
    {
      c.render(frame);
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
      if (c.onAction(action, getPos().x + xShift, getPos().y + yShift)) { return true; }
    }
    return false;
  }
  
  public void setActionListener(String todo) //Close, Click, Touch/DnD, Key
  {
    //TODO: on action execute it on component
  }
  public void addComponent(Component c)
  {
    components.add(c);
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

  /** Returns an IFrameRenderer for use in all containing components to render.
    * That may be a wrapper over raw IFrameRenderer of IFrame, but shifts x and y coordinates, or clips some area.
    * @return A wrapper, or IFrameRenderer of IFrame. */
  public abstract IFrameRenderer getInternalRenderer();
}
