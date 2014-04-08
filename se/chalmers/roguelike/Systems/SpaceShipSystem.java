package se.chalmers.roguelike.Systems;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.SpaceShip;

public class SpaceShipSystem implements ISystem {

	private Entity spaceShip;
	
	public SpaceShipSystem() {
		
	}
	
	@Override
	public void update() {
		
		SpaceShip shipComp = spaceShip.getComponent(SpaceShip.class);
		double xDistance = shipComp.getxGoal() - spaceShip.getComponent(Position.class).getX(); 
		double yDistance = shipComp.getyGoal() - spaceShip.getComponent(Position.class).getY();
		double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
		double speed = shipComp.getSpeed();
		shipComp.setxSpeed((int) Math.round(speed * (xDistance / distance)));
		shipComp.setySpeed((int) Math.round(speed * (yDistance / distance)));
		
		//while (x != starX)
	}

	@Override
	public void addEntity(Entity entity) {
		spaceShip = entity;
	}

	@Override
	public void removeEntity(Entity entity) {
		// Let's not 
	}

}
