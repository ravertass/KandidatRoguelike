package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Camera;

public class HighlightSystem implements ISystem {

	ArrayList<Entity> entities;

	Input i;

	Camera camera;

	public HighlightSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if (Mouse.isButtonDown(1) && i.getNextMouseClick() == 0) {
				System.out.println("FIRE!!");
				i.resetMouse();

			} else if (i.getNextMouseClick() == 0) {
				e.getComponent(Position.class).set(
						(i.getNextMouseClickPos().getFirst() / 16)
								+ camera.getPosition().getX(),
						(i.getNextMouseClickPos().getSecond() / 16)
								+ camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);
				i.resetMouse();
			} else if (Mouse.isButtonDown(1)) {

				ArrayList<Position> line = calculateLine(new Position(10, 10),
						new Position((Mouse.getX() / 16)
								+ camera.getPosition().getX(),
								(Mouse.getY() / 16)
										+ camera.getPosition().getY()));

				// System.out.println("Line: " + line);

				for (Position pos : line) {
					// Insert code for highlighting all the tiles
				}

				e.getComponent(Position.class).set(
						(Mouse.getX() / 16) + camera.getPosition().getX(),
						(Mouse.getY() / 16) + camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);

			} else if (!Mouse.isButtonDown(1)) {
			}
		}

	}

	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);

	}

	public void setCamera(Camera c) {
		this.camera = c;
	}

	/**
	 * Returns a list of all the positions between and including the two input positions.
	 * @param pos0 The First Position.
	 * @param pos1 The Target Position.
	 * @return Returns a list of positions.
	 */
	public ArrayList<Position> calculateLine(Position pos0, Position pos1) {
		return calculateLine(pos0.getX(), pos0.getY(), pos1.getX(), pos1.getY());
	}
	
	/**
	 * Returns a list of all the positions between and including the two input positions.
	 * @param x0 X value of the First Position.
	 * @param y0 Y value of the First Position.
	 * @param x1 X value of the Second Position.
	 * @param y1 Y value of the Second Position.
	 * @return Returns a list of positions.
	 */
	public ArrayList<Position> calculateLine(int x0, int y0, int x1, int y1) {
		ArrayList<Position> line = new ArrayList<Position>();
		
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int x = x0;
		int y = y0;
		int n = 1 + dx + dy;
		int x_inc = (x1 > x0) ? 1 : -1;
		int y_inc = (y1 > y0) ? 1 : -1;
		int error = dx - dy;
		dx *= 2;
		dy *= 2;
		for (; n > 0; --n) {
			line.add(new Position(x, y));

			if (error > 0) {
				x += x_inc;
				error -= dy;
			} else if (error == 0) {
				x += x_inc;
				y += y_inc;
				error += dx;
				error -= dy;
				--n;
			} else {
				y += y_inc;
				error += dx;
			}
		}
		return line;
	}
}
