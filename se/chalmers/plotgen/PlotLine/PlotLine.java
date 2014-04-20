package se.chalmers.plotgen.PlotLine;

import java.util.ArrayList;

public class PlotLine {

	private ArrayList<PlotNode> nodeList;
	private int currentNodeIndex;
	
	public PlotLine(ArrayList<PlotNode> nodeList) {
		this.nodeList = nodeList;
		currentNodeIndex = 0;
	}
	
	public PlotNode getCurrentNode() {
		return nodeList.get(currentNodeIndex);
	}
	
	public PlotNode getNextNode() {
		if (currentNodeIndex == nodeList.size()-1) {
			return null;
		}
		return nodeList.get(currentNodeIndex + 1);
	}
	
	public void incrementCurrentNode() {
		currentNodeIndex++;
	}
	
	public int getCurrentNodeIndex() {
		return currentNodeIndex;
	}
	
	public PlotNode getNode(int index) {
		return nodeList.get(index);
	}
	
	public int size() {
		return nodeList.size();
	}
	
	public ArrayList<PlotNode> getListCopy() {
		return new ArrayList<PlotNode>(nodeList);
	}
	
	@Override
	public String toString() {
		String string = "";
		for (PlotNode node : nodeList) {
			string += node.getAction();
			string += ": ";
			string += node.getText();
			string += "\n";
		}
		return string;
	}
}
