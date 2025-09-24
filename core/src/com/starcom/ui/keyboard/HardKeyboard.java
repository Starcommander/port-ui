package com.starcom.ui.keyboard;

import com.starcom.ui.components.ext.simple.ContextMenu;
import com.starcom.ui.components.ext.simple.TextField;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;

public class HardKeyboard implements IKeyboard{
    ContextMenu menu = new ContextMenu();

    @Override
    public void show(TextField focusComponent) {
        menu.setFocusComponent(focusComponent);
        menu.setActionListener((a,x,y) -> onActionInternal(a,x,y));
        menu.setOnHide(() -> ((TextField)menu.getFocusComponent()).setCursorVisible(false) );
    }

    public boolean onActionInternal(Action action, int xShift, int yShift)
    {
        if (action.type != Action.AType.MouseClicked) { return false; }
        TextField tf = (TextField)menu.getFocusComponent();
        Point absPos = tf.getAbsolutePos();
        tf.setCursorLocation(action.x - absPos.x, action.y - absPos.y); //TODO Update as softKeyboard
        return true;
    }
}
