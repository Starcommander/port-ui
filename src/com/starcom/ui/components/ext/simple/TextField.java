package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;

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
        setShouldRender();
    }

    @Override
    public void render(IFrame frame, IFrameRenderer frameRenderer, int xShift, int yShift) {
        logger.fine("Start render label");

        Font f = frameRenderer.newFont();
        f.setSize(16);
        Color col = new Color(0, 0, 255, 255);
        Image fImg = f.genTextImage(title, col);
        int x = 5 + getPos().x;
        int y = getPos().y + (getSize().y/2) - (fImg.getSize().y/2);
        frameRenderer.drawImage(fImg, x + xShift, y + yShift);
        doRender = false;
        //TODO: Implement Cursor and handle key-events.
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
        return false;
    }
}
