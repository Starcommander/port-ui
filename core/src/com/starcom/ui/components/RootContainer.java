package com.starcom.ui.components;

import com.starcom.ui.components.ext.simple.ContextMenu;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.render.IRenderer;

public class RootContainer extends Container {
    boolean shouldRender = true;
    ContextMenu menu;

    @Override
    public boolean shouldRender() {
        if (shouldRender) { return true; }
        if (menu != null && menu.shouldRender()) { return true; }
        return shouldRenderComponents();
    }

    @Override
    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
        return onActionMenu(action, xShift, yShift) || onActionComponents(action, xShift, yShift);
    }

    @Override
    public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> render((RootContainer)c,g,x,y);
    }

    private static void render(RootContainer c, IFrameGraphics g, int xShift, int yShift)
    {
        Container.renderComponents(c, g, xShift, yShift);
        renderContextMenu(c.menu, g, xShift, yShift);
        c.setShouldRender(false);
    }

    @Override
    public boolean intersect(int x, int y)
    { // Always true on this root-container.
        return true;
    }

    @Override
    public Point getSize()
    {
        return new Point(FrameFactory.getFrame().getSize().x, FrameFactory.getFrame().getSize().y);
    }

    public void setContextMenu(ContextMenu menu)
    {
        if (this.menu != null)
        {
            this.menu.setParent(null);
        }
        if (menu != null)
        {
            menu.setParent(this);
        }
        this.menu = menu;
    }
    
    private boolean onActionMenu(Action action, int xShift, int yShift)
    {
        if (menu == null) { return false; }
        return menu.onAction(action, xShift, yShift);
    }

    private static void renderContextMenu(ContextMenu menu, IFrameGraphics g, int xShift, int yShift) {
        if (menu == null) { return; }
        menu.getLayoutManager().doLayout(menu);
        menu.getLayoutManager().doLayoutSub(menu);
        menu.getFallbackRenderer().render(menu, g, xShift, yShift);
    }
}
