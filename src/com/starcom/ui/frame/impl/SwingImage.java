package com.starcom.ui.frame.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.starcom.ui.model.Image;
import com.starcom.ui.model.Point;

public class SwingImage implements Image {
    BufferedImage parent;

    public SwingImage(BufferedImage parent) { this.parent = parent; }

    public static BufferedImage toBufferedImage(java.awt.Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    @Override
    public Point getSize() {
        return new Point(parent.getWidth(), parent.getHeight());
    }

    @Override
    public SwingImage getScaledInstance(Point newSize) {
        java.awt.Image scaledImage = parent.getScaledInstance(newSize.x, newSize.y, BufferedImage.SCALE_DEFAULT);
        BufferedImage newParent = toBufferedImage(scaledImage);
        return new SwingImage(newParent);
    }
}
