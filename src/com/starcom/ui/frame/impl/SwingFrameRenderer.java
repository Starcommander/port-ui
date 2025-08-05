package com.starcom.ui.frame.impl;

import com.starcom.ui.model.Color;

import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Rect;

public class SwingFrameRenderer implements IFrameRenderer
{
    static final java.awt.Color TRANSP_COLOR = new java.awt.Color(0,0,0,0);
    Logger logger = Logger.getLogger(SwingFrameRenderer.class.getName());
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
    public void drawFilledRect(Color color, int x, int y, int w, int h) {
        gr.setColor(toSwing(color));
        gr.fillRect(x, y, w, h);
    }

    @Override
    public void drawLine(Color color, int th, int x1, int y1, int x2, int y2) {
        gr.setColor(toSwing(color));
        Graphics2D g2 = (Graphics2D) gr;
        g2.setStroke(new BasicStroke(th));
        g2.draw(new Line2D.Float(x1, y1, x2, y2));
    }

    @Override
    public void drawImage(Image img, int x, int y) {
        SwingImage sImage = (SwingImage)img;
        gr.drawImage(sImage.parent, x, y, TRANSP_COLOR, null);
    }

    @Override
    public void drawPartialImage(Image img, int x, int y, Rect visibleRect)
    {
        if (visibleRect.pos.x<0) throw new IllegalStateException("visibleRect.pos.x must not be negative");
        if (visibleRect.pos.y<0) throw new IllegalStateException("visibleRect.pos.y must not be negative");
        if ((visibleRect.pos.x + visibleRect.size.x) > img.getSize().x) throw new IllegalStateException("visibleRect.size.x is outside of image to draw");
        if ((visibleRect.pos.y + visibleRect.size.y) > img.getSize().y) throw new IllegalStateException("visibleRect.size.y is outside of image to draw");
        Image tmpImg = img.getScaledInstance(img.getSize());
        Graphics imgGraph = ((SwingImage)tmpImg).parent.createGraphics();
        ((Graphics2D)imgGraph).setBackground(TRANSP_COLOR);
        if (visibleRect.pos.x != 0) { imgGraph.clearRect(0,0,visibleRect.pos.x, img.getSize().y); }
        if (visibleRect.pos.y != 0) { imgGraph.clearRect(0,0,img.getSize().x, visibleRect.pos.y); }
        if ((visibleRect.pos.x+visibleRect.size.x) < img.getSize().x) { imgGraph.clearRect(visibleRect.pos.x+visibleRect.size.x,0,img.getSize().x, img.getSize().y); }
        if ((visibleRect.pos.y+visibleRect.size.y) < img.getSize().x) { imgGraph.clearRect(0,visibleRect.pos.y+visibleRect.size.y,img.getSize().x, img.getSize().y); }
        imgGraph.dispose();
        drawImage(tmpImg, x, y);
    }

    @Override
    public Font newFont() {
        return new SwingFont();
    }

    @Override
    public Image loadImage(InputStream s) {
        try {
            BufferedImage img = ImageIO.read(s);
            return new SwingImage(img);
        } catch (IOException e) {
            logger.severe("Cannot load image: " + e);
            return null;
        }
    }
}
