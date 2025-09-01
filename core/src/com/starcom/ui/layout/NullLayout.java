package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;

public class NullLayout implements ILayoutManager{

    @Override
    public void doLayout(Container c) {
    }

    @Override
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException {
    }

    private static void updateSizeAdd(Container main, Component cAdded) {
        int newX = cAdded.getPos().x + cAdded.getSize().x;
        int newY = cAdded.getPos().y + cAdded.getSize().y;
        if (newX > main.getSize().x) { main.getSize().x = newX; }
        if (newY > main.getSize().y) { main.getSize().y = newY; }
    }

    public static void packNull(Container main) {
        main.getSize().x = 0;
        main.getSize().y = 0;
        for (Component cur : main.getComponents())
        {
            updateSizeAdd(main, cur);
        }
    }

    @Override
    public void pack(Container main) {
        packNull(main);
    }
}
