package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Entities.Entity;

public class HighlightSystem implements ISystem {
	

	ArrayList<Entity> entities;
	
	Input i;
	
	Entity camera;
	
	public HighlightSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if(i.getNextMouseClick() == 0) {
				System.out.println(i.getNextMouseClickPos().getFirst()/32);
				System.out.println(i.getNextMouseClickPos().getSecond()/32);
				e.getComponent(Position.class).set(i.getNextMouseClickPos().getFirst()/32, i.getNextMouseClickPos().getSecond()/32);
				e.getComponent(Sprite.class).setVisibility(true);
				i.resetMouse();
			} else if (Mouse.isButtonDown(1)) {
				e.getComponent(Position.class).set(Mouse.getX()/32, Mouse.getY()/32);
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
	
	public void setCamera(Entity c) {
		this.camera = c;
	}
	

}
