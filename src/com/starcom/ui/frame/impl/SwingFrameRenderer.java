package com.starcom.ui.frame.impl;

import com.starcom.ui.model.Color;
import com.starcom.ui.model.Image;

import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.InputStream;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrameRenderer;

public class SwingFrameRenderer implements IFrameRenderer
{
    static final java.awt.Color TRANSP_COLOR = new java.awt.Color(0,0,0,0);
    SwingFrame sframe;
    public SwingFrameRenderer(SwingFrame sframe)
    {
        this.sframe = sframe;
        this.sframe.jframe.getContentPane().addComponentListener(genListener());
    }

    public ComponentListener genListener()
    {
        return new ComponentListener() {

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                sframe.content.setShouldRender();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                sframe.content.setShouldRender();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                sframe.content.setShouldRender();
            }
            
        };
    }

    // public static Color fromSwing(java.awt.Color sc)
    // {
    //     Color c = new Color();
    //     c.r = sc.getRed();
    //     c.g = sc.getGreen();
    //     c.b = sc.getBlue();
    //     c.a = sc.getAlpha();
    //     return c;
    // }

    public static java.awt.Color toSwing(Color c)
    {
        return new java.awt.Color(c.r, c.g, c.b, c.a);
    }

    @Override
    public void drawFilledRect(Color color, int width, int height, int x, int y) {
        Graphics gr = sframe.jframe.getContentPane().getGraphics();
        gr.setColor(toSwing(color));
        gr.fillRect(x, y, width, height);
        gr.dispose();
    }

    @Override
    public void drawLine(Color color, int th, int x1, int y1, int x2, int y2) {
        Graphics gr = sframe.jframe.getContentPane().getGraphics();
        gr.setColor(toSwing(color));
        gr.drawLine(x1, y1, x2, y2);
        gr.dispose();
        //TODO: Use FillRect instead, and use th.
    }

    @Override
    public void drawImage(Image img, int x, int y) {
        SwingImage sImage = (SwingImage)img;
        Graphics gr = sframe.jframe.getContentPane().getGraphics();
        gr.drawImage(sImage.parent, x, y, TRANSP_COLOR, null);
        gr.dispose();
    }

    @Override
    public Font newFont() {
        return new SwingFont();
    }

    @Override
    public void clear() {
        Graphics gr = sframe.jframe.getContentPane().getGraphics();
        gr.clearRect(0, 0, sframe.jframe.getWidth(), sframe.jframe.getHeight());
        gr.dispose();
    }

    @Override
    public Image scaleImage(Image i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scaleImage'");
    }

    @Override
    public Image loadImage(InputStream s) {
        // TODO Auto-generated method stub
        ImageIcon i = new ImageIcon("");
        throw new UnsupportedOperationException("Unimplemented method 'loadImage'");
    }
}
