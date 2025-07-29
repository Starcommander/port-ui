package com.starcom.ui.components;

import java.util.ArrayList;
import com.starcom.ui.frame.IFrame;

public abstract class Container extends Component
{
  ArrayList<Component> components = new ArrayList<>();

  public boolean onAction(String todo) //For components: Click, Touch/DnD, Key
  {
//TODO: Iterate components, return true if consumed
    return false;
  }
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

}
