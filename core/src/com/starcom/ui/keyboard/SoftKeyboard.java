package com.starcom.ui.keyboard;

import java.util.ArrayList;
import java.util.HashMap;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.ext.simple.ContextMenu;
import com.starcom.ui.components.ext.simple.Label;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.render.IRenderer;

public class SoftKeyboard implements IKeyboard
{
  // public static final String SPECIAL_DEL = "d";
  // public static final String SPECIAL_ENTER = "e";
  public static final String SPECIAL_SWITCH_TO = "s=";
  private static final String PIX_KEYB_DEF = "/keyb-def.png";
  private static final String PIX_KEYB_DEF_UP = "/keyb-def-up.png";

  int lastX, lastY;
  boolean skipIdenticalTouch = true;
  boolean active = false;
  String curKeyboard = PIX_KEYB_DEF;
  HashMap<String,ArrayList<KeyModel>> keyboards = new HashMap<>();
  KeyboardView keyboardView = new KeyboardView(PIX_KEYB_DEF, this);
  Component focusComponent;

  public SoftKeyboard()
  {
    keyboards.put(PIX_KEYB_DEF, createKeyList(false));
    keyboards.put(PIX_KEYB_DEF_UP, createKeyList(true));
  }

  /** Sets the keyboard active, and showing for textfields.
   * @param active True to set active.
   * @param skipIdenticalTouch For recognize and skip touch-release.
   * <br>Useful on mobile devices, but not on desktop with mouse. */
  public void setActive(boolean active, boolean skipIdenticalTouch)
  {
    this.active = active;
    this.skipIdenticalTouch = skipIdenticalTouch;
  }

  /** *  List of keyboards.
   * <br>The hashmap contains the image-file-name with appendant keyModel-list.
   * @return The whole list. */
  public HashMap<String,ArrayList<KeyModel>> getKeyboards()
  {
    return keyboards;
  }

  /** Switching, using the SPECIAL_SWITCH_TO key. */
  private void switchKeyboard(String imgFile)
  {
    curKeyboard = imgFile;
    keyboardView.updateKeyboard(imgFile);
  }

  @Override
  public void show(Component focusComponent)
  {
    this.focusComponent = focusComponent;
    FrameFactory.getFrame().getContent().setContextMenu(keyboardView.getMenu());
  }

  // public void hide()
  // {
  //   if (!active) { return; }
  //   GuiLoader.getInstance().getForm().remove(panel);
  // }

  private static boolean onPressed(SoftKeyboard sk, int x, int y)
  {
    if (sk.skipIdenticalTouch && sk.lastX == x && sk.lastY == y)
    {
      System.out.println("Skip-Touch-Release");
      return true;
    }
    sk.lastX = x;
    sk.lastY = y;
    x = (int)(sk.keyboardView.scalerX * x);
    y = (int)(sk.keyboardView.scalerY * y);
    System.out.println("SoftKeyboard.onPressed(" + x + "/" + y + ")");
    for (KeyModel km : sk.keyboards.get(sk.curKeyboard))
    {
      if (km.x > x) { continue; }
      if (km.x+km.w < x) { continue; }
      if (km.y > y) { continue; }
      if (km.y+km.h < y) { continue; }
      if (km.special != null)
      {
        // if (km.special.equals(SPECIAL_DEL))
        // {
        //   GuiLoader.getInstance().getForm().keyEventGlfw(259, 8, 1, 0);
        // }
        // else if (km.special.equals(SPECIAL_ENTER))
        // {
        //   GuiLoader.getInstance().getForm().keyEventGlfw(257, 13, 1, 0);
        // }
        //else 
        if (km.special.startsWith(SPECIAL_SWITCH_TO))
        {
          String newKb = km.special.substring(SPECIAL_SWITCH_TO.length());
          sk.switchKeyboard(newKb);
        }
      }
      else
      {
        sk.focusComponent.onAction(Action.fromKeyTyped(km.c), 0, 0);
      }
    }
    return true;
  }

