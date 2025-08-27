package com.starcom.ui.frame;

import com.starcom.ui.model.Action;

public interface IActionListener {
    public boolean onAction(Action action, int xShift, int yShift);
}
