package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;
import com.starcom.ui.model.Point;

public class RelativeLayout implements ILayoutManager{

    @Override
    public void doLayout(Container c) {
        float sizeX = c.getSize().x;
        float sizeY = c.getSize().y;
        for (Component comp: c.getComponents())
        {
            RelativeLayoutConf conf = (RelativeLayoutConf)comp.getLayoutConf();
            comp.getSize().x = (int)(sizeX * conf.sizeX);
            comp.getSize().y = (int)(sizeY * conf.sizeY);
            comp.getPos().x = (int)(sizeX * conf.posX);
            comp.getPos().y = (int)(sizeY * conf.posY);
            if (comp.getSize().x < conf.minSize.x) { comp.getSize().x = conf.minSize.x; }
            if (comp.getSize().y < conf.minSize.y) { comp.getSize().y = conf.minSize.y; }
        }
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
        if (layoutConf instanceof RelativeLayoutConf) { return; }
        throw new IllegalStateException("LayoutConf is wrong instance: " + layoutConf);
    }
    
    public static class RelativeLayoutConf implements ILayoutConf
    {
        public float posX;
        public float posY;
        public float sizeX;
        public float sizeY;
        public Point minSize = new Point(20,20);

        /** Creates RelativeLayoutConf, where the values are between 0.0 and 1.0 that is relative to MainFrame. */
        public RelativeLayoutConf(float posX, float posY, float width, float height)
        {
            this.posX = posX;
            this.posY = posY;
            this.sizeX = width;
            this.sizeY = height;
        }

        @Override
        public String toString()
        {
            return super.toString() + ": posX=" + posX + " posY=" + posY + " width=" + sizeX + " height=" + sizeY;
        }
    }
}
