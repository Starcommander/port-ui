package com.starcom.ui.keyboard;

import java.util.ArrayList;
import java.util.HashMap;

import com.starcom.ui.components.Component;
import com.starcom.ui.components.ext.simple.ContextMenu;
import com.starcom.ui.components.ext.simple.Label;
import com.starcom.ui.components.ext.simple.TextField;
import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Action.AType;
import com.starcom.ui.render.IRenderer;

public class SoftKeyboard implements IKeyboard
{
  // public static final String SPECIAL_DEL = "d";
  // public static final String SPECIAL_ENTER = "e";
  public static final String SPECIAL_SWITCH_TO = "s=";
  private static final String PIX_KEYB_DEF = "/keyb-def.png";
  private static final String PIX_KEYB_DEF_UP = "/keyb-def-up.png";
  private static final String PIX_KEYB_DEF_NUM = "/keyb-def-num.png";

  HashMap<String,ArrayList<KeyModel>> keyboards = new HashMap<>();
  KeyboardView keyboardView = new KeyboardView(this);

  public SoftKeyboard()
  {
    keyboards.put(PIX_KEYB_DEF, createKeyList(false));
    keyboards.put(PIX_KEYB_DEF_UP, createKeyList(true));
    keyboards.put(PIX_KEYB_DEF_NUM, createKeyListNum());
  }

  /** *  List of keyboards.
   * <br>The hashmap contains the image-file-name with appendant keyModel-list.
   * @return The whole list. */
  public HashMap<String,ArrayList<KeyModel>> getKeyboards()
  {
    return keyboards;
  }

  @Override
  public void show(TextField focusComponent)
  {
    keyboardView.getMenu().setFocusComponent(focusComponent);
    keyboardView.updateKeyboard(PIX_KEYB_DEF);
    FrameFactory.getFrame().getContent().setContextMenu(keyboardView.getMenu());
    focusComponent.setCursorVisible(true);
  }

  private static boolean onPressed(SoftKeyboard sk, int x, int y)
  {
    x = (int)(sk.keyboardView.scalerX * x);
    y = (int)(sk.keyboardView.scalerY * y);
    System.out.println("SoftKeyboard.onPressed(" + x + "/" + y + ")");
    for (KeyModel km : sk.keyboards.get(sk.keyboardView.getViewRes()))
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
          sk.keyboardView.updateKeyboard(newKb);
        }
      }
      else
      {
        sk.keyboardView.getMenu().getFocusComponent().onAction(Action.fromKeyTyped(km.c), 0, 0);
        System.out.println("Entered: " + km.c); //TODO: Logger, or delete
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
    spec = SPECIAL_SWITCH_TO + PIX_KEYB_DEF_NUM;
    keyList.add(new KeyModel(' ', 10, y, (int)(1.5f*w), h, spec)); // NUM
    keyList.add(new KeyModel(' ', x, y, 4*w, h, null)); x+=(4*w);// Space
    //keyList.add(new KeyModel(' ', x, y, 3*w, h, SPECIAL_ENTER)); // Enter
    keyList.add(new KeyModel('\n', x, y, 3*w, h, null)); // Enter
    return keyList;
  }

  private ArrayList<KeyModel> createKeyListNum()
  {
    ArrayList<KeyModel> keyList = new ArrayList<>();
    int x = 8;
    int y = 40;
    int w = 58;
    int h = 82;
    keyList.add(new KeyModel((char)('1'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('2'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('3'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('4'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('5'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('6'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('7'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('8'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('9'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('0'), x, y, w, h, null));
    
    x = 38;
    y = 146;
    keyList.add(new KeyModel((char)('.'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)(','), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)(':'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)(';'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('-'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('_'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('#'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('+'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('*'), x, y, w, h, null));
    
    x=102;
    y=250;
    keyList.add(new KeyModel((char)('?'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('='), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('/'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('\\'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('('), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)(')'), x, y, w, h, null)); x+=63;
    keyList.add(new KeyModel((char)('"'), x, y, w, h, null)); x+=63;
    
    //keyList.add(new KeyModel(' ', x, y, 2*w, h, SPECIAL_DEL)); // Delete
    keyList.add(new KeyModel('\b', x, y, 2*w, h, null)); // Delete
    
    y = 360;
    x = 230;

    String spec = SPECIAL_SWITCH_TO + PIX_KEYB_DEF;
    keyList.add(new KeyModel(' ', 10, y, (int)(1.5f*w), h, spec)); // NUM

    keyList.add(new KeyModel(' ', x, y, 4*w, h, null)); x+=(4*w);// Space
    //keyList.add(new KeyModel(' ', x, y, 3*w, h, SPECIAL_ENTER)); // Enter
    keyList.add(new KeyModel('\n', x, y, 3*w, h, null)); // Enter
    return keyList;
  }

  public boolean onActionInternal(Action action, int xShift, int yShift) {
    System.out.println("SoftKick"); //TODO clear
    if (action.type != Action.AType.MouseClicked) { return false; }
    if (Component.intersectComponent(keyboardView.menu, action.x, action.y))
    {
      System.out.println("Is intersecting"); //TODO clear
      return onPressed(this, action.x + xShift, action.y + yShift);
    }
    TextField tf = (TextField)keyboardView.getMenu().getFocusComponent();
    Point absPos = tf.getAbsolutePos();
    tf.setCursorLocation(action.x - absPos.x, action.y - absPos.y);
    return true;
  }

  public static class KeyboardView
  {
    boolean shouldRender = true;
    ContextMenu menu = new ContextMenu();
    Label img;
    float scalerX;
    float scalerY;
    String viewRes;

    /** Creates an empty keyboardView, set with updateKeyboard(str) */
    public KeyboardView(SoftKeyboard sk)
    {
      menu.setActionListener((a,x,y) -> sk.onActionInternal(a,x,y));
      menu.setOnHide(() -> ((TextField)menu.getFocusComponent()).setCursorVisible(false) );
    }

    public String getViewRes() { return viewRes; }

    public void updateKeyboard(String viewRes)
    {
      if (img != null)
      {
        menu.removeComponent(img);
      }
      this.viewRes = viewRes;
      img = Label.fromResource(viewRes);
      Point oriSize = img.getImage().getSize();
      Point frameSize = FrameFactory.getFrame().getContent().getSize();
      if (frameSize.x == 0 || frameSize.y == 0)
      {
        //Dont scale.
        scalerX = 1;
        scalerY = 1;
        System.out.println("Update KeyboardView while Component size still 0"); //TODO: WARN
      }
      else
      {
        frameSize.y = frameSize.y / 4;
        menu.getPos().y = frameSize.y*3;
        scalerX = (float)oriSize.x / (float)frameSize.x;
        scalerY = (float)oriSize.y / (float)frameSize.y;
        Image newImg = img.getImage().getScaledInstance(frameSize);
        img.setImage(newImg);
      }

      menu.addComponent(img, null);
      menu.getLayoutManager().pack(menu);
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
