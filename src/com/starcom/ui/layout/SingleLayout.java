package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;

public class SingleLayout implements ILayoutManager
{

    @Override
    public void doLayout(Container c) {
        for (Component comp : c.getComponents())
        {
            comp.getPos().set(0,0);
            comp.getSize().set(c.getSize().x, c.getSize().y);
            break;
        }
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
    }
    
}
