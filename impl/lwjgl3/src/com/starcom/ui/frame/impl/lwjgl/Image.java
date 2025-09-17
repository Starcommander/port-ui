/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package com.starcom.ui.frame.impl.lwjgl;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.starcom.ui.frame.impl.LwjglFrame;
import com.starcom.ui.model.Point;
import com.starcom.ui.model.Rect;

import java.io.*;
import java.nio.*;
import java.util.*;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/** STB Image demo. */
public final class Image implements com.starcom.ui.frame.Image, Closeable {

    private final ByteBuffer buffer;
    private final int textureId;
    private final int width;
    private final int height;

    public Image(InputStream in) throws IOException
    {
        // BufferedImage image = ImageIO.read(LwjglFrame.class.getResource(path));
        // int width = image.getWidth();
        // int height = image.getHeight();
		// www = width;
		// hhh = height;

        // ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        // for (int y = 0; y < height; y++) {
        //     for (int x = 0; x < width; x++) {
        //         int pixel = image.getRGB(x, y);
        //         buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
        //         buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
        //         buffer.put((byte) (pixel & 0xFF));         // Blue
        //         buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
        //     }
        // }
        // buffer.flip();


				com.starcom.ui.frame.impl.lwjgl.PNGDecoder e = new PNGDecoder(in);
				int components = 3;
				if (e.hasAlphaChannel()) { components++; }
				buffer = ByteBuffer.allocateDirect(e.getWidth() * e.getHeight() * components);
				e.decode(buffer, e.getWidth()*components, PNGDecoder.Format.RGBA);
				width = e.getWidth();
				height = e.getHeight();
				buffer.flip();

        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, e.getWidth(), e.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
//        return textureId;
    }

    private Image(ByteBuffer buffer, int textureId, int width, int height)
    {
        this.textureId = textureId;
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }
    public void doRender(float x, float y)
    {
        doRender(x, y, 0, 0, 1, 1);
    }

    public void doRender(float x, float y, Rect visibleRect)
    {
        float u = (float)visibleRect.pos.x/(float)width;
        float v = (float)visibleRect.pos.y/(float)height;
        float u2 = (float)visibleRect.size.x/(float)width;
        float v2 = (float)visibleRect.size.y/(float)height;
        u2 += u;
        v2 += v;

        doRender(x, y, u, v, u2, v2);
    }

    public void doRender(float x, float y, float u, float v, float u2, float v2)
    {
        glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glBegin(GL_QUADS);
			glTexCoord2f(u, v);
			glVertex2f(x, y);
			glTexCoord2f(u, v2);
			glVertex2f(x, y + height);
			glTexCoord2f(u2, v2);
			glVertex2f(x + width, y + height);
			glTexCoord2f(u2, v);
			glVertex2f(x + width, y);
		glEnd();
		glDisable(GL_TEXTURE_2D);
    }

    @Override
    public com.starcom.ui.frame.Image getScaledInstance(Point newSize) {
        return new Image(buffer, textureId, newSize.x, newSize.y);
    }

    @Override
    public Point getSize() {
        return new Point(width, height);
    }

    @Override
    public void close() throws IOException {
        glDeleteTextures(textureId);
    }

}