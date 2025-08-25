package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;

public class HBox implements ILayoutManager{

    @Override
    public void doLayout(Container c) {
        int minX = 0;
        for (Component comp : c.getComponents())
        {
            minX += ((HBoxConf)comp.getLayoutConf()).minWidth;
        }
        int fullX = c.getSize().x;
        float scale = 1.0f;
        if (fullX > minX)
        {
            scale = fullX/minX;
        }
        float curX = 0;
        for (Component comp : c.getComponents())
        {
            comp.getPos().x = (int)curX;
            comp.getSize().y = c.getSize().y;
            comp.getPos().y = 0;
            float curW = ((HBoxConf)comp.getLayoutConf()).minWidth * scale;
            curX += curW;
            comp.getSize().x = (int)curW;
        }
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
        if (layoutConf instanceof HBoxConf) { return; }
        throw new IllegalStateException("LayoutConf does not match LayoutManager: " + layoutConf);
    }
    
    public static class HBoxConf implements ILayoutConf
    {
        public int minWidth = 10;
        public HBoxConf(int minWidth) { this.minWidth = minWidth; }
    }

}
