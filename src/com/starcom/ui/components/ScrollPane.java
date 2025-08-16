package com.starcom.ui.components;

public class ScrollPane extends Container
{
    PartialFrameRenderer renderer;
    public ScrollPane(boolean hBar, boolean vBar)
    {
      if (vBar) throw new IllegalStateException("The vBar is currently not implemented in Scrollpane");
      if (!hBar) throw new IllegalStateException("The ScrollPane without hBar does not make sense");
      IFrameRenderer parentRenderer;
      if (parent != null) { parentRenderer = parent.getInternalRenderer(); }
      else { parentRenderer = FrameFactory.getFrame().getRenderer(); }
      Rect visibleRect = new Rect(pos.x, pos.y, size.x, size.y);
      renderer = new PartialFrameRenderer(parentRenderer, visibleRect); //TODO: Update visibleRect
    }

    @Override
    public IFrameRenderer getInternalRenderer()
    {
      return FrameFactory.getFrame().getRenderer();
    }
}
