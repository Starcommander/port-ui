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
  Point tmpPoint3 = new Point();

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

  /** Moves both points into rect.
   * @return False if completly outside and does not intersect.
   */
  public static boolean intersect(Rect rect, Point p1, Point p2)
  {
    boolean p1Inside = isInside(rect, p1);
    boolean p2Inside = isInside(rect, p2);
    if (p1Inside && p2Inside) { return true; }

    //TOP
    Point rp1 = new Point(rect.pos.x, rect.pos.y);
    Point rp2 = new Point(rect.pos.x + rect.size.x, rect.pos.y);
    Point intersection = lineLineIntersection(rp1, rp2, p1, p2, true, true);
    if (intersection == null) {}
    else if (p1Inside)
    {
      p2.set(intersection.x, intersection.y);
      return true;
    }
    else if (p2Inside)
    {
      p1.set(intersection.x, intersection.y);
      return true;
    }
    else if (p1.y < rect.pos.y)
    {
      p1.set(intersection.x, intersection.y);
      p1Inside = true;
    }
    else
    {
      p2.set(intersection.x, intersection.y);
      p2Inside = true;
    }

    //BOTTOM
    rp1.set(rect.pos.x, rect.pos.y + rect.size.y);
    rp2.set(rect.pos.x + rect.size.x, rect.pos.y + rect.size.y);
    intersection = lineLineIntersection(rp1, rp2, p1, p2, true, true);
    if (intersection == null) {}
    else if (p1Inside)
    {
      p2.set(intersection.x, intersection.y);
      return true;
    }
    else if (p2Inside)
    {
      p1.set(intersection.x, intersection.y);
      return true;
    }
    else if (p1.y > rect.pos.y)
    {
      p1.set(intersection.x, intersection.y);
      p1Inside = true;
    }
    else
    {
      p2.set(intersection.x, intersection.y);
      p2Inside = true;
    }

    //Left
    rp1.set(rect.pos.x, rect.pos.y);
    rp2.set(rect.pos.x, rect.pos.y + rect.size.y);
    intersection = lineLineIntersection(rp1, rp2, p1, p2, true, true);
    if (intersection == null) {}
    else if (p1Inside)
    {
      p2.set(intersection.x, intersection.y);
      return true;
    }
    else if (p2Inside)
    {
      p1.set(intersection.x, intersection.y);
      return true;
    }
    else if (p1.x < rect.pos.x)
    {
      p1.set(intersection.x, intersection.y);
      p1Inside = true;
    }
    else
    {
      p2.set(intersection.x, intersection.y);
      p2Inside = true;
    }

    //RIGHT
    rp1.set(rect.pos.x + rect.size.x, rect.pos.y);
    rp2.set(rect.pos.x + rect.size.x, rect.pos.y + rect.size.y);
    intersection = lineLineIntersection(rp1, rp2, p1, p2, true, true);
    if (intersection == null) {}
    else if (p1Inside)
    {
      p2.set(intersection.x, intersection.y);
      return true;
    }
    else if (p2Inside)
    {
      p1.set(intersection.x, intersection.y);
      return true;
    }
    else if (p1.x > rect.pos.x)
    {
      p1.set(intersection.x, intersection.y);
      p1Inside = true;
    }
    else
    {
      p2.set(intersection.x, intersection.y);
      p2Inside = true;
    }
    return false;
  }

  public static boolean isInside(Rect rect, Point p)
  {
    if (p.x < rect.pos.x) { return false; }
    if (p.x > (rect.pos.x + rect.size.x)) { return false; }
    if (p.y < rect.pos.y) { return false; }
    if (p.y > (rect.pos.y + rect.size.y)) { return false; }
    return true;
  }

  public static Point lineLineIntersection(Point pa, Point pb, Point pc, Point pd, boolean limitAB, boolean limitCD)
  {
      double a1 = pb.y - pa.y;
      double b1 = pa.x - pb.x;
      double c1 = a1*(pa.x) + b1*(pa.y);

      double a2 = pd.y - pc.y;
      double b2 = pc.x - pd.x;
      double c2 = a2*(pc.x)+ b2*(pc.y);
     
      double determinant = a1*b2 - a2*b1;
      if (determinant == 0)
      { // The lines are parallel.
          return null;
      }
      else
      {
          double x = (b2*c1 - b1*c2)/determinant;
          double y = (a1*c2 - a2*c1)/determinant;
          if (limitAB)
          {
            if (x < pa.x && x < pb.x) { return null; } // Intersection left of lineCD
            if (x > pa.x && x > pb.x) { return null; } // Intersection right of lineCD
            if (y < pa.y && y < pb.y) { return null; } // Intersection over lineCD
            if (y > pa.y && y > pb.y) { return null; } // Intersection under lineCD
          }
          if (limitCD)
          {
            if (x < pc.x && x < pd.x) { return null; } // Intersection left of lineCD
            if (x > pc.x && x > pd.x) { return null; } // Intersection right of lineCD
            if (y < pc.y && y < pd.y) { return null; } // Intersection over lineCD
            if (y > pc.y && y > pd.y) { return null; } // Intersection under lineCD
          }
          return new Point((int)x, (int)y);
      }
    }

  @Override
  public void drawLine(Color color, int th, int x1, int y1, int x2, int y2)
  {
    tmpPoint1.x = x1;
    tmpPoint1.y = y1;
    tmpPoint2.x = x2;
    tmpPoint2.y = y2;
    boolean inside = intersect(visibleRect, tmpPoint1, tmpPoint2);
    if (!inside) { return; }
    parent.drawLine(color, th, tmpPoint1.x, tmpPoint1.y, tmpPoint2.x, tmpPoint2.y);
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
