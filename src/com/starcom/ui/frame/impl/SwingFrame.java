package com.starcom.ui.frame.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
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
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

public class SwingFrame implements IFrame
{
    JFrame jframe = new JFrame();
    Container content = new SimpleContainer();
    SwingFrameRenderer renderer;
    public SwingFrame()
    {
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setContentPane(genContentPane());
        jframe.getContentPane().addComponentListener(genComponentListener());
        jframe.getContentPane().addKeyListener(genKeyListener());
        jframe.getContentPane().addMouseListener(genMouseListener());
        renderer = new SwingFrameRenderer(this);

        Point p = getMaxSize();
        jframe.setSize(p.x,p.y);
        jframe.setLocation(0,0);
    }

    private java.awt.Container genContentPane()
    {
        JPanel p = new JPanel()
        {
            @Override public void paintComponent(Graphics g)
            {
                if (getContent().shouldRender()) { super.paintComponent(g); }
                SwingFrameRenderer.preRender(g);
                getContent().layout();
                if (getContent().shouldRender())
                {
                    System.out.println("Render of components started"); //TODO: Use a logger
                    Color c = new Color(255, 255, 255, 255);
                    renderer.drawFilledRect(c, SwingFrame.this.getSize().x, SwingFrame.this.getSize().y, 0, 0);
                    SwingFrame.this.getContent().render(SwingFrame.this);
                }
                SwingFrameRenderer.postRender();
            }
        };
        p.setLayout(jframe.getContentPane().getLayout());
        p.setName(jframe.getName());
        return p;
    }

    private MouseListener genMouseListener() {
        return new MouseListener (){

            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.MouseClicked, e.getPoint().x, e.getPoint().y, null),0,0));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.MousePressed, e.getPoint().x, e.getPoint().y, null),0,0));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.MouseReleased, e.getPoint().x, e.getPoint().y, null),0,0));
            }};
    }

    private KeyListener genKeyListener() {
        return new KeyListener (){

            @Override
            public void keyPressed(KeyEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.KeyPressed, 0, 0, KeyEvent.getKeyText(e.getKeyCode())),0,0));
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.KeyReleased, 0, 0, KeyEvent.getKeyText(e.getKeyCode())),0,0));
                // TODO Auto-generated method stub
            }

            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> content.onAction(new Action(Action.AType.KeyTyped, 0, 0, KeyEvent.getKeyText(e.getKeyCode())),0,0));
                // TODO Auto-generated method stub
            }};
    }

    private ComponentListener genComponentListener()
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
        SwingUtilities.invokeLater(() -> jframe.setVisible(b));
        if (b)
        {
            SwingUtilities.invokeLater(() -> new Thread(() -> loopThread()).start());
        }
    }

    private void loopThread()
    {
        while (true)
        {
            if (!jframe.isVisible())
            {
                System.out.println("Stop render as jframe not visible"); //TODO: Use a logger
                break;
            }

            getContent().layout();
            jframe.getContentPane().repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace(); //TODO: Use logger
                break;
            }
        }
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
