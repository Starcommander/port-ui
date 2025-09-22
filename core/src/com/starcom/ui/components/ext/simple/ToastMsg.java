package com.starcom.ui.components.ext.simple;

import com.starcom.ui.frame.FrameFactory;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

public class ToastMsg implements Runnable{
    public static void show(String txt, int durationMS)
    {
        ToastMsg msg = new ToastMsg(txt, durationMS);
        FrameFactory.getFrame().getContent().extraRenderers.add(msg);
    }

    String txt;
    int durationMS;
    int curY = 0;
    Image fontImg;
    long curTime;
    long curDur;
    boolean moveOut = false;

    private ToastMsg(String txt, int durationMS)
    {
        this.txt = txt;
        this.durationMS = durationMS;
        fontImg = FrameFactory.getFrame().getGraphicsImpl().newFont().genTextImage(txt, Color.BLACK);
        curTime = System.currentTimeMillis();
        System.out.println("I am added");
    }

    public long getFPS()
    {
        long oldTime = curTime;
        curTime = System.currentTimeMillis();
        curDur += curTime - oldTime;
        return curTime - oldTime;
    }

    @Override
    public void run() {
        IFrameGraphics gr = FrameFactory.getFrame().getGraphicsImpl();
        Point frameSize = FrameFactory.getFrame().getSize();
        if (curY < frameSize.y/4 && !moveOut)
        { // Move in
            curY += getFPS()/3;
        }
        else if (curY < 0)
        { // End reached
            FrameFactory.getFrame().getContent().extraRenderers.remove(this);
        }
        else if (curDur >= durationMS)
        { // Move out
            moveOut = true;
            curY -= getFPS()/3;
        }
        else
        { // Keep for some time.
            getFPS();
        }
        int x = frameSize.x/2 - fontImg.getSize().x/2;
        int y = frameSize.y - curY;
        gr.drawFilledRect(Color.GRAY_BRIGHT_BRIGHT, x, y, fontImg.getSize().x, fontImg.getSize().y);
        gr.drawImage(fontImg, x, y);
    }
    
}
