package com.starcom.ui.layout;

import com.starcom.ui.components.Container;

public class NullLayout implements ILayoutManager{

    @Override
    public void doLayout(Container c) {
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
    }
}
