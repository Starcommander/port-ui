package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;

public class SimpleContainer extends Container {

    @Override
    public void render(IFrame frame) {
        renderComponents(frame);
    }

    @Override
    public boolean shouldRender() {
        for (Component c : components)
        {
            if (c.shouldRender()) { return true; }
        }
        return false;
    }

    @Override
    public void setShouldRender() {
        for (Component c : components)
        {
            c.setShouldRender();
        }
    }
}
