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
		
		System.out.println("\n" + plotEngine.getPlotGraph());
		
		System.out.println("\nActive vertex:\n" + plotEngine.getActiveVertex().getPlotText());
		
		PlotEdge takeAction = null;
		System.out.println("\nPossible actions:");
		for (PlotEdge plotEdge : plotEngine.getPossibleActions()) {
			System.out.println(plotEdge.getAction());
			 takeAction = plotEdge;
		}
		
		try {
			System.out.println("\nVertex after taking the action:\n" + plotEngine.takeAction(takeAction));
		} catch (ImpossibleActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
