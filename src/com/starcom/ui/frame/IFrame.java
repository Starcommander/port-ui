package com.starcom.ui.frame;

import com.starcom.ui.model.Point;
import com.starcom.ui.components.SimpleContainer;

public interface IFrame
{
  public IFrameRenderer getRendererImpl();
  public void setVisible(boolean b);
  public SimpleContainer getContent();
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
