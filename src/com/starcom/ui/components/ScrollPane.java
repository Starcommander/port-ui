package com.starcom.ui.components;

import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.PartialFrameRenderer;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Rect;

public class ScrollPane extends Container
{
    PartialFrameRenderer renderer;
    boolean shouldRenderScrollpane = true;
    public ScrollPane(boolean hBar, boolean vBar)
    {
      if (vBar) throw new IllegalStateException("The vBar is currently not implemented in Scrollpane");
      if (!hBar) throw new IllegalStateException("The ScrollPane without hBar does not make sense");
      IFrameRenderer parentRenderer;
      if (parent != null) { parentRenderer = parent.getInternalRenderer(); }
      else { parentRenderer = FrameFactory.getFrame().getRenderer(); }
      Rect visibleRect = new Rect(getPos().x, getPos().y, getSize().x, getSize().y);
      renderer = new PartialFrameRenderer(parentRenderer, visibleRect); //TODO: Update visibleRect
    }

    @Override
    public IFrameRenderer getInternalRenderer()
    {
      return renderer;
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
    public void render(IFrame frame) {
      shouldRenderScrollpane = false;
      
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'onAction'");
    }
}
