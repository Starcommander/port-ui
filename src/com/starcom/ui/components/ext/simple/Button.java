package com.starcom.ui.components.ext.simple;

import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import java.util.logging.Logger;

public class Button extends Label {

    static Logger logger = java.util.logging.Logger.getLogger(Button.class.getName());
    Color buttonDownColor = Color.GRAY_BRIGHT;
    Color buttonBgColor;
    boolean buttonDown;

    public Button(String title)
    {
        super(title);
    }

    public Button(Image image)
    {
        super(image);
    }

    public boolean isButtonDown() { return buttonDown; }
    public void setButtonDownColor(Color buttonDownColor)
    {
        this.buttonDownColor = buttonDownColor;
    }

    @Override
    public void setBackgroundColor(Color buttonBgColor)
    {
        this.buttonBgColor = buttonBgColor;
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
            super.setBackgroundColor(buttonBgColor);
            return true;
        }
        if (!intersect(action.x + xShift, action.y + yShift)) { return false; }
        if (action.type == Action.AType.MousePressed)
        {
            buttonDown = true;
            super.setBackgroundColor(buttonDownColor);
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
