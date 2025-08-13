package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;

import java.util.logging.Logger;

public class Label extends Component {
    String title;
    String trimmedTitle;
    int trimmedWidth;
    boolean doRender = true;
    Logger logger = java.util.logging.Logger.getLogger(Label.class.getName());

    public Label(String title)
    {
        this.title = title;
    }

    public void setText(String title)
    {
        this.title = title;
        setShouldRender();
    }

    @Override
    public void render(IFrame frame) {
        logger.fine("Start render label");

        Font f = frame.getRenderer().newFont();
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
        int x = 5 + getPos().x;
        int y = getPos().y + (getSize().y/2) - (fImg.getSize().y/2);
        frame.getRenderer().drawImage(fImg, x, y);
        doRender = false;
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
