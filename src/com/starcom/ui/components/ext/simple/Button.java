package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Image;

public class Button extends Component {
    boolean doRender = true;

    @Override
    public void render(IFrame frame) {
        frame.getRenderer().drawRect(Color.BLUE, 1, getPos().x, getPos().y, getSize().x, getSize().y);
        System.out.println("Button render done"); //TODO: use logger

        Font f = frame.getRenderer().newFont();
        f.setSize(16);
        Color col = new Color(0, 0, 255, 255);
        Image fImg = f.genTextImage("Simple button", col);
        int x = (getSize().x - fImg.getSize().x) /2;
        x = x + getPos().x;
        int y = getPos().y + (getSize().y/2) - (fImg.getSize().y/2);
        frame.getRenderer().drawImage(fImg, x, y);
        // TODO: first calcSize to check if trim necessary.
        // TODO Implement more
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
    
}
