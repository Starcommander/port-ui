package com.starcom.ui.frame.impl;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.starcom.ui.frame.Font;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.frame.impl.LwjglFont.FontImage;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Rect;

public class LwjglGraphics implements IFrameGraphics {

    long window;
    static ArrayList<Closeable> cleanupList = new ArrayList<>(); //TODO: Cleanup the cleanup-List

    public LwjglGraphics(long window)
    {
        this.window = window;
    }

    @Override
    public void drawFilledRect(Color c, int x, int y, int width, int height) {
//        glPushMatrix();
		{

            // float scalerX = 0.5f/(float)size.x;
            // float scalerY = 0.5f/(float)size.y;
            // float scX = ((float)x * scalerX) - 1f;
            // float scY = ((float)y * scalerY);

			//glTranslatef(newX, y, 0); // Shifts the position
            //glTran
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColor4f(c.r/255f, c.g/255f, c.b/255f, c.a/255f);
//            GL11.glColor4i(c.r, c.g, c.b, c.a);

//glOrtho(0.0f, size.x, size.y, 0.0f, 0.0f, 1.0f);
//glOrtho(0.0f, 800, 800, 0.0f, 0.0f, 1.0f);

//glOrtho(0.0f, 800, 0f, 800, -1.0f, 1.0f);

 //           glOrtho(-100, 100, -100, 100, -1, 1);
            //glOrtho(0, 3000, 3000, 0, -1, 1);
			glBegin(GL11.GL_QUADS);
			{
//                glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
				 glVertex2f(x, y);
				 glVertex2f(x, y + height);
				 glVertex2f(x + width, y + height);
				 glVertex2f(x + width, y);


//				glVertex2f(10*((float)size.x/200f), 10*((float)size.y/200f));
//				glVertex2f(10*((float)size.x/200f), 80);
//GL11.glVertex2i(10, 10);
//GL11.glVertex2i(10, 10);
//GL11.glVertex2i(80, 80);
//GL11.glVertex2i(80, 10);
/*glVertex2f(250f, 250f);
glVertex2f(250f, 250f + height);
glVertex2f(250f + width, 250f + height);
glVertex2f(250f + width, 250f);
*/
//System.out.println("SizeX:" + size.x);
//glVertex2f(scaleX(size.x/2,size), size.y-50f);
//glVertex2f(scaleX(size.x/2,size), size.y-50f + height);
//glVertex2f(scaleX((size.x/2) + width,size), size.y-50f + height);
//glVertex2f(scaleX((size.x/2) + width,size), size.y-50f);
/*
glVertex2f(size.x-50f, size.y-50f);
glVertex2f(size.x-50f, size.y-50f + height);
glVertex2f(size.x-50f + width, size.y-50f + height);
glVertex2f(size.x-50f + width, size.y-50f);
*/
//				glVertex2f(scX, scY);
//				glVertex2f(scX, scY + scHeight);
//				glVertex2f(scX + scWidth, scY + scHeight);
//				glVertex2f(scX + scWidth, scY);
			}
			glEnd();
		}
//		glPopMatrix();
    }

    @Override
    public void drawImage(Image img, int x, int y) {
if (img instanceof FontImage)
{
    FontImage fi = (FontImage)img;
    fi.doRender(window, x, y);
}
else
{
        com.starcom.ui.frame.impl.lwjgl.Image lwImgBase = (com.starcom.ui.frame.impl.lwjgl.Image)img;
        lwImgBase.doRender(x,y);
}
    }

    @Override
    public void drawLine(Color c, int th, int x1, int y1, int x2, int y2) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor4f(c.r/255f, c.g/255f, c.b/255f, c.a/255f);

float xRatio = x1 - x2;
float yRatio = y1 - y2;
float length = (float)Math.sqrt(xRatio*xRatio + yRatio*yRatio);
if (th < 1) { th = 1; }
// Normalize
xRatio = xRatio / length;
yRatio = yRatio / length;
// Mult with th
xRatio *= th;
yRatio *= th;
float xyStore = xRatio;
xRatio = yRatio;
yRatio = xyStore;

        glBegin(GL11.GL_QUADS);
        {
            glVertex2f(x1 + xRatio, y1 + yRatio);
            glVertex2f(x1 - xRatio, y1 - yRatio);

            glVertex2f(x2 - xRatio, y2 - yRatio);
            glVertex2f(x2 + xRatio, y2 + yRatio);
		}
		glEnd();

    }

    @Override
    public void drawPartialImage(Image img, int x, int y, Rect visibleRect) {
        if (img instanceof FontImage)
        {
            FontImage fi = (FontImage)img;
            fi.doRender(window, x, y); //TODO: Implement partial draw.
        }
        else
        {
            com.starcom.ui.frame.impl.lwjgl.Image lwImgBase = (com.starcom.ui.frame.impl.lwjgl.Image)img;
            lwImgBase.doRender(x,y,visibleRect);
        }
    }

    @Override
    public Image loadImage(InputStream is) {
        try
        {
          Image img = new com.starcom.ui.frame.impl.lwjgl.Image(is);
          cleanupList.add((Closeable)img);
          return img;
        }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Font newFont() {
        return new LwjglFont();
    }

    
}
