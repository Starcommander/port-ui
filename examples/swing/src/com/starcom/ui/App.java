package com.starcom.ui;

import java.io.FileInputStream;

import com.starcom.ui.components.ScrollPane;
import com.starcom.ui.components.ext.simple.Button;
import com.starcom.ui.components.ext.simple.TextField;
import com.starcom.ui.components.ext.simple.DropDown;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.keyboard.SoftKeyboard;
import com.starcom.ui.layout.RelativeLayout;
import com.starcom.ui.layout.RelativeLayout.RelativeLayoutConf;
import com.starcom.ui.layout.VBox;
import com.starcom.ui.layout.VBox.VBoxConf;

public class App {

    public static void main(String[] args) throws Exception {
        IFrame frame = FrameFactory.getFrame();
        frame.setTitle("The portable GUI");
        //addSimpleButton(frame);
        //addScrollPaneButton(frame);
        //addScrollPaneButtons(frame);
        //addScrollPaneRelativeButtons(frame);
        //addDropDown(frame);
        addTextField(frame);

        frame.setVisible(true);
    }

    private static void addTextField(IFrame frame) {
        TextField tf = new TextField("Initial Text");
        tf.getSize().x = 300;
        tf.getSize().y = 60;
        tf.getPos().x = 30;
        tf.getPos().y = 30;
        frame.getContent().addComponent(tf, null);
        FrameFactory.setKeyboard(new SoftKeyboard());
    }

    private static void addDropDown(IFrame frame) {
        DropDown d = new DropDown((s) -> System.out.println("Submenu: " + s), "One", "Two", "Three");
        d.getSize().x = 90;
        d.getSize().y = 60;
        d.getPos().x = 30;
        d.getPos().y = 30;
        frame.getContent().addComponent(d, null);
    }

    static void addScrollPaneButton(IFrame frame)
    {
        ScrollPane sp = genScrollPane();
        Button b = genSimpleButton();
        b.getSize().y = 800;
        b.getSize().x = 800;
        sp.addComponent(b, null);
        frame.getContent().addComponent(sp, null);
    }

    static void addScrollPaneRelativeButtons(IFrame frame)
    {
        ScrollPane sp = genScrollPane();
        sp.setLayoutManager(new VBox());
        frame.getContent().setLayoutManager(new RelativeLayout());
        for (int i=0; i<50; i++)
            sp.addComponent(genSimpleButton(), new VBoxConf(30));
        frame.getContent().addComponent(sp, new RelativeLayoutConf(0f,0f,1f,0.8f));
        frame.getContent().addComponent(genSimpleButton(), new RelativeLayoutConf(0f,0.8f,1f,0.2f));
    }

    static void addScrollPaneButtons(IFrame frame)
    {
        ScrollPane sp = genScrollPane();
        sp.setLayoutManager(new VBox());
        for (int i=0; i<50; i++)
            sp.addComponent(genSimpleButton(), new VBoxConf(30));
        frame.getContent().addComponent(sp, null);
    }
    static void addSimpleButton(IFrame frame)
    {
        frame.getContent().addComponent(genSimpleButton(), null);
    }

    static ScrollPane genScrollPane()
    {
        ScrollPane sp = new ScrollPane(false, true);
        sp.getSize().x = 600;
        sp.getSize().y = 600;
        sp.getPos().x = 30;
        sp.getPos().y = 30;
        return sp;
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
            Button b = new Button(FrameFactory.getFrame().getGraphicsImpl().loadImage(new FileInputStream(new java.io.File("/path/img.png"))));
            b.getSize().x = 300;
            b.getSize().y = 150;
            b.getPos().x = 30;
            b.getPos().y = 30;
            return b;
        }
        catch (Exception e) { return genSimpleButton(); }
    }
}
