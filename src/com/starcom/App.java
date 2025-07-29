package com.starcom;

import com.starcom.ui.components.ext.simple.Button;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrame;

public class App {

    public static void main(String[] args) throws Exception {
        FrameFactory.setFrameImpl("com.starcom.ui.frame.impl.SwingFrame");
        IFrame frame = FrameFactory.getFrame();
        frame.setTitle("The portable GUI"); //TODO The title makes render invisible, but why?
        Button b = new Button();
        b.getSize().x = 300;
        b.getSize().y = 150;
        b.getPos().x = 30;
        b.getPos().y = 30;
        frame.getContent().addComponent(b);
        frame.setVisible(true);
    }
    
}
