package se.chalmers.plotgen;

import java.util.Random;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;

public class PlotTest {

	public static void main(String[] args) {
		PlotEngine plotEngine = new PlotEngine(new Random().nextLong());
		
		System.out.println("Actors:");
		for (Actor actor : plotEngine.getActors()) {
			System.out.println(actor);
		}
		
		System.out.println("\nProps:");
		for (Prop prop : plotEngine.getProps()) {
			System.out.println(prop);
		}
		
		System.out.println("\nScenes:");
		for (Scene scene : plotEngine.getScenes()) {
			System.out.println(scene);
		}
		
		System.out.println("\n" + plotEngine.getPlotLine());
		
		System.out.println("\nActive vertex:\n" + plotEngine.getCurrentNode().getText());
		
		PlotEdge takeAction = null;
		System.out.println("\nPossible actions:");
		System.out.println(plotEngine.getPossibleAction());
		
		System.out.println("\nVertex after taking the action:\n" + plotEngine.takeAction());
		
	}
}
