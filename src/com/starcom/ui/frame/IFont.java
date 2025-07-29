package com.starcom.ui.frame;

import com.starcom.ui.model.Image;
import com.starcom.ui.model.Point;

public interface IFont
{
  public void setSize(int size);
  public Image genImage(String txt);
  public Point calcSize(String txt);
}
