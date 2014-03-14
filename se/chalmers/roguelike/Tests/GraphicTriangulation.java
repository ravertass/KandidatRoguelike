package se.chalmers.roguelike.Tests;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Circle;

import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.ModifiedGenerator;
import se.chalmers.roguelike.util.DelauneyTriangulator;
import se.chalmers.roguelike.util.Edge;
import se.chalmers.roguelike.util.KruskalMST;
import se.chalmers.roguelike.util.Triangle;

public class GraphicTriangulation {
	private long lastFrame, lastFPS;
	private int fps, height, width;
	private boolean buttonPressed = false;
	private ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
	private DelauneyTriangulator dTri;
	private ArrayList<Edge> edges1 = new ArrayList<Edge>();
	
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
		
		//delauney
		Triangle superTri = Triangle.getSuperTriangle(height, width, 0, 0);
		dTri = new DelauneyTriangulator(superTri);
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
	//		System.out.println("frig off mr lahey, X: " + Mouse.getX() + " Y: "	+ y);
			// blocks.add(int i)
			
			Rectangle newBlock = new Rectangle(Mouse.getX(), y, 3, 3);
			blocks.add(newBlock);
			
			//Delauney stuff
			ArrayList<Position> nodes = new ArrayList<Position>();
			for (Rectangle block : blocks) {
				nodes.add(new Position(block.x + 1, block.y + 1));
			}
			edges1 = dTri.triangulate(nodes);
			System.out.println("Edges after triangulation: " + edges1.size());
			edges1 = new KruskalMST().createMST(edges1);
			System.out.println("Edges after MST: " + edges1.size());
			
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
		renderTriangulation();
	}

	public void renderTriangulation(){
		
/*		Triangle tri1 = new Triangle(-1944, 600, 400, -692, 2744, 600);
		Triangle tri2 = new Triangle(400, -692, 2744, 600, -1944, 600);
		Triangle tri3 = new Triangle(2744, 600, -1944, 600, 400, -692);
		
		Circle cir1 = tri1.circumCircle();
		System.out.println("Circlecenter of triangle " + tri1 + " is: (" + cir1.getCenterX() +", " + cir1.getCenterY() + ") with radius " + cir1.getRadius());
		Circle cir2 = tri2.circumCircle();
		System.out.println("Circlecenter of triangle " + tri2 + " is: (" + cir2.getCenterX() +", " + cir2.getCenterY() + ") with radius " + cir2.getRadius());
		Circle cir3 = tri3.circumCircle();
		System.out.println("Circlecenter of triangle " + tri3 + " is: (" + cir3.getCenterX() +", " + cir3.getCenterY() + ") with radius " + cir3.getRadius());
*/		
		//For testing circumcircling
//		ArrayList<Triangle> triangles = dTri.getTriangles();
//		for (Triangle tri : triangles) {
//			Circle circle = tri.circumCircle();
//			DrawCircle(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), 300);
//		}
		
		GL11.glPushMatrix();
		for (Edge edge : edges1) {
			GL11.glColor3f(0, 0, 0);
			glBegin(GL_LINES);
			glVertex2i(edge.getX1(), edge.getY1());
			glVertex2i(edge.getX2(), edge.getY2());
			glEnd();
		}
		GL11.glPopMatrix();
	}
	
	void DrawCircle(double cx, double cy, double r, int num_segments) 
	{ 
		double theta = (2 * 3.1415926 / num_segments); 
		double c = Math.cos((theta));
		double s = Math.sin(theta);
		double t;

		double x = r; //we start at angle = 0 
		double y = 0; 
	    
		glBegin(GL_LINE_LOOP); 
		for(int ii = 0; ii < num_segments; ii++) 
		{ 
			glVertex2d(x + cx, y + cy);//output vertex 
	        
			//apply the rotation matrix
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		} 
		glEnd(); 
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
