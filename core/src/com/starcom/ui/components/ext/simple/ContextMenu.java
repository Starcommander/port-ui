package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;
import com.starcom.ui.components.ScrollPane;
import com.starcom.ui.components.RootContainer;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.ISelectedListener;
import com.starcom.ui.layout.SingleLayout;
import com.starcom.ui.layout.VBox;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;
import com.starcom.ui.render.IRenderer;

public class ContextMenu extends Container{
    boolean shouldRender = true;
    Component focusComponent;
    Runnable onHide;

    public ContextMenu()
    {
    }

    public ContextMenu(ISelectedListener subListener, String... entries)
    {
        setLayoutManager(new SingleLayout());
        ScrollPane sp = new ScrollPane(false, true);
        sp.setLayoutManager(new VBox());
        int w = 30;
        int h = 0;
        for (String entry : entries)
        {
            Button b = genButton(entry, subListener);
            Point txtSize = b.getFont().calcTextSize(entry);
            if (w < txtSize.x) { w = txtSize.x; }
            h += txtSize.y + 5;
            sp.addComponent(b, new VBox.VBoxConf(16));
        }
        if (h > FrameFactory.getFrame().getMaxSize().y) { h =  FrameFactory.getFrame().getMaxSize().y;}
        getSize().x = w;
        getSize().y = h;
        addComponent(sp, null);
    }

    public void setOnHide(Runnable onHide) { this.onHide = onHide; }

    /** Set an extra focus component.
     * @see com.starcom.ui.keyboard.SoftKeyboard
     * @param focusComponent The focusComponent that should not close the context menu. */
    public void setFocusComponent(Component focusComponent)
    {
        this.focusComponent = focusComponent;
    }

    public Component getFocusComponent() { return focusComponent; }

    private Button genButton(String title, ISelectedListener subListener)
    {
        Button b = new Button(title);
        b.setActionListener((a,x,y) -> { subListener.selected(title); hide(); return true; });
        return b;
    }

    @Override
    public boolean shouldRender() {
        return shouldRenderComponents() || shouldRender;
    }

    @Override
    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    @Override
    public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> renderContextMenu((ContextMenu)c,g,x,y);
    }

    private static void renderContextMenu(ContextMenu menu, IFrameGraphics g, int xShift, int yShift)
    {
        g.drawFilledRect(Color.GRAY_BRIGHT_BRIGHT, menu.getPos().x + xShift, menu.getPos().y + yShift, menu.getSize().x, menu.getSize().y);
        renderComponents(menu,g,xShift,yShift);
        menu.setShouldRender(false);
    }

    @Override
    public boolean intersect(int x, int y)
    { // Always true, as contextMenu is on top.
        return true;
    }

    public void hide()
    {
        if (onHide != null) { onHide.run(); }
        RootContainer c = (RootContainer)getParent();
        c.setShouldRender(true);
        c.setContextMenu(null);
        setFocusComponent(null);
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift)
    {
        if (!Component.intersectComponent(this, action.x + xShift, action.y + yShift))
        {
            if (!Component.intersectComponent(focusComponent, action.x + xShift, action.y + yShift))
            {
                if (action.type == Action.AType.MouseClicked) { hide(); }
                return true;
            }
        }
        if (this.getActionListener() != null)
        {
            boolean consumed = this.getActionListener().onAction(action, xShift - getPos().x, yShift - getPos().y);
            if (consumed) { return true; }
        }
        return onActionComponents(action, xShift, yShift);
    }
    
}
