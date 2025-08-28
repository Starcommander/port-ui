package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.render.IRenderer;

import java.io.InputStream;
import java.util.logging.Logger;

public class Label extends Component {
    String title;
    Font font;
    Image image;
    Color backgroundColor; // May be null
    String trimmedTitle;
    int trimmedWidth;
    boolean doRender = true;
    static Logger logger = java.util.logging.Logger.getLogger(Label.class.getName());

    public Label(String title)
    {
        this.title = title;
        initFont();
    }

    public Label(Image image)
    {
        this.image = image;
        getSize().x = image.getSize().x;
        getSize().y = image.getSize().y;
    }

    public static Label fromResource(String resource)
    {
        InputStream s = Label.class.getResourceAsStream(resource);
        return new Label(FrameFactory.getFrame().getGraphicsImpl().loadImage(s));
    }

    private void initFont()
    {
        if (font != null) { return; }
        font = FrameFactory.getFrame().getGraphicsImpl().newFont();
    }

    public void setText(String title)
    {
        this.title = title;
        initFont();
        setShouldRender(true);
    }
    public String getText() { return title; }

    public void setImage(Image image)
    {
        this.image = image;
        setShouldRender(true);
    }
    public Image getImage() { return image; }
    /** Returns the font in case a text is set, otherwise null. */
    public Font getFont() { return font; }

    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
        setShouldRender(true);
    }

    @Override
    public IRenderer getFallbackRenderer()
    {
        return (c,g,x,y) -> render(c,g,x,y);
    }

    private static void render(Component c, IFrameGraphics frameGraphics, int xShift, int yShift) {
        Label l = (Label)c;
        frameGraphics.drawRect(Color.BLUE, 1, l.getPos().x + xShift, l.getPos().y + yShift, l.getSize().x, l.getSize().y);
        logger.fine("Start render button");

        if (l.getBackgroundColor() != null)
        {
            frameGraphics.drawFilledRect(l.getBackgroundColor(), l.getPos().x + xShift, l.getPos().y + yShift, l.getSize().x, l.getSize().y);
        }
        renderText(l, l.getFont(), l.title, frameGraphics, xShift, yShift);
        renderImage(l, l.image, frameGraphics, xShift, yShift);
        c.setShouldRender(false);
    }

    /** Draws the Text into center of component, and trimms the text if necessary. */ 
    public static void renderText(Component c, Font f, String title, IFrameGraphics frameGraphics, int xShift, int yShift)
    {
        if (title == null) { return; }
        Color col = new Color(0, 0, 255, 255);
        Image fImg;
        Integer trimmedWidth = (Integer)c.getProperties().get(Button.class + ".TrimTextWidth");
        String trimmedTitle = (String)c.getProperties().get(Button.class + ".TrimTextTitle");
        if (trimmedTitle != null && trimmedWidth == c.getSize().x)
        {
            fImg = f.genTextImage(trimmedTitle, col);
        }
        else if (c.getSize().x >= f.calcTextSize(title).x)
        {
            fImg = f.genTextImage(title, col);
        }
        else
        {
            trimmedWidth = c.getSize().x;
            trimmedTitle = title;
            for (int i=1; i<title.length(); i++)
            {
                trimmedTitle = title.substring(0, title.length()-i) + "...";
                int w = f.calcTextSize(trimmedTitle).x;
                if (c.getSize().x > w)
                {
                    break;
                }
            }
            fImg = f.genTextImage(trimmedTitle, col);
            c.getProperties().put(Button.class + ".TrimTextWidth", trimmedWidth);
            c.getProperties().put(Button.class + ".TrimTextTitle", trimmedTitle);
        }
        int x = (c.getSize().x - fImg.getSize().x) /2;
        x = x + c.getPos().x;
        int y = c.getPos().y + (c.getSize().y/2) - (fImg.getSize().y/2);
//        if (image == null) { y = b.getPos().y + (b.getSize().y/2) - (fImg.getSize().y/2); }
//        else { y = b.getPos().y + b.getSize().y -1 - fImg.getSize().y; }
        frameGraphics.drawImage(fImg, x + xShift, y + yShift);
    }

    /** Renders the image into center of component. */
    public static void renderImage(Component c, Image image, IFrameGraphics frameGraphics, int xShift, int yShift)
    {
        if (image == null) { return; }
        int x = (c.getSize().x - image.getSize().x) /2;
        x = x + c.getPos().x;
        int y = c.getPos().y + (c.getSize().y/2) - (image.getSize().y/2);
        frameGraphics.drawImage(image, x + xShift, y + yShift);
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
    public boolean intersect(int x, int y) {
        return Component.intersectComponent(this, x, y);
    }
}
