package com.starcom;

import com.starcom.ui.components.ext.simple.Button;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrame;

public class App {

    public static void main(String[] args) throws Exception {
        IFrame frame = FrameFactory.getFrame();
        frame.setTitle("The portable GUI");
        Button b = new Button("Simple Button");
        b.getSize().x = 300;
        b.getSize().y = 150;
        b.getPos().x = 30;
        b.getPos().y = 30;
        frame.getContent().addComponent(b);
        frame.setVisible(true);
    }
    
}
