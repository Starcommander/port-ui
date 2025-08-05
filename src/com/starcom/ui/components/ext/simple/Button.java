package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;

import java.util.logging.Logger;

public class Button extends Component {
    String title;
    Image image;
    String trimmedTitle;
    int trimmedWidth;
    boolean doRender = true;
    boolean buttonDown = false;
    Color buttonDownCol = new Color(100, 100, 100, 255);
    Logger logger = java.util.logging.Logger.getLogger(Button.class.getName());

    public Button(String title)
    {
        this.title = title;
    }

    public Button(Image image)
    {
        this.image = image;
    }

    public void setText(String title)
    {
        this.title = title;
        setShouldRender();
    }

    public void setImage(Image image)
    {
        this.image = image;
        setShouldRender();
    }

    @Override
    public void render(IFrame frame, IFrameRenderer frameRenderer, int xShift, int yShift) {
        frameRenderer.drawRect(Color.BLUE, 1, getPos().x + xShift, getPos().y + yShift, getSize().x, getSize().y);
        logger.fine("Start render button");

        if (buttonDown)
        {
            frameRenderer.drawFilledRect(buttonDownCol, getPos().x + xShift, getPos().y + yShift, getSize().x, getSize().y);
        }
        renderText(frameRenderer, xShift, yShift);
        renderImage(frameRenderer, xShift, yShift);
        doRender = false;
    }

    private void renderText(IFrameRenderer frameRenderer, int xShift, int yShift)
    {
        if (title == null) { return; }
        Font f = frameRenderer.newFont();
        f.setSize(16);
        Color col = new Color(0, 0, 255, 255);
        Image fImg;
        if (trimmedTitle != null && trimmedWidth == getSize().x)
        {
            fImg = f.genTextImage(trimmedTitle, col);
        }
        else if (getSize().x > f.calcTextSize(title).x)
        {
            fImg = f.genTextImage(title, col);
        }
        else
        {
            trimmedWidth = getSize().x;
            trimmedTitle = title;
            for (int i=1; i<title.length(); i++)
            {
                trimmedTitle = title.substring(0, title.length()-i) + "...";
                int w = f.calcTextSize(trimmedTitle).x;
                if (getSize().x > w)
                {
                    break;
                }
            }
            fImg = f.genTextImage(trimmedTitle, col);
        }
        int x = (getSize().x - fImg.getSize().x) /2;
        x = x + getPos().x;
        int y = 0;
        if (image == null) { y = getPos().y + (getSize().y/2) - (fImg.getSize().y/2); }
        else { y = getPos().y + getSize().y -1 - fImg.getSize().y; }
        frameRenderer.drawImage(fImg, x + xShift, y + yShift);
    }

    private void renderImage(IFrameRenderer frameRenderer, int xShift, int yShift)
    {
        if (image == null) { return; }
        int x = (getSize().x - image.getSize().x) /2;
        x = x + getPos().x;
        int y = getPos().y + (getSize().y/2) - (image.getSize().y/2);
        frameRenderer.drawImage(image, x + xShift, y + yShift);
    }

    @Override
    public boolean shouldRender() {
        return doRender;
    }

    @Override
    public void setShouldRender() {
        doRender = true;
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {

        if (action.value != 0)
        { // We only handle mouseEvents, where button is 0
            return false;
        }
        if (action.type == Action.AType.MouseReleased && buttonDown)
        {
            buttonDown = false;
            setShouldRender();
            return true;
        }
        if (!intersect(action.x + xShift, action.y + yShift)) { return false; }
        if (action.type == Action.AType.MousePressed)
        {
            buttonDown = true;
            setShouldRender();
            return true;
        }
        if (action.type == Action.AType.MouseClicked)
        {
            logger.info("Button clicked: " + title);
            if (getActionListener() != null)
            {
                getActionListener().onAction(action, xShift, yShift);
            }
        }
        return false;
    }
    
}
