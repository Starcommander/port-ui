package com.starcom.ui.components.ext.simple;

import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import java.util.logging.Logger;

public class Button extends Label {

    static Logger logger = java.util.logging.Logger.getLogger(Button.class.getName());
    Color bgColorDown = Color.GRAY_BRIGHT;
    Color bgColor;
    boolean buttonDown;
    Image upImage;
    Image downImage;

    public Button(String title)
    {
        super(title);
    }

    public Button(Image image)
    {
        super(image);
        upImage = image;
    }

    public void setDownImage(Image downImage)
    {
        if (image == null) { throw new IllegalStateException("DownImage does only make sense with Button(image) not with Button(text)"); }
        this.downImage = downImage;
    }

    public boolean isButtonDown() { return buttonDown; }
    public void setBackgroundColorDown(Color bgColorDown)
    {
        this.bgColorDown = bgColorDown;
    }

    @Override
    public void setBackgroundColor(Color bgColor)
    {
        this.bgColor = bgColor;
        setShouldRender(true);
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
            super.setBackgroundColor(bgColor);
            if (image != null) { super.setImage(image); }
            return true;
        }
        if (!intersect(action.x + xShift, action.y + yShift)) { return false; }
        if (action.type == Action.AType.MousePressed)
        {
            buttonDown = true;
            super.setBackgroundColor(bgColorDown);
            if (downImage != null) { super.setImage(downImage); }
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
