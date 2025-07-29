package com.starcom.ui.frame.impl;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;

import com.starcom.ui.components.SimpleContainer;
import com.starcom.ui.components.Container;
import com.starcom.ui.frame.IFont;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.model.Image;
import com.starcom.ui.model.Point;

public class SwingFrame implements IFrame
{
    JFrame jframe = new JFrame();
    Container content = new SimpleContainer();
    SwingFrameRenderer renderer;
    public SwingFrame()
    {
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        renderer = new SwingFrameRenderer(this);


        Point p = getMaxSize();
        jframe.setSize(p.x,p.y);
        jframe.setLocation(0,0);
    }

    @Override
    public IFrameRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void dispose() {
        jframe.setVisible(false);
        jframe.dispose();
    }

    @Override
    public IFrame newSubFrame() {
        return new SwingFrame();
    }

    @Override
    public Point getSize() {
        return new Point(jframe.getWidth(), jframe.getHeight());
    }

    @Override
    public Point getMaxSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        return new Point(xSize,ySize);
    }

    @Override
    public void setSize(Point s) {
        jframe.setSize(s.x, s.y);
    }

    @Override
    public void setVisible(boolean b) {
        jframe.setVisible(b);
        SwingUtilities.invokeLater(() -> jframe.setVisible(b));
        if (b)
        {
          SwingUtilities.invokeLater(() -> renderLoop());
        }
    }

    private void renderLoop()
    {
        System.out.println("Render started"); //TODO: Use a logger
        if (!jframe.isVisible())
        {
            System.out.println("Stop render as jframe not visible"); //TODO: Use a logger
            return;
        }
        getContent().layout();
        getContent().render(this);
        SwingUtilities.invokeLater(() -> renderLoop());
    }

    @Override
    public Container getContent() {
        return content;
    }

    @Override
    public IFont getFont() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFont'");
    }

    @Override
    public boolean setTitle(String title) {
        jframe.setTitle(title);
        return true;
    }

    @Override
    public boolean setIcon(Image icon) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIcon'");
    }
}
