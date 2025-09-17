package com.starcom.ui.frame.impl;

import java.io.Closeable;
import java.io.IOException;

import org.lwjgl.stb.STBTTBakedChar;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.Image;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;
import com.starcom.ui.frame.impl.lwjgl.Truetype;

public class LwjglFont extends Font{
    Truetype.FontData fontData = new Truetype.FontData("fonts/FiraSans-Regular.ttf");
    static final boolean KERNING_ENABLED = false;
    static final int FONT_HEIGHT = 24; //TODO: Configurable?

    @Override
    public Point calcTextSize(String text) {
        int w = (int)Truetype.getStringWidth(fontData.info, text, 0, text.length(), FONT_HEIGHT, KERNING_ENABLED);
        int h = FONT_HEIGHT;
        return new Point(w,h);
    }

    @Override
    public Image genTextImage(String text, Color color) {
        return new FontImage(text, color, fontData);
    }

    @Override
    public String[] getFonts() {
        //TODO: Configurable?
        String fonts[] = new String[1];
        fonts[0] = "FiraSans-Regular";
        return fonts;
    }


    static class FontImage implements Image, Closeable
    {
        Truetype.FontData fontData;
        String text;
        Color color;
        float w,h;
        float scaleX = 1;
        float scaleY = 1;
        STBTTBakedChar.Buffer cdata;

        public FontImage(String text, Color color, Truetype.FontData fontData)
        {
            this.text = text;
            this.color = color;
            this.fontData = fontData;
            w = Truetype.getStringWidth(fontData.info, text, 0, text.length(), FONT_HEIGHT, KERNING_ENABLED);
            h = FONT_HEIGHT;
            int BITMAP_W = Math.round(512 * 1/scaleX);
            int BITMAP_H = Math.round(512 * 1/scaleY);

            cdata = Truetype.init(fontData, BITMAP_W, BITMAP_H, FONT_HEIGHT, 1/scaleY);
            LwjglGraphics.cleanupList.add(this); //TODO: Use one cdata for all fontImages
        }

        public void doRender(long window, int x, int y)
        {
            Truetype.loopStep(window, text, x, y, 0, fontData, FONT_HEIGHT, 1/scaleX, 1/scaleY, false, KERNING_ENABLED, cdata, color);
        }

        @Override
        public Image getScaledInstance(Point scale) {
            float scaleX = scale.x/w;
            float scaleY = scale.y/h;
            FontImage fi = new FontImage(text, color, fontData);
            fi.scaleX = scaleX;
            fi.scaleY = scaleY;
            return fi;
        }

        @Override
        public Point getSize() {
            return new Point((int)(w*scaleX),(int)(h*scaleY));
        }

        @Override
        public void close() throws IOException {
            cdata.free();
        }
        
    }
    
}
