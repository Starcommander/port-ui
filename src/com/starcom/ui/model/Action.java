package com.starcom.ui.model;

public class Action {
    public enum AType {MousePressed, MouseReleased, MouseClicked, KeyPressed, KeyReleased, KeyTyped}

    public AType type;
    public int x;
    public int y;
    public String key;

    public Action(AType type, int x, int y, String key)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.key = key;
    }
}
