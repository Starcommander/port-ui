package com.starcom.ui.frame;

import com.starcom.ui.model.Color;

import java.io.InputStream;

public interface IFrameRenderer
{
  public default void drawRect(Color color, int th, int x, int y, int w, int h)
  {
    drawLine(color, th, x, y, x+w, y);
    drawLine(color, th, x+w, y, x+w, y+h);
    drawLine(color, th, x+w, y+h, x, y+h);
    drawLine(color, th, x, y+h, x, y);
  }
  public Font newFont();
  public void drawFilledRect(Color color, int width, int height, int x, int y);
//  public void drawEllipse(Color color, int th, int x, int y, int w, int h);
//  public void drawFilledEllipse(Color color, int x, int y, int w, int h);
  public void drawLine(Color color, int th, int x1, int y1, int x2, int y2);
  public void drawImage(Image img, int x, int y);
  public Image scaleImage(Image i);
  public Image loadImage(InputStream s);
  public void clear();
}
