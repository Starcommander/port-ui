package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.PartialFrameRenderer;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Rect;
import com.starcom.ui.model.Action.AType;
import com.starcom.ui.model.Color;

public class ScrollPane extends Container
{
    PartialFrameRenderer internalRenderer = new PartialFrameRenderer();
    Rect visibleRect = new Rect(0,0,0,0);
    boolean shouldRenderScrollpane = true;
    Point scrollPos = new Point(0,0);
    Point mouseDragPos = new Point(0,0);
    public ScrollPane(boolean hBar, boolean vBar)
    {
      if (hBar) throw new IllegalStateException("The hBar is currently not implemented in Scrollpane");
      if (!vBar) throw new IllegalStateException("The ScrollPane without vBar does not make sense");
    }

    @Override
    public boolean shouldRender() {
      return shouldRenderScrollpane || shouldRenderComponents();
    }

    @Override
    public void setShouldRender() {
      shouldRenderScrollpane = true;
    }

    @Override
    public void render(IFrame frame, IFrameRenderer frameRenderer, int xShift, int yShift) {
      shouldRenderScrollpane = false;
      visibleRect.set(getPos().x, getPos().y, getSize().x, getSize().y);
      internalRenderer.set(frameRenderer, visibleRect);
      renderComponents(frame, internalRenderer, xShift - scrollPos.x, yShift - scrollPos.y);

      frameRenderer.drawRect(Color.BLUE, 1, getPos().x + xShift, getPos().y + yShift, getSize().x, getSize().y);
      Point componentsSize = getComponentsSize();
      float ratioY = getRatioY(componentsSize);
      if (ratioY > 1)
      { // Draw vertical scrollbar
        float scrollbarSize = 1/ratioY;
        scrollbarSize = ((float)getSize().y) * scrollbarSize;
        frameRenderer.drawRect(Color.GRAY, 1, getPos().x + xShift + getSize().x - 30, getPos().y + yShift, 30, getSize().y);
        internalRenderer.drawFilledRect(Color.GRAY, getPos().x + xShift + getSize().x - 30, getPos().y + yShift + scrollPos.y, 30, (int)scrollbarSize);
      }
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

    float getRatioY(Point componentsSize)
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
      //TODO: implement x
      boolean down = y > 0;
      int newVal = scrollPos.y + y;
      if (newVal < 0) {} // Upper edge reached
      else if (down && (getComponentsSize().y - scrollPos.y) < getSize().y) {} // Bottom edge reached
      else
      {
        scrollPos.y = newVal;
        shouldRenderScrollpane = true;
      }
      boolean right = x > 0;
      newVal = scrollPos.x + x;
      if (newVal < 0) {} // Left edge reached
      else if (right && (getComponentsSize().x - scrollPos.x) < getSize().x) {} // Right edge reached
      else
      {
        scrollPos.x = newVal;
        shouldRenderScrollpane = true;
      }
    }
}
