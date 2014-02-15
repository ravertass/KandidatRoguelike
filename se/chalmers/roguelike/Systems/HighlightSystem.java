package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Entities.Entity;
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
			if(i.getNextMouseClick() == 0) {
				e.getComponent(Position.class).set((i.getNextMouseClickPos().getFirst()/32)+camera.getPosition().getX(), (i.getNextMouseClickPos().getSecond()/32)+camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);
				i.resetMouse();
			} else if (Mouse.isButtonDown(1)) {
				e.getComponent(Position.class).set((Mouse.getX()/32)+camera.getPosition().getX(), (Mouse.getY()/32)+camera.getPosition().getY());
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
	

}
