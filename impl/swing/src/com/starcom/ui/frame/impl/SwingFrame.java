package com.starcom.ui.frame.impl;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;

import com.starcom.ui.components.RootContainer;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

public class SwingFrame implements IFrame
{
    static Logger logger = java.util.logging.Logger.getLogger(SwingFrame.class.getName());
    JFrame jframe;
    RootContainer content = new RootContainer();
    SwingFrameGraphics graphics;
    public SwingFrame()
    {
        this(new JFrame());
    }

    public SwingFrame(JFrame jframe)
    {
        this.jframe = jframe;
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.getContentPane().add(genContentPane());
        jframe.getContentPane().addComponentListener(genComponentListener());
        jframe.setFocusable(true);
        jframe.setFocusTraversalKeysEnabled(false);
        jframe.addKeyListener(genKeyListener());
        jframe.getContentPane().addMouseListener(genMouseListener());
        jframe.getContentPane().addMouseWheelListener(genMouseListener());
        jframe.getContentPane().addMouseMotionListener(genMouseListener());
        jframe.addWindowStateListener( (e) -> content.setShouldRender(true) );
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
                getContent().onRender(SwingFrame.this.getSize().x, SwingFrame.this.getSize().y, graphics);
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
                if (e.getModifiersEx() == 0 || e.getModifiersEx() == 64 || e.getModifiersEx() == 8192)
                { // Only 'None' or 'Shift' or 'AltGr' allowed
                    content.onAction(Action.fromKeyTyped(e.getKeyChar()),0,0);
                }
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
                logger.info("Stop render as jframe not visible");
                break;
            }

            // This repaint() executes paintComponent of contentPane, see also genContentPane().
            jframe.getContentPane().repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace(); //TODO: Use logger
                break;
            }
        }
    }

    @Override
    public RootContainer getContent() {
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

    @Override
    public String getClipboardString() {
        Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard == null) return "";
        Transferable contents = clipboard.getContents(null);
        String result = "";
        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            try
            {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (Exception e)
            {
                //do nothing
            }
        }
        return result;
    }

    @Override
    public void setClipboardString(String txt) {
        Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard == null) return;
        clipboard.setContents(new StringSelection(txt), new ClipboardOwner()
        {
            public void lostOwnership(Clipboard clipboard, Transferable contents)
            {
                //do nothing
            }
        });
    }
}
