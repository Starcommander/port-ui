package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.render.IRenderer;

import java.util.logging.Logger;

public class TextField extends Component {
    String title;
    int trimmedWidth;
    boolean doRender = true;
    Logger logger = java.util.logging.Logger.getLogger(TextField.class.getName());

    public TextField(String title)
    {
        this.title = title;
    }

    public void setText(String title)
    {
        this.title = title;
        setShouldRender(true);
    }

    @Override
    public boolean shouldRender() {
        return doRender;
    }

    @Override
    public void setShouldRender(boolean shouldRender) {
        doRender = shouldRender;
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
        return false;
    }

    @Override
    public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> render(c, g, x, y);
    }

    private void render(Component c, IFrameGraphics g, int xShift, int yShift)
    {
        Font f = g.newFont();
        Image i = f.genTextImage(title, Color.BLUE);
        g.drawImage(i, c.getPos().x + xShift, c.getPos().y + yShift);
        c.setShouldRender(false);
    }

    @Override
    public boolean intersect(int x, int y) {
        return Component.intersectComponent(this, x, y);
    }
}
