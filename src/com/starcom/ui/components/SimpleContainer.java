package com.starcom.ui.components;

import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.model.Action;
import com.starcom.ui.render.IRenderer;

public class SimpleContainer extends Container {
    boolean shouldRender = false; //SimpleContainer itshelf has nothing do draw

    @Override
    public boolean shouldRender() {
        return shouldRenderComponents() || shouldRender;
    }

    @Override
    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
        return onActionComponents(action, xShift, yShift);
    }

    @Override
    public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> render((Container)c,g,x,y);
    }

    private static void render(Container c, IFrameGraphics g, int xShift, int yShift)
    {
        Container.renderComponents(c, g, xShift, yShift);
        c.setShouldRender(false);
    }
}
