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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;

import com.starcom.ui.components.SimpleContainer;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

public class SwingFrame implements IFrame
{
    JFrame jframe = new JFrame();
    SimpleContainer content = new SimpleContainer();
    SwingFrameGraphics graphics;
    public SwingFrame()
    {
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setContentPane(genContentPane());
        jframe.getContentPane().addComponentListener(genComponentListener());
        jframe.getContentPane().addKeyListener(genKeyListener());
        jframe.getContentPane().addMouseListener(genMouseListener());
        jframe.getContentPane().addMouseWheelListener(genMouseListener());
        jframe.getContentPane().addMouseMotionListener(genMouseListener());
        graphics = new SwingFrameGraphics(this);

        Point p = getMaxSize();
        jframe.setSize(p.x,p.y);
        jframe.setLocation(0,0);
    }

    /** A JPanel that implements the render function. */
    private java.awt.Container genContentPane()
    {
        JPanel p = new JPanel()
        {
            @Override public void paintComponent(Graphics g)
            {
                if (!getContent().shouldRender()) { return; }
                super.paintComponent(g);
                SwingFrameGraphics.preRender(g);
                getContent().getSize().set(SwingFrame.this.getSize().x, SwingFrame.this.getSize().y);
                getContent().getLayoutManager().doLayout(getContent());
                getContent().getLayoutManager().doLayoutSub(getContent());
                Color c = new Color(255, 255, 255, 255);
                graphics.drawFilledRect(c, SwingFrame.this.getSize().x, SwingFrame.this.getSize().y, 0, 0);
                getContent().getRenderer().render(getContent(), graphics, 0,0);
                SwingFrameGraphics.postRender();
            }
        };
        p.setLayout(jframe.getContentPane().getLayout());
        p.setName(jframe.getName());
        return p;
    }

    private MouseAdapter genMouseListener() {
        return new MouseAdapter (){

            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
                {
                    content.onAction(Action.fromMouseScrolled(e.getPoint().x, e.getPoint().y, e.getUnitsToScroll()*8), 0, 0);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                content.onAction(Action.fromMouseClicked(e.getPoint().x, e.getPoint().y, e.getButton()-1),0,0);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                content.onAction(Action.fromMouseDragged(e.getPoint().x, e.getPoint().y),0,0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                content.onAction(Action.fromMousePressed(e.getPoint().x, e.getPoint().y, e.getButton()-1),0,0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                content.onAction(Action.fromMouseReleased(e.getPoint().x, e.getPoint().y, e.getButton()-1),0,0);
            }};
    }

    private KeyListener genKeyListener() {
        return new KeyListener (){

            @Override
            public void keyPressed(KeyEvent e) {
                content.onAction(Action.fromKeyPressed(e.getKeyCode()),0,0);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                content.onAction(Action.fromKeyReleased(e.getKeyCode()),0,0);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                content.onAction(Action.fromKeyTyped(e.getKeyChar()),0,0);
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
                content.setShouldRender(true);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                content.setShouldRender(true);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                content.setShouldRender(true);
            }
            
        };
    }

    @Override
    public IFrameGraphics getGraphicsImpl() {
        return graphics;
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
        return new Point(jframe.getContentPane().getWidth(), jframe.getContentPane().getHeight());
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
            // Must be called later, otherwise first render renders nothing.
            SwingUtilities.invokeLater(() -> new Thread(() -> loopThread()).start());
        }
    }

    /** The render loop. */
    private void loopThread()
    {
        while (true)
        {
            if (!jframe.isVisible())
            {
                System.out.println("Stop render as jframe not visible"); //TODO: Use a logger
                break;
            }

            // This repaint() executes paintComponent of contentPane, see also genContentPane().
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
    public SimpleContainer getContent() {
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