  /** Fills the KeyList.
   * @param up Using 32 for upper case, otherwise 0. */
  private ArrayList<KeyModel> createKeyList(boolean upperChar)
  {
    ArrayList<KeyModel> keyList = new ArrayList<>();
    int x = 8;
    int y = 40;
    int w = 58;
    int h = 82;
    char up = upperChar ? (char)32 : 0;
    keyList.add(new KeyModel((char)('q'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('w'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('e'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('r'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('t'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('y'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('u'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('i'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('o'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('p'-up), x, y, w, h, null));
    
    x = 38;
    y = 146;
    keyList.add(new KeyModel((char)('a'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('s'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('d'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('f'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('g'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('h'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('j'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('k'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('l'-up), x, y, w, h, null));
    
    x=102;
    y=250;
    keyList.add(new KeyModel((char)('z'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('x'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('c'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('v'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('b'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('n'-up), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('m'-up), x, y, w, h, null)); x+=63;
    
    //keyList.add(new KeyModel(' ', x, y, 2*w, h, SPECIAL_DEL)); // Delete
    keyList.add(new KeyModel('\b', x, y, 2*w, h, null)); // Delete
    String spec = SPECIAL_SWITCH_TO + PIX_KEYB_DEF_UP;
    if (up > 0) { spec = SPECIAL_SWITCH_TO + PIX_KEYB_DEF; }
    keyList.add(new KeyModel(' ', 10, y, (int)(1.5f*w), h, spec)); // Shift
    
    y = 360;
    x = 230;
    keyList.add(new KeyModel(' ', x, y, 4*w, h, null)); x+=(4*w);// Space
    //keyList.add(new KeyModel(' ', x, y, 3*w, h, SPECIAL_ENTER)); // Enter
    keyList.add(new KeyModel('\n', x, y, 3*w, h, null)); // Enter
    return keyList;
  }


  public static Component genActionComponent(SoftKeyboard sk)
  {
    return new Component() {
      boolean shouldRender = true;

      @Override
      public boolean intersect(int x, int y) {
        return intersectComponent(this, x, y);
      }

      @Override
      public boolean shouldRender() {
        return shouldRender;
      }

      @Override
      public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
      }

      @Override
      public IRenderer getFallbackRenderer() {
        return (c,g,x,y) -> {};
      }

      @Override
      public boolean onAction(Action action, int xShift, int yShift) {
        if (sk.keyboardView.menu.intersect(action.x + xShift, action.y + yShift))
        {
          return onPressed(sk, action.x + xShift, action.y + yShift);
        }
        else if (sk.focusComponent.onAction(action, xShift, yShift))
        {
          return true;
        }
        FrameFactory.getFrame().getContent().setContextMenu(null);
        return true;
      }
      
    };
  }

  public static class KeyboardView
  {
    boolean shouldRender = true;
    ContextMenu menu = new ContextMenu();
    Label img;
    float scalerX;
    float scalerY;

    public KeyboardView(String initialViewRes, SoftKeyboard sk)
    {
      updateKeyboard(initialViewRes);
      menu.addComponent(genActionComponent(sk), null);
    }

    public void updateKeyboard(String viewRes)
    {
      if (img != null)
      {
        menu.removeComponent(img);
      }
      img = Label.fromResource(viewRes);
      Point oriSize = img.getImage().getSize();
      Point frameSize = FrameFactory.getFrame().getContent().getSize();
      if (frameSize.x == 0 || frameSize.y == 0)
      {
        frameSize = new Point(FrameFactory.getFrame().getSize().x, FrameFactory.getFrame().getSize().y);
      }
      if (frameSize.x == 0 || frameSize.y == 0)
      {
        //Dont scale.
        scalerX = 1;
        scalerY = 1;
      }
      else
      {
        frameSize.y = frameSize.y / 4;
        scalerX = (float)oriSize.x / (float)frameSize.x;
        scalerY = (float)oriSize.y / (float)frameSize.y;
        Image newImg = img.getImage().getScaledInstance(frameSize);
        img.setImage(newImg);
      }

      menu.addComponent(img, null);
      menu.setShouldRender(true);
    }

    public ContextMenu getMenu() { return menu; }
  }
  
  class KeyModel
  {
    public int x;
    public int y;
    public int w;
    public int h;
    public char c;
    public String special; //Enter, Del, KeySwitch=name
    
    public KeyModel(char c, int x, int y, int w, int h, String special)
    {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.c = c;
      this.special = special;
    }
  }
}
