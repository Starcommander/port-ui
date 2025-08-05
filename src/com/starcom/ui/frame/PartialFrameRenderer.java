package com.starcom.ui.frame;

import com.starcom.ui.model.Color;
import com.starcom.ui.model.Rect;
import com.starcom.ui.model.Point;

import java.io.InputStream;

public class PartialFrameRenderer implements IFrameRenderer
{
  IFrameRenderer parent;
  PartialFrameRenderer child;
  Rect visibleRect;
  Rect tmpRect1 = new Rect(0,0,0,0);
  Rect tmpRect2 = new Rect(0,0,0,0);
  Point tmpPoint1 = new Point();
  Point tmpPoint2 = new Point();

  /** Must be updated via set(p,v) */
  public PartialFrameRenderer() {}
  
  public void set(IFrameRenderer parent, Rect visibleRect)
  {
    this.parent = parent;
    this.visibleRect = visibleRect;
    if (parent instanceof PartialFrameRenderer)
    {
      ((PartialFrameRenderer)parent).child = this;
    }
  }

  public void dispose()
  {
    child.parent = parent;
    if (parent instanceof PartialFrameRenderer)
    {
      ((PartialFrameRenderer)parent).child = child;
    }
  }

  @Override
  public Font newFont() { return parent.newFont(); }

  @Override
  public void drawFilledRect(Color color, int x, int y, int w, int h)
  {
    tmpRect1.set(x,y,w,h);
    intersect(tmpRect1, visibleRect, tmpRect2);
    parent.drawFilledRect(color, tmpRect2.pos.x, tmpRect2.pos.y, tmpRect2.size.x, tmpRect2.size.y);
  }

  public static void intersect(Rect r1, Rect r2, Rect store)
  {
    if (r1.pos.x > r2.pos.x) { store.pos.x = r1.pos.x; }
    else { store.pos.x = r2.pos.x; }
    if (r1.pos.y > r2.pos.y) { store.pos.y = r1.pos.y; }
    else { store.pos.y = r2.pos.y; }
    if ((r1.pos.x+r1.size.x) < (r2.pos.x+r2.size.x)) { store.size.x = (r1.pos.x+r1.size.x) - store.pos.x; }
    else { store.size.x = (r2.pos.x+r2.size.x) - store.pos.x; }
    if ((r1.pos.y+r1.size.y) < (r2.pos.y+r2.size.y)) { store.size.y = (r1.pos.y+r1.size.y) - store.pos.y; }
    else { store.size.y = (r2.pos.y+r2.size.y) - store.pos.y; }
  }

  /** Moved the point into rect.
   * @return False if outside.
   */
  public static boolean intersect(Rect rect, Point p, Point store)
  {
    boolean inside = true;
    if (rect.pos.x > p.x)
    {
      store.x = rect.pos.x;
      inside = false;
    }
    else if ((rect.pos.x+rect.size.x) < p.x)
    {
      store.x = rect.pos.x+rect.size.x;
      inside = false;
    }
    else { store.x = p.x; }
    if (rect.pos.y > p.y)
    {
      store.y = rect.pos.y;
      inside = false;
    }
    else if ((rect.pos.y+rect.size.y) < p.y)
    {
      store.y = rect.pos.y+rect.size.y;
      inside = false;
    }
    else { store.y = p.y; }
    return inside;
  }

  @Override
  public void drawLine(Color color, int th, int x1, int y1, int x2, int y2)
  {
    tmpPoint1.x = x1;
    tmpPoint1.y = y1;
    boolean inside1 = intersect(visibleRect, tmpPoint1, tmpPoint2);
    x1 = tmpPoint2.x;
    y1 = tmpPoint2.y;
    tmpPoint1.x = x2;
    tmpPoint1.y = y2;
    boolean inside2 = intersect(visibleRect, tmpPoint1, tmpPoint2);
    if (!inside1 && !inside2) { return; }
    x2 = tmpPoint2.x;
    y2 = tmpPoint2.y;
    parent.drawLine(color, th, x1, y1, x2, y2);
  }

  @Override
  public void drawImage(Image img, int x, int y)
  {
    tmpRect1.set(x, y, img.getSize().x, img.getSize().y);
    intersect(visibleRect, tmpRect1, tmpRect2);
    if (tmpRect1.equals(tmpRect2))
    {
      parent.drawImage(img,x,y);
    }
    else
    {
      if (tmpRect2.size.x <= 0 || tmpRect2.size.y <= 0) { return; } // Skip render inivisible
      tmpRect2.pos.x = tmpRect2.pos.x - x;
      tmpRect2.pos.y = tmpRect2.pos.y - y;
      parent.drawPartialImage(img, x, y, tmpRect2);
    }
  }

  @Override
  public void drawPartialImage(Image img, int x, int y, Rect pVisibleRect)
  {
    tmpRect1.pos.x = x;
    tmpRect1.pos.y = y;
    tmpRect1.size.x = img.getSize().x;
    tmpRect1.size.y = img.getSize().y;
    intersect(visibleRect, tmpRect1, tmpRect2);
    if (tmpRect1.equals(tmpRect2))
    {
      parent.drawPartialImage(img,x,y,pVisibleRect);
    }
    else
    {
      tmpRect2.pos.x = x - tmpRect2.pos.x;
      tmpRect2.pos.y = y - tmpRect2.pos.y;
      intersect(pVisibleRect, tmpRect2, tmpRect1);
      parent.drawPartialImage(img, x, y, tmpRect1);
    }
  }

  @Override
  public Image loadImage(InputStream s)
  {
    return parent.loadImage(s);
  }
}
