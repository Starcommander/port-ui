package com.starcom.ui.frame.impl;

import com.starcom.ui.model.Color;

import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.io.InputStream;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.Image;

public class SwingFrameRenderer implements IFrameRenderer
{
    static final java.awt.Color TRANSP_COLOR = new java.awt.Color(0,0,0,0);
    SwingFrame sframe;
    static Graphics gr;
    public SwingFrameRenderer(SwingFrame sframe)
    {
        this.sframe = sframe;
    }

    public static void preRender(Graphics graph) { gr = graph; }
    public static void postRender() { gr = null; }

//    public static Color fromSwing(java.awt.Color sc)
//    {
//        return new Color(sc.getRed(), sc.getGreen(), sc.getBlue(), sc.getAlpha());
//    }

    public static java.awt.Color toSwing(Color c)
    {
        return new java.awt.Color(c.r, c.g, c.b, c.a);
    }

    @Override
    public void drawFilledRect(Color color, int width, int height, int x, int y) {
        gr.setColor(toSwing(color));
        gr.fillRect(x, y, width, height);
    }

    @Override
    public void drawLine(Color color, int th, int x1, int y1, int x2, int y2) {
        gr.setColor(toSwing(color));
        gr.drawLine(x1, y1, x2, y2);
        //TODO: Use FillRect instead, and use th.
    }

    @Override
    public void drawImage(Image img, int x, int y) {
        SwingImage sImage = (SwingImage)img;
        gr.drawImage(sImage.parent, x, y, TRANSP_COLOR, null);
    }

    @Override
    public Font newFont() {
        return new SwingFont();
    }

    @Override
    public void clear() {
        gr.clearRect(0, 0, sframe.jframe.getWidth(), sframe.jframe.getHeight());
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
