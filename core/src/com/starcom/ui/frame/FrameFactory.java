package com.starcom.ui.frame;

import com.starcom.ui.keyboard.HardKeyboard;
import com.starcom.ui.keyboard.IKeyboard;

public class FrameFactory
{
  public static String FRAME_IMPL = "com.starcom.ui.frame.impl.FrameImpl";
  private static IFrame frameImpl;
  static IKeyboard keyboard = new HardKeyboard();

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static IFrame getFrame()
  {
    if (frameImpl == null)
    {
      Class clazz;
      try {
        clazz = Class.forName(FRAME_IMPL);
        frameImpl = (IFrame)clazz.getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        throw new IllegalStateException("Frame implemention missing in project", e);
      }
    }
    return frameImpl;
  }

  /** Allows to use non-default constructor.
   * @param frame The frame to use, for example using an existing frame. */
  public static void presetFrame(IFrame frame)
  {
    if (frameImpl != null) { throw new IllegalStateException("Frame was already created."); }
    frameImpl = frame;
  }

  public static IKeyboard getKeyboard()
  {
    return keyboard;
  }

  public static void setKeyboard(IKeyboard newKeyboard)
  {
    if (newKeyboard == null) { throw new NullPointerException("Keyboard must not be null"); }
    keyboard = newKeyboard;
  }
}
