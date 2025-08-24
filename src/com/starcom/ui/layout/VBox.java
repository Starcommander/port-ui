package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;

public class VBox implements ILayoutManager{

    @Override
    public void doLayout(Container c) {
        int minY = 0;
        for (Component comp : c.getComponents())
        {
            minY += ((VBoxConf)comp.getLayoutConf()).minHight;
        }
        int fullY = c.getSize().y;
        float scale = 1.0f;
        if (fullY > minY)
        {
            scale = fullY/minY;
        }
        float curY = 0;
        for (Component comp : c.getComponents())
        {
            comp.getPos().x = 0;
            comp.getSize().x = c.getSize().x;
            comp.getPos().y = (int)curY;
            float curH = ((VBoxConf)comp.getLayoutConf()).minHight * scale;
            curY += curH;
            comp.getSize().y = (int)curH;
        }
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
        if (layoutConf instanceof VBoxConf) { return; }
        throw new IllegalStateException("LayoutConf does not match LayoutManager: " + layoutConf);
    }
    
    public static class VBoxConf implements ILayoutConf
    {
        public int minHight = 10;
        public VBoxConf(int minHight) { this.minHight = minHight; }
    }

}
