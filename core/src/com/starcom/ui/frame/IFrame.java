package com.starcom.ui.frame;

import com.starcom.ui.model.Point;
import com.starcom.ui.components.RootContainer;

/** The implementation of IFrame catches all actions and forwards them to the RootContainer.<br>
 * It is also responsible for a looper-Thread, that executes rootContainer.onRender(width, height, graphics) */
public interface IFrame
{
  public IFrameGraphics getGraphicsImpl();
  public void setVisible(boolean b);
  public RootContainer getContent();
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
