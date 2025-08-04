package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameRenderer;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.model.Action;

public class SimpleContainer extends Container {
    @Override
    public void render(IFrame frame) {
        renderComponents(frame);
    }

    @Override
    public boolean shouldRender() {
        return shouldRenderComponents();
    }

    @Override
    public void setShouldRender() {
        for (Component c : components)
        {
            c.setShouldRender();
        }
    }

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
        return onActionComponents(action, xShift, yShift);
    }

    @Override
    public IFrameRenderer getInternalRenderer()
    {
      return FrameFactory.getFrame().getRenderer();
    }
}
