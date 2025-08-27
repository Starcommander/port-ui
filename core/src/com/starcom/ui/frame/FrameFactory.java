package com.starcom.ui.frame;

public class FrameFactory
{
  public static String FRAME_IMPL = "com.starcom.ui.frame.impl.FrameImpl";
  private static IFrame frameImpl;

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
}
