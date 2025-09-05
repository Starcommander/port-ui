package com.starcom.ui.frame.impl;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SwingFont extends Font
{
    java.awt.Font sFont;
    BufferedImage emptyImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    private void updateSwingFont()
    {
        if (sFont == null) {}
        else if (!sFont.getName().equals(getFont())) {}
        else if (sFont.getStyle() != SwingFont.convert(getStyle())) {}
        else if (sFont.getSize() != getSize()) {}
        else { return; }
        sFont = new java.awt.Font(getFont(), SwingFont.convert(getStyle()), getSize());
    }

    @Override
    public String[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public static int convert(Style style)
    {
        if (style == Style.Plain) return java.awt.Font.PLAIN;
        if (style == Style.Bolt) return java.awt.Font.BOLD;
        if (style == Style.Italic) return java.awt.Font.ITALIC;
        return java.awt.Font.PLAIN;
    }

    @Override
    public Image genTextImage(String txt, Color color) {
        if (txt.length() == 0)
        {
          return new SwingImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        }
        Point size = calcTextSize(txt);
        BufferedImage img = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graph = img.createGraphics();
        updateSwingFont();
        graph.setFont(sFont);
        graph.setColor(SwingFrameGraphics.toSwing(color));
        graph.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graph.drawString(txt, 0, graph.getFontMetrics().getAscent());
        graph.dispose();
        return new SwingImage(img);
    }

    @Override
    public Point calcTextSize(String txt) {
        updateSwingFont();
        Graphics2D gr = emptyImg.createGraphics();
        FontMetrics fm = gr.getFontMetrics(sFont);
        gr.dispose();
        return new Point(fm.charsWidth(txt.toCharArray(), 0, txt.length()), fm.getHeight());
    }
    
}
