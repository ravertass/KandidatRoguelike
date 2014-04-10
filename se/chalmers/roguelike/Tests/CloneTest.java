package se.chalmers.roguelike.Tests;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;

public class CloneTest {

	public static void main(String[] args) {
		Entity foo = new Entity("bork");
		foo.add(new Health(10));
		Entity bar = foo.clone();
		
		Health hp1 = foo.getComponent(Health.class);
		Health hp2 = bar.getComponent(Health.class);
		
		System.out.println("foo: "+hp1.getHealth()+" bar: "+hp2.getHealth());
		hp2.setHealth(9);
		System.out.println("foo: "+hp1.getHealth()+" bar: "+hp2.getHealth());

	}

}
