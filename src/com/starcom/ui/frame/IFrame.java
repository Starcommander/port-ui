package com.starcom.ui.frame;

import com.starcom.ui.model.Image;
import com.starcom.ui.model.Point;
import com.starcom.ui.components.Container;

public interface IFrame
{
  public IFrameRenderer getRenderer();
  public IFont getFont();
  public void setVisible(boolean b);
  public Container getContent();
  public void dispose();
  public Point getSize();
  public Point getMaxSize();
  public void setSize(Point size);
  public IFrame newSubFrame();
  /** Sets the title.
   * @param title The title of this frame
   * @return True if supported
   */
  public boolean setTitle(String title);
  /** Sets the Icon.
   * @param title The icon of this frame
   * @return True if supported
   */
  public boolean setIcon(Image icon);
}
