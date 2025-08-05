package com.starcom;

import java.io.FileInputStream;

import com.starcom.ui.components.ScrollPane;
import com.starcom.ui.components.ext.simple.Button;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrame;

public class App {

    public static void main(String[] args) throws Exception {
        IFrame frame = FrameFactory.getFrame();
        frame.setTitle("The portable GUI");
        //addSimpleButton(frame);
        addScrollPaneButton(frame);

        frame.setVisible(true);
    }

    static void addScrollPaneButton(IFrame frame)
    {
        ScrollPane sp = new ScrollPane(false, true);
        sp.getSize().x = 600;
        sp.getSize().y = 600;
        sp.getPos().x = 30;
        sp.getPos().y = 30;
        sp.addComponent(genSimpleButton());
        frame.getContent().addComponent(sp);
    }
    static void addSimpleButton(IFrame frame)
    {
        frame.getContent().addComponent(genSimpleButton());
    }

    static Button genSimpleButton()
    {
        Button b = new Button("Simple Button");
        b.getSize().x = 300;
        b.getSize().y = 150;
        b.getPos().x = 30;
        b.getPos().y = 30;
        return b;
    }

    static Button genImageButton()
    {
        try
        {
            Button b = new Button(FrameFactory.getFrame().getRendererImpl().loadImage(new FileInputStream(new java.io.File("/path/img.png"))));
            b.getSize().x = 300;
            b.getSize().y = 150;
            b.getPos().x = 30;
            b.getPos().y = 30;
            return b;
        }
        catch (Exception e) { return genSimpleButton(); }
    }
}
