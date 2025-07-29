package com.starcom.ui.frame;

public class FrameFactory
{
  private static IFrame frameImpl;
  public static void setFrameImpl(String impl) throws Exception
  {
    Class clazz = Class.forName(impl);
    frameImpl = (IFrame)clazz.getDeclaredConstructor().newInstance();
  }
  public static IFrame getFrame()
  {
    if (frameImpl == null) { throw new IllegalStateException("Frame implemention must be selected first"); }
    return frameImpl;
  }
}
