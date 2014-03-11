package se.chalmers.roguelike.Tests;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GraphicTriangulation {
	private long lastFrame, lastFPS;
	private int fps, height, width;
	private boolean buttonPressed = false;
	private ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();

	/**
	 * @return the system time
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * @return the time difference between the current and last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Calculates the FPS
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void start() {
		width = 800;
		height = 600;
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Convay");
			Display.setInitialBackground(1, 1, 1);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		initGL();
		getDelta(); // initierar lastFrame
		lastFPS = getTime();
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			renderGL();
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	public void update(int delta) {
		updateFPS();
		if (Mouse.isButtonDown(0) && !buttonPressed) { // ugly hack for only
														// allowing on button
														// press to go trough
			buttonPressed = true;
			int y = Display.getHeight() - Mouse.getY();
			System.out.println("frig off mr lahey, X: " + Mouse.getX() + " Y: "
					+ y);
			// blocks.add(int i)
			Rectangle newBlock = new Rectangle(Mouse.getX(), y, 25, 25);
			blocks.add(newBlock);
		} else if (!Mouse.isButtonDown(0)) {
			buttonPressed = false;
		}

	}

	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void renderGL() {
		// clear screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderBlocks();
	}

	public void renderBlocks() {

		GL11.glPushMatrix();
		for (Rectangle block : blocks) {
			GL11.glColor3f(0, 0, 0);
			glBegin(GL_QUADS);
			// glTexCoord2f(0, 0);
			glVertex2d(block.getX(), block.getY()); // upper left
			// glTexCoord2f(1, 0);
			glVertex2d(block.getX() + block.getWidth(), block.getY()); // upper
																				// right
			// glTexCoord2f(1, 1);
			glVertex2d(block.getX() + block.getWidth(), block.getY()
					+ block.getHeight()); // bottom right
			// glTexCoord2f(0, 1);
			glVertex2d(block.getX(), block.getY() + block.getHeight()); // bottom
																					// left
			glEnd();
		}
		GL11.glPopMatrix();
	}

	public static void main(String[] argv) {
		GraphicTriangulation displayExample = new GraphicTriangulation();
		displayExample.start();
	}
}
