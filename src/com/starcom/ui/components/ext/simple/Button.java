package com.starcom.ui.components.ext.simple;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.model.Color;

public class Button extends Component {

    @Override
    public void render(IFrame frame) {
        frame.getRenderer().drawRect(Color.BLUE, 1, getPos().x, getPos().y, getSize().x, getSize().y);
        System.out.println("Button render done"); //TODO: use logger
        // TODO Implement more
    }
    
}
