package com.starcom.ui.model;

public class Action {
    public enum AType {MousePressed, MouseReleased, MouseClicked, MouseDragged, MouseScrolled, KeyPressed, KeyReleased, KeyTyped}

    public AType type;
    public int x;
    public int y;
    public int value;

    /** Holds all necessary information for an action.<br>
     * The value may be mouse-button-number (0=first), scrollvalue (+/-), vk_keycode or typed character.
     * @see java.awt.event.KeyEvent
     * @see https://learn.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes */
    private Action(AType type, int x, int y, int value)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public static Action fromMouseScrolled(int x, int y, int value) { return new Action(AType.MouseScrolled, x, y, value); }
    public static Action fromMousePressed(int x, int y, int btn) { return new Action(AType.MousePressed, x, y, btn); }
    public static Action fromMouseReleased(int x, int y, int btn) { return new Action(AType.MouseReleased, x, y, btn); }
    public static Action fromMouseClicked(int x, int y, int btn) { return new Action(AType.MouseClicked, x, y, btn); }
    public static Action fromMouseDragged(int x, int y) { return new Action(AType.MouseDragged, x, y, 0); }
    public static Action fromKeyPressed(int vk_keycode) { return new Action(AType.KeyPressed, 0, 0, vk_keycode); }
    public static Action fromKeyReleased(int vk_keycode) { return new Action(AType.KeyReleased, 0, 0, vk_keycode); }
    public static Action fromKeyTyped(int character) { return new Action(AType.KeyTyped, 0, 0, character); }
}
