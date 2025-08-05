package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.PartialFrameRenderer;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Rect;
import com.starcom.ui.model.Action.AType;

public class ScrollPane extends Container
{
    PartialFrameRenderer internalRenderer = new PartialFrameRenderer();
    Rect visibleRect = new Rect(0,0,0,0);
    boolean shouldRenderScrollpane = true;
    Point scrollPos = new Point(0,0);
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
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
      boolean consumed = onActionComponents(action, xShift + scrollPos.x, yShift + scrollPos.y);
      if (consumed) { return true; }
      if (action.type == AType.MouseScrolled)
      { //TODO: Check only for scroll actions that are inside
        scrollPos.y = scrollPos.y - action.value;
        shouldRenderScrollpane = true;
        return true;
      }
      return false;
    }
}
