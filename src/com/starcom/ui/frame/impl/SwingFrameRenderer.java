package com.starcom.ui.frame.impl;

import com.starcom.ui.model.Color;
import com.starcom.ui.model.Image;

import javax.swing.ImageIcon;
import java.io.InputStream;

import com.starcom.ui.frame.IFrameRenderer;

public class SwingFrameRenderer implements IFrameRenderer
{
    SwingFrame sframe;
    public SwingFrameRenderer(SwingFrame sframe)
    {
        this.sframe = sframe;
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
        sframe.jframe.getGraphics().setColor(toSwing(color));
        sframe.jframe.getGraphics().fillRect(x, y, width, height);
    }

    @Override
    public void drawLine(Color color, int th, int x1, int y1, int x2, int y2) {
        sframe.jframe.getGraphics().setColor(toSwing(color));
        sframe.jframe.getGraphics().drawLine(x1, y1, x2, y2);
        //TODO: Use FillRect instead, and use th.
    }

    @Override
    public void drawPic(Image img, int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawPic'");
    }

    @Override
    public void clear() {
        sframe.jframe.getGraphics().clearRect(0, 0, sframe.jframe.getWidth(), sframe.jframe.getHeight());
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
