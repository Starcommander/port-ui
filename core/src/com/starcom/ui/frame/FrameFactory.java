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
