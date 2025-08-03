package com.starcom.ui.frame.impl;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.starcom.ui.components.SimpleContainer;
import com.starcom.ui.components.Container;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;

public class SwingFrame implements IFrame
{
    JFrame jframe = new JFrame();
    Container content = new SimpleContainer();
    SwingFrameRenderer renderer;
    public SwingFrame()
    {
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.getContentPane().addComponentListener(genComponentListener());
//        jframe.getContentPane().addKeyListener(genKeyListener()); //TODO: Add this
        jframe.getContentPane().addMouseListener(genMouseListener());
        renderer = new SwingFrameRenderer(this);

        Point p = getMaxSize();
        jframe.setSize(p.x,p.y);
        jframe.setLocation(0,0);
    }

    private MouseListener genMouseListener() {
        return new MouseListener (){

            @Override
            public void mouseClicked(MouseEvent e) {
                content.onAction(new Action(Action.AType.MouseClicked, e.getPoint().x, e.getPoint().y, null),0,0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e) {
                content.onAction(new Action(Action.AType.MousePressed, e.getPoint().x, e.getPoint().y, null),0,0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                content.onAction(new Action(Action.AType.MouseReleased, e.getPoint().x, e.getPoint().y, null),0,0);
            }};
    }

    private KeyListener genKeyListener() {
        return new KeyListener (){

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }};
    }

    public ComponentListener genComponentListener()
    {
        return new ComponentListener() {

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                content.setShouldRender();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                content.setShouldRender();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                content.setShouldRender();
            }
            
        };
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
        if (!jframe.isVisible())
        {
            System.out.println("Stop render as jframe not visible"); //TODO: Use a logger
            return;
        }
        getContent().layout();
        if (getContent().shouldRender())
        {
            System.out.println("Render of components started"); //TODO: Use a logger
            getContent().render(this);
        }
        SwingUtilities.invokeLater(() -> renderLoop());
    }

    @Override
    public Container getContent() {
        return content;
    }

    @Override
    public boolean setTitle(String title) {
        jframe.setTitle(title);
        return true;
    }

    @Override
    public boolean setIcon(Image icon) {
        SwingImage img = (SwingImage)icon;
        jframe.setIconImage(img.parent);
        return true;
    }
}
