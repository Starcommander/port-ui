package com.starcom.ui.frame.impl;

public class FrameImpl extends LwjglFrame {
    public FrameImpl()
    {
        super();
    }

    /** In this case loopStep() must be called in render-Loop. */
    public FrameImpl(long window)
    {
        super(window);
    }
}
