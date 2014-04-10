package se.chalmers.roguelike.Components;

public class SpaceShip implements IComponent {

	private int xGoal, yGoal, xSpeed, ySpeed;
	private double speed;
	
	public SpaceShip(double speed) {
		this.setSpeed(speed);
		xGoal = -1;
		yGoal = -1;
		xSpeed = 0;
		ySpeed = 0;
	}

	public int getxGoal() {
		return xGoal;
	}

	public void setxGoal(int xGoal) {
		this.xGoal = xGoal;
	}

	public int getyGoal() {
		return yGoal;
	}

	public void setyGoal(int yGoal) {
		this.yGoal = yGoal;
	}

	public int getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getySpeed() {
		return ySpeed;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public IComponent clone() {
		return new SpaceShip(speed);
	}

}
