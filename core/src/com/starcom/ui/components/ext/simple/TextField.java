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

import java.util.logging.Logger;

public class TextField extends Component {
    public static final int VK_LCONTROL = 0xA2;
    public static final int VK_CONTROL = 0x11;
    public static final int VK_LMENU = 0xA4;
    public static final int VK_DELETE = 0x2E;
    public static final int VK_SHIFT = 0x10;
    public static final int VK_V = 0x56;
    public static final int VK_C = 0x43;
    public static final int VK_X = 0x58;
    public static final int VK_LEFT = 0x25;
    public static final int VK_RIGHT = 0x27;
    public static final int VK_UP = 0x26;
    public static final int VK_DOWN = 0x28;
    public static final int VK_TAB = 0x09;
    String title;
    boolean doRender = true;
    Logger logger = java.util.logging.Logger.getLogger(TextField.class.getName());
    boolean renderCursor = false;
    boolean ctrlDown = false;
    boolean shiftDown = false;
    Point cursorLoc = new Point();
    Point markLoc = new Point();
    int cursorPos = 0;
    int markPos = 0;
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
        System.out.println("TextField: onAction: " + action.type); //TODO log this
        String newTitle = title;
        boolean updateView = false; // When title not has changed but cursor.
        boolean alsoSetMarker = true;
        if (action.type == Action.AType.MousePressed || action.type == Action.AType.MouseDragged)
        {
            System.out.println("TextField:CLICK"); //TODO: Clear this
            if (renderCursor)
            {
                setCursorLocation(action.x - xShift, action.y - yShift);

                if (action.type == Action.AType.MousePressed)
                {
                    markPos = cursorPos;
                    markLoc.set(cursorLoc.x, cursorLoc.y);
                }
            }
            else if (intersect(action.x + xShift, action.y + yShift))
            {
                FrameFactory.getKeyboard().show(this);
                renderCursor = true;
            }
        }
        else if (action.type == Action.AType.KeyTyped)
        {
            if (action.value == '\b')
            {
                if (cursorPos==0) { return true; }
                newTitle = actionDel();
            }
            else
            {
                newTitle = title.substring(0, cursorPos) + (char)action.value + title.substring(cursorPos);
                cursorPos++;
            }
        }
        else if (action.type == Action.AType.KeyPressed)
        {
            if (action.value == VK_LEFT)
            {
                if (cursorPos==0) { return true; }
                cursorPos--;
                updateView = true;
                if (shiftDown) { alsoSetMarker = false; }
            }
            else if (action.value == VK_RIGHT)
            {
                if (cursorPos>=title.length()) { return true; }
                cursorPos++;
                updateView = true;
                if (shiftDown) { alsoSetMarker = false; }
            }
            else if (action.value == VK_DELETE)
            {
                if (cursorPos==0) { return true; }
                newTitle = actionDel();
            }
            else if (action.value == VK_TAB)
            {
                newTitle = title.substring(0, cursorPos) + "  " + title.substring(cursorPos);
                cursorPos++;
                cursorPos++;
            }
            else if (action.value == VK_V && ctrlDown)
            {
                String clip = FrameFactory.getFrame().getClipboardString();
                newTitle = title.substring(0, cursorPos) + clip + title.substring(cursorPos);
                cursorPos += clip.length();
                System.out.println("Pasted: " + clip); //TODO: logging
            }
            else if (action.value == VK_C && ctrlDown)
            {
                int mark1 = cursorPos < markPos ? cursorPos : markPos;
                int mark2 = cursorPos > markPos ? cursorPos : markPos;
                String clip = title.substring(mark1,mark2);
                FrameFactory.getFrame().setClipboardString(clip);
            }
            else if (action.value == VK_X && ctrlDown)
            {
                int mark1 = cursorPos < markPos ? cursorPos : markPos;
                int mark2 = cursorPos > markPos ? cursorPos : markPos;
                if (mark1 != mark2)
                {
                    String clip = title.substring(mark1,mark2);
                    FrameFactory.getFrame().setClipboardString(clip);
                    newTitle = actionDel();
                }
            }
            if (action.value == VK_LCONTROL || action.value == VK_CONTROL)
            {
                ctrlDown = true;
            }
            else if (action.value == VK_SHIFT)
            {
                shiftDown = true;
            }
        }
        else if (action.type == Action.AType.KeyReleased)
        {
            if (action.value == VK_LCONTROL || action.value == VK_CONTROL)
            {
                ctrlDown = false;
            }
            else if (action.value == VK_SHIFT)
            {
                shiftDown = false;
            }
        }
        if (newTitle.equals(title) && !updateView) { return false; }
        setText(newTitle);
        setCursorLocation(cursorPos, alsoSetMarker);
        return true;
    }

    private String actionDel()
    {
        System.out.println("Delete idx: " + cursorPos); //TODO del this
        String newTitle;
        if (markPos == cursorPos)
        {
            newTitle = title.substring(0, cursorPos-1) + title.substring(cursorPos);
            cursorPos--;
        }
        else
        {
            int mark1 = cursorPos < markPos ? cursorPos : markPos;
            int mark2 = cursorPos > markPos ? cursorPos : markPos;
            newTitle = title.substring(0, mark1) + title.substring(mark2);
            if (cursorPos > markPos)
            {
              cursorPos -= mark2 - mark1;
            }
        }
        return newTitle;
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

    /** Sets the new position via character position.
     * Also updates markerPos as changed by char-value */
    public void setCursorLocation(int charPos, boolean alsoSetMarker)
    {
        this.cursorPos = charPos;
        if (alsoSetMarker) { this.markPos = charPos; }
        String cursorStr = title.substring(0,charPos);
        int newX = getFont().calcTextSize(cursorStr).x;
        cursorLoc.set(newX,0);
        if (alsoSetMarker) { markLoc.set(newX,0); }
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
        if (renderCursor)
        {
            int ySize = i.getSize().y;
            int x = absPosX + cursorLoc.x;
            int y = absPosY + cursorLoc.y;
            if (cursorPos < markPos)
            {
                int xx = absPosX + markLoc.x;
                g.drawFilledRect(Color.YELLOW, x, y, xx-x, y + ySize);
            }
            else if (cursorPos > markPos)
            {
                int xx = absPosX + markLoc.x;
                g.drawFilledRect(Color.YELLOW, xx, y, x-xx, y + ySize);
            }
            g.drawLine(Color.GRAY, 3, x, y, x, y + ySize);
        }
        g.drawImage(i, absPosX, absPosY);
        g.drawRect(Color.GRAY, 1, absPosX, absPosY, absPosX + getSize().x, absPosY + getSize().y);
        c.setShouldRender(false);
    }

    @Override
    public boolean intersect(int x, int y) {
        return Component.intersectComponent(this, x, y);
    }
}
