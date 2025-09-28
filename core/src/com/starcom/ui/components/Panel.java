package com.starcom.ui.components;

import com.starcom.ui.model.Action;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.render.IRenderer;
import com.starcom.ui.model.Color;

public class Panel extends Container
{
    boolean doRender = true;
    Color frameColor = Color.GRAY;
    Color fillColor = Color.GRAY_BRIGHT_BRIGHT;

    @Override
    public boolean onAction(Action a, int xShift, int yShift)
    {
      return onActionComponents(a,xShift,yShift);
    }

    @Override
    public IRenderer getFallbackRenderer()
    {
        return (c,g,x,y) -> render(c,g,x,y);
    }

    private static void render(Component c, IFrameGraphics g, int xShift, int yShift)
    {
      g.drawRect(((Panel)c).frameColor, 2, c.getPos().x + xShift, c.getPos().y + yShift, c.getSize().x, c.getSize().y);
      g.drawFilledRect(((Panel)c).fillColor, c.getPos().x + xShift, c.getPos().y + yShift, c.getSize().x, c.getSize().y);
    }

    @Override
    public boolean shouldRender() {
        return doRender || shouldRenderComponents();
    }

    @Override
    public void setShouldRender(boolean doRender) {
        this.doRender = doRender;
    }

    @Override
    public boolean intersect(int x, int y) {
        return Component.intersectComponent(this, x, y);
    }

    public void setColor(Color frameColor, Color fillColor)
    {
      this.frameColor = frameColor;
      this.fillColor = fillColor;
    }
}
