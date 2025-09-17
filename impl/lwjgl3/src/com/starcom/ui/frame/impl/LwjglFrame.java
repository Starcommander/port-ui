package com.starcom.ui.frame.impl;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.nio.*;

import javax.imageio.ImageIO;

import com.starcom.ui.components.RootContainer;
import com.starcom.ui.frame.IFrame;
import com.starcom.ui.frame.IFrameGraphics;
import com.starcom.ui.frame.Image;
import com.starcom.ui.frame.impl.lwjgl.PNGDecoder;
import com.starcom.ui.model.Action;
import com.starcom.ui.model.Color;
import com.starcom.ui.model.Point;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LwjglFrame implements IFrame {

	// The window handle
	private long window;
	private GLFWMouseButtonCallback oldMouseButtonCallback;
	private GLFWKeyCallback oldKeyCallback;
	private GLFWCharCallback oldCharCallback;
	private GLFWScrollCallback oldScrollCallback;
	private GLFWCursorPosCallback oldCursorPosCallback;
	//private GLFWWindowSizeCallback oldWindowResizeCallback;
	private GLFWWindowRefreshCallback oldWindowRefreshCallback;
	private boolean shouldCleanup = false;
	LwjglGraphics graphics;
	RootContainer content = new RootContainer();
	Point mousePos = new Point();

	private void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		loop();

	}

	public LwjglFrame()
	{
		init();
		initCallbacks();
	}

	public LwjglFrame(long window)
	{
		this.window = window;
		initCallbacks();
		graphics = new LwjglGraphics(window);
	}

	private void initCallbacks()
	{
		oldMouseButtonCallback = glfwSetMouseButtonCallback(window, GLFWMouseButtonCallback.create((win,but,act,mod) -> onMouseButton(but, act, mod) ));
		oldKeyCallback = glfwSetKeyCallback(window, GLFWKeyCallback.create( (win, key, scancode, action, mod) -> onKey(key, scancode, action, mod) ));
		oldCharCallback = glfwSetCharCallback(window, GLFWCharCallback.create( (w,c) -> onChar(c) ));
		oldScrollCallback = glfwSetScrollCallback(window, GLFWScrollCallback.create( (w,x,y) -> onScroll(x,y) ));
		oldCursorPosCallback = glfwSetCursorPosCallback(window, GLFWCursorPosCallback.create( (w,x,y) -> onCursorPos(x,y) ));
		//oldWindowResizeCallback = glfwSetWindowSizeCallback(window, (w,x,y) -> onWindowResize(x,y));
		oldWindowRefreshCallback = glfwSetWindowRefreshCallback(window, (w) -> onWindowRefresh());
	}

	private void onWindowRefresh()
	{
		content.setShouldRender(true);
		if (oldWindowRefreshCallback != null) { oldWindowRefreshCallback.invoke(window); }
	}
	// private void onWindowResize(int x, int y)
	// {
	// 	content.setShouldRender(true);
	// 	if (oldWindowResizeCallback != null) { oldWindowResizeCallback.invoke(window, x, y); }
	// }

	private void onCursorPos(double x, double y)
	{
		mousePos.set((int)x,(int)y);
		if (oldMouseButtonCallback != null) { oldCursorPosCallback.invoke(window, x, y); }
	}

	private void onMouseButton(int button, int action, int mod)
	{
		System.out.println("MouseAction(" +button+ "): " + action + "/" + mod );
		if (action==0)
		{
			content.onAction(Action.fromMouseReleased(mousePos.x, mousePos.y, button+1), 0, 0);
			content.onAction(Action.fromMouseClicked(mousePos.x, mousePos.y, button+1), 0, 0);
			System.out.println("Click: " + mousePos.x + "/" + mousePos.y); //TODO: Logger or cleanup
		}
		else
		{
			content.onAction(Action.fromMousePressed(mousePos.x, mousePos.y, button+1), 0, 0);
		}

		if (oldMouseButtonCallback != null) { oldMouseButtonCallback.invoke(window, button, action, mod); }
	}

	private void onKey(int key, int scancode, int action, int mod)
	{
		System.out.println("KeyAction(" +key+ "): " + scancode + "/" + action + "/" + mod );
		if (action == 0)
		{
			content.onAction(Action.fromKeyReleased(scancode), 0, 0);
		}
		else if (action == 1) // 2=hold
		{
			content.onAction(Action.fromKeyPressed(scancode), 0, 0);
		}
		if (oldKeyCallback != null) { oldKeyCallback.invoke(window, key, scancode, action, mod); }
	}

	private void onChar(int c)
	{
		System.out.println("CharAction(" +(char)c+ ")");
		content.onAction(Action.fromKeyTyped(c), 0, 0);
		if (oldCharCallback != null) { oldCharCallback.invoke(window, c); }
	}

	private void onScroll(double x, double y)
	{
		System.out.println("ScrollAction(" +x+ "/" + y + ")");
		content.onAction(Action.fromMouseScrolled(mousePos.x, mousePos.y, (int)y), 0, 0);
		if (oldScrollCallback != null) { oldScrollCallback.invoke(window, x,y); }
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		glfwMaximizeWindow(window);

		// Make the window visible
		glfwShowWindow(window);
		graphics = new LwjglGraphics(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
//		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) || shouldCleanup ) {
			if (content.shouldRender())
			{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			//graphics.drawLine(new com.starcom.ui.model.Color(0,255,0,255), 3, 0, 0, 500, 500);

			// Setup 2d graphics
			Point size = LwjglFrame.getSize(window);
			glLoadIdentity();
			glOrtho(0.0f, size.x, size.y, 0, -1.0f, 1.0f);
			glViewport(0, 0, size.x, size.y);

//			graphics.drawFilledRect(new com.starcom.ui.model.Color(0,255,0,255), 0, 0, 30, 30);
//graphics.drawFilledRect(Color.BLUE, 10, 250, 100, 5);

			System.out.println("Drawing");
			content.onRender(getSize().x, getSize().y, graphics);
//graphics.drawLine(Color.BLUE, 10, 10, 250, 110, 250);
//graphics.drawLine(Color.BLUE, 10, 30, 30, 260, 30);
			glfwSwapBuffers(window); // swap the color buffers
			}

			try{
			Thread.sleep(50);
			} catch (Exception e) { e.printStackTrace(); }

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
		System.out.println("Cleanup lwjgl");
		for (Closeable c : LwjglGraphics.cleanupList)
		{
			try
			{
				c.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}


		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public static void main(String[] args) {
		LwjglFrame f = new LwjglFrame();
//		f.init();
//		f.initCallbacks();
		f.run();
	}

	@Override
	public void dispose() {
		shouldCleanup = true;
	}

	@Override
	public RootContainer getContent() {
		return content;
	}

	@Override
	public IFrameGraphics getGraphicsImpl() {
		return graphics;
	}

	@Override
	public Point getMaxSize() {
		GLFWVidMode vid = GLFW.glfwGetVideoMode(glfwGetPrimaryMonitor());
		return new Point(vid.width(),vid.height());
	}

	@Override
	public Point getSize() {
		return getSize(window);
	}

	public static Point getSize(long window)
	{
		int xSize[] = new int[1];
		int ySize[] = new int[1];
		glfwGetFramebufferSize(window, xSize, ySize);
		return new Point(xSize[0], ySize[0]);
	}

	@Override
	public IFrame newSubFrame() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'newSubFrame'");
	}

	@Override
	public boolean setIcon(Image arg0) {
		//TODO: Set the icon
		return false;
	}

	@Override
	public void setSize(Point size) {
		//TODO: This should return a bool?
		glfwSetWindowSize(window, size.x, size.y);
	}

	@Override
	public boolean setTitle(String title) {
		glfwSetWindowTitle(window, title);
		return true;
	}

	@Override
	public void setVisible(boolean vis) {
		if (vis)
		{
			glfwShowWindow(window);
			loop(); //TODO: loop does block, right?
		}
		else
		{
			glfwHideWindow(window);
		}
	}

}
