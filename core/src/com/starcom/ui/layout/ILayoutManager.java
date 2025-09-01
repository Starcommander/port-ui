package com.starcom.ui.layout;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.Container;

public interface ILayoutManager {
    public void doLayout(Container c);
    public default void doLayoutSub(Container c)
    {
        for (Component comp : c.getComponents())
        {
            if (comp instanceof Container)
            {
                Container subC = (Container)comp;
                subC.getLayoutManager().doLayout(subC);
            }
        }
    }
    /** Checks whether this layoutConf is the correct instance for this LayoutManager.
     * @param layoutConf The conf to check.
     * @throws IllegalStateException If layoutConf is not the correct instance. */
    public void checkLayoutConf(ILayoutConf layoutConf) throws IllegalStateException; 
    /** Sets the size of container matching all components. */
    public void pack(Container main);
    public static interface ILayoutConf {}
}
