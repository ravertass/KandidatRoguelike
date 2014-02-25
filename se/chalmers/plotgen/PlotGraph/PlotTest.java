package se.chalmers.plotgen.PlotGraph;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.util.DirectedGraph;

public class PlotTest {

	public static void main(String[] args) {
		new PlotTest();
	}

	public PlotTest() {
		DirectedGraph<PlotVertex, PlotEdge> plotGraph = new DirectedGraph<PlotVertex, PlotEdge>();
		
		Actor sonic = new Actor("Sonic");
		Actor darth = new Actor("Darth Vader");
		Prop sword = new Prop("sword");
		
		PlotVertex startVertex = new PlotVertex("Once upon a time");
		plotGraph.addVertex(startVertex);
		plotGraph.setRootVertex(startVertex);
		
		Action gift = new Action(Action.ActionType.GIVE, darth, sonic, sword);
		PlotEdge firstEdge = new PlotEdge(gift);
		
		PlotVertex secondVertex = new PlotVertex("Sonic got the sword from Darth");
		plotGraph.addVertex(secondVertex);
		
		plotGraph.addEdge(firstEdge, startVertex, secondVertex);
		
		
		
	}
}
