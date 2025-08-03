package com.starcom.ui.components;

import com.starcom.ui.frame.IFrame;
import com.starcom.ui.model.Action;

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

    @Override
    public boolean onAction(Action action, int xShift, int yShift) {
        for (Component c : components)
        {
            if (c.onAction(action, getPos().x + xShift, getPos().y + yShift)) { return true; }
        }
        return false;
    }
}
