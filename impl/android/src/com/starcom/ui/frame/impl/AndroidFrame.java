package com.starcom.ui.frame.impl;

import com.starcom.ui.components.RootContainer;
import com.starcom.ui.components.Container;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class AndroidFrame implements IFrame
{
    RootContainer content = new RootContainer();
    Canvas canvas;
    Paint paint = new Paint();
    public AndroidFrame()
    {
    }

    public void preRender(Canvas canvas)
    {
        this.canvas = canvas;
    }

    // public void onStarActiveActivity(Activity a)
    // {
    //     a.getResources().getDrawable(0);
    // }

    @Override
    public IFrameGraphics getGraphicsImpl() {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public IFrame newSubFrame() {
        return null;
    }

    @Override
    public Point getSize() {
        return new Point(300,300);
    }

    @Override
    public Point getMaxSize() {
        return new Point(300,300);
    }

    @Override
    public void setSize(Point s) {
    }

    @Override
    public void setVisible(boolean b) {
    }

    @Override
    public RootContainer getContent() {
        return content;
    }

    @Override
    public boolean setTitle(String title) {
        return false;
    }

    @Override
    public boolean setIcon(Image icon) {
        return false;
    }
}
