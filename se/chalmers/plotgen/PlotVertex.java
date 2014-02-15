package se.chalmers.plotgen;

/**
 * This is a vertex in a plot graph.
 * 
 * The vertex only contains a string, which is a piece of story/plot text
 * that is related to the plot vertex. The string is to be easily interpreted
 * by a human. A human should be able to read all plot texts of a graph and
 * understand the plot.
 * 
 * @author fabian
 */
public class PlotVertex {

	private String plotText;
	
	public PlotVertex(String plotText) {
		this.plotText = plotText;
	}
	
	public String getPlotText() {
		return plotText;
	}
}
