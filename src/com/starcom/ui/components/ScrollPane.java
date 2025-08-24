package com.starcom.ui.components;

import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.PartialFrameGraphics;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Rect;
import com.starcom.ui.render.IRenderer;
import com.starcom.ui.model.Action.AType;
import com.starcom.ui.model.Color;

public class ScrollPane extends Container
{
    PartialFrameGraphics internalGraphics = new PartialFrameGraphics();
    Rect visibleRect = new Rect(0,0,0,0);
    boolean shouldRenderScrollpane = true;
    Point scrollPos = new Point(0,0);
    Point mouseDragPos = new Point(0,0);
    public ScrollPane(boolean hBar, boolean vBar)
    {
      if (hBar) throw new IllegalStateException("The hBar is currently not implemented in Scrollpane");
      if (!vBar) throw new IllegalStateException("The ScrollPane without vBar does not make sense");
    }

    public Rect getVisibleRect() { return visibleRect; }

    @Override
    public boolean shouldRender() {
      return shouldRenderScrollpane || shouldRenderComponents();
    }

    @Override
    public void setShouldRender(boolean shouldRenderScrollpane) {
      this.shouldRenderScrollpane = shouldRenderScrollpane;
    }

    public Point getScrollPos() { return scrollPos; }

    public PartialFrameGraphics getInternalGraphics() { return internalGraphics; }

    @Override
    public IRenderer getFallbackRenderer()
    {
      return (c,g,x,y) -> render((ScrollPane)c,g,x,y);
    }

    public static void render(ScrollPane c, IFrameGraphics frameGraphics, int xShift, int yShift) {
      c.getVisibleRect().set(c.getPos().x, c.getPos().y, c.getSize().x, c.getSize().y);
      c.getInternalGraphics().set(frameGraphics, c.getVisibleRect());
      Container.renderComponents(c, c.getInternalGraphics(), xShift - c.getScrollPos().x, yShift - c.getScrollPos().y);
      frameGraphics.drawRect(Color.BLUE, 1, c.getPos().x + xShift, c.getPos().y + yShift, c.getSize().x, c.getSize().y);

      float ratioY = c.getRatioY();
      if (ratioY > 1)
      { // Draw vertical scrollbar
        float scrollbarSize = 1/ratioY;
        scrollbarSize = ((float)c.getSize().y) * scrollbarSize;
        frameGraphics.drawRect(Color.GRAY, 1, c.getPos().x + xShift + c.getSize().x - 30, c.getPos().y + yShift, 30, c.getSize().y);
        c.getInternalGraphics().drawFilledRect(Color.GRAY, c.getPos().x + xShift + c.getSize().x - 30, c.getPos().y + yShift + (int)(((float)c.scrollPos.y) / ratioY), 30, (int)scrollbarSize);
      }
      c.setShouldRender(false);
    }

    Point getComponentsSize()
    {
      Point csize = new Point();
      for (Component c : components)
      {
        int xMax = c.getPos().x + c.getSize().x;
        if (csize.x < xMax) { csize.x = xMax; }
        int yMax = c.getPos().y + c.getSize().y;
        if (csize.y < yMax) { csize.y = yMax; }
      }
      return csize;
    }

    float getRatioY()
    {
      return ((float)getComponentsSize().y) / ((float)getSize().y);
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
      if (action.type == AType.MousePressed)
      {
        mouseDragPos.x = action.x;
        mouseDragPos.y = action.y;
      }
      if (action.type == AType.MouseDragged)
      {
        int toScrollX = mouseDragPos.x - action.x;
        int toScrollY = mouseDragPos.y - action.y;
        mouseDragPos.x = action.x;
        mouseDragPos.y = action.y;
        doScroll(toScrollX, toScrollY);
      }
      boolean consumed = onActionComponents(action, xShift + scrollPos.x, yShift + scrollPos.y);
      if (consumed) { return true; }
      if (action.type == AType.MouseScrolled)
      { //TODO: Check only for scroll actions that are inside
        doScroll(0, action.value);
        return true;
      }
      return false;
    }

    void doScroll(int x, int y)
    {
      boolean down = y > 0;
      int newVal = scrollPos.y + y;
      if (newVal < 0)
      { // Upper edge reached
        if (scrollPos.y != 0)
        {
          scrollPos.y = 0;
          shouldRenderScrollpane = true;
        }
      }
      else if (down && (getComponentsSize().y - scrollPos.y) < getSize().y) {} // Bottom edge reached
      else
      {
        scrollPos.y = newVal;
        shouldRenderScrollpane = true;
      }
      boolean right = x > 0;
      newVal = scrollPos.x + x;
      if (newVal < 0)
      { // Left edge reached
        if (scrollPos.x != 0)
        {
          scrollPos.x = 0;
          shouldRenderScrollpane = true;
        }
      }
      else if (right && (getComponentsSize().x - scrollPos.x) < getSize().x) {} // Right edge reached
      else
      {
        scrollPos.x = newVal;
        shouldRenderScrollpane = true;
      }
    }
}
