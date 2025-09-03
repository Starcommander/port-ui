package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;
import com.starcom.ui.render.IRenderer;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public class TextField extends Component {
    String title;
    boolean doRender = true;
    Logger logger = java.util.logging.Logger.getLogger(TextField.class.getName());
    boolean renderCursor = false;
    Point cursorLoc = new Point();
    int cursorPos = 0;
    Font font;

    public TextField(String title)
    {
        this.title = title;
    }

    public void setText(String title)
    {
        this.title = title;
        setShouldRender(true);
    }

    public Font getFont()
    {
        if (font == null) { font = FrameFactory.getFrame().getGraphicsImpl().newFont(); }
        return font;
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
        if (action.type == Action.AType.MouseClicked)
        {
            System.out.println("TextField:CLICK"); //TODO: Clear this
            if (intersect(action.x + xShift, action.y + yShift))
            {
                FrameFactory.getKeyboard().show(this);
            }
        }
        //TODO: KeyEvents and MouseMark or cursor
        else if (action.type == Action.AType.KeyTyped)
        {
            String newTitle = title;
            if (action.value == '\b')
            {
                if (cursorPos==0) { return true; }
                newTitle = title.substring(0, cursorPos-1) + title.substring(cursorPos);
                cursorPos--;
            }
            else
            {
                newTitle = title.substring(0, cursorPos) + (char)action.value + title.substring(cursorPos);
                cursorPos++;
            }
            setText(newTitle);
            setCursorLocation(cursorPos);
        }
        return false;
    }

    /** Calculates the new position via coordinates. */
    public void setCursorLocation(int x, int y)
    {
        StringBuilder sb = new StringBuilder();
        Point p = new Point();
        cursorPos = 0;
        for (char c : title.toCharArray())
        {
            cursorPos++;
            sb.append(c);
            p = getFont().calcTextSize(sb.toString());
            if (p.x >= x) { break; }
        }
        cursorLoc.set(p.x,0);
        renderCursor = true;
        setShouldRender(true);
    }

    /** Sets the new position via character position. */
    public void setCursorLocation(int charPos)
    {
        this.cursorPos = charPos;
        String cursorStr = title.substring(0,charPos);
        int newX = getFont().calcTextSize(cursorStr).x;
        cursorLoc.set(newX,0);
    }

    /** Returns the character position. */
    public int getCursorLocation() { return cursorPos; }

    public void setCursorVisible(boolean visible)
    {
        renderCursor = visible;
        setShouldRender(true);
    }

    @Override
    public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> render(c, g, x, y);
    }

    private void render(Component c, IFrameGraphics g, int xShift, int yShift)
    {
        int absPosX = c.getPos().x + xShift;
        int absPosY = c.getPos().y + yShift;
        Image i = getFont().genTextImage(title, Color.BLUE);
        g.drawImage(i, absPosX, absPosY);
        g.drawRect(Color.GRAY, 1, absPosX, absPosY, absPosX + getSize().x, absPosY + getSize().y);
        if (renderCursor)
        {
            int ySize = i.getSize().y;
            g.drawLine(Color.GRAY, 3, absPosX + cursorLoc.x, absPosY + cursorLoc.y, absPosX + cursorLoc.x, absPosY + cursorLoc.y + ySize);
        }
        c.setShouldRender(false);
    }

    @Override
    public boolean intersect(int x, int y) {
        return Component.intersectComponent(this, x, y);
    }
}
