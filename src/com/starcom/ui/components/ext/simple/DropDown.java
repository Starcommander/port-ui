package com.starcom.ui.components.ext.simple;

import java.util.ArrayList;

import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IActionListener;
import com.starcom.ui.frame.ISelectedListener;
import com.starcom.ui.model.Action;

public class DropDown extends Button{
    boolean keepTitle = false;
    ArrayList<String> items = new ArrayList<>();
    ContextMenu menu;
    int menuWidth;

    /** A drop down menu that opens context menu.
     * @param title The title that should be displayed, or null for showing selected item
     */
    public DropDown(ISelectedListener subListener, String... items) {
        super(items[0]);
        menu = new ContextMenu((s) -> onSubButton(s, subListener), items);
        menuWidth = menu.getSize().x;
        for (String curItem : items)
        {
          this.items.add(curItem);
        }
        super.setActionListener(genSuperListener());
    }

    private IActionListener genSuperListener() {
        return new IActionListener() {
            @Override
            public boolean onAction(Action action, int xShift, int yShift) {
                if (menuWidth < getSize().x) { menu.getSize().x = getSize().x; }
                else { menu.getSize().x = menuWidth; }
                menu.getPos().x = getPos().x + xShift + getSize().x - menu.getSize().x;
                if (menu.getPos().x < 0) { menu.getPos().x = 0; }
                menu.getPos().y = getPos().y + yShift + getSize().y;
                int maxY = FrameFactory.getFrame().getSize().y; //TODO: What about subFrames?
                if ((menu.getPos().y + menu.getSize().y) > maxY) { menu.getPos().y = maxY - menu.getSize().y; }
                FrameFactory.getFrame().getContent().setContextMenu(menu);
                return true;
            }
        };
    }

    private void onSubButton(Object s, ISelectedListener subListener) {
        subListener.selected(s);
        if (!keepTitle) { setText(s.toString()); }
    }
        
    public void setPermanentText(String title)
    {
        super.setText(title);
        keepTitle = true;
    }

    @Override
    public void setActionListener(IActionListener listener)
    {
        throw new IllegalStateException("Use initial ISelectedListener instead.");
    }
}
