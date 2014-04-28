package se.chalmers.plotgen.PlotLine;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;

public class PlotTextGenerator {

	private static final String plotTextsLocation = "resources/plottexts/";
	private static final String visitsFile = plotTextsLocation + "visits";

	private static final String killText = 
			"After an intense battle, @a defeated @t and ascended from the depths of @s.";
	private static final String takeText =
			"Deep in the dungeons of the @s planet, @a found the @t.";
	private static final String giveGiveText =
			"@a gives the @t to @b. \n \"Thank you!\", @b says.";
	private static final String giveReceiveText =
			"@a receives the @t from @b. \n \"Use it well!\", @b says."; 

	private ArrayList<PlotNode> nodeList;
	private Actor mainCharacter;
	private Actor finalBoss;
	private Prop finalLoot;

	public PlotTextGenerator(PlotLine plotLine, Actor mainCharacter) {
		this.nodeList = plotLine.getListCopy();
		this.mainCharacter = mainCharacter;
		
		Action lastAction = nodeList.get(nodeList.size()-2).getAction();
		if (lastAction.getActionType() == Action.ActionType.KILL) {
			// The goal in the game is to kill the final boss
			finalBoss = lastAction.getObjectActor();
			finalLoot = null;
		} else if (lastAction.getActionType() == Action.ActionType.TAKE || 
				lastAction.getActionType() == Action.ActionType.GIVE) {
			// The goal in the game is to get the final loot
			// TODO: You shouldn't be able to be given the final loot
			finalLoot = lastAction.getObjectProp();
			finalBoss = null;
		}
	}

	public PlotLine generatePlotText() {
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setText(actionToText(i));
		}
		return new PlotLine(nodeList);
	}

	private String actionToText(int i) {
		PlotNode node = nodeList.get(i);

		String plotText = null;
		
		Action.ActionType type = node.getAction().getActionType();
		if (type == Action.ActionType.VISIT) {
			plotText = visitAction(i);
		} else if (type == Action.ActionType.KILL) {
			plotText = killAction(i);
		} else if (type == Action.ActionType.TAKE) {
			plotText = takeAction(i);
		} else if (type == Action.ActionType.GIVE) {
			plotText = giveAction(i);
		} else if (type == Action.ActionType.MEET) {
			plotText = meetAction(i);
		} else if (type == Action.ActionType.FIRST) {
			//TODO: The first node should hold meaningful text
			plotText = "This is a story";
		} else if (type == Action.ActionType.LAST) {
			//TODO: The last node should hold meaningful text
			plotText = "That was a story, the end!";
		}

		return plotText;
	}

	private String meetAction(int i) {
		Actor speaker;
		if (nodeList.get(i).getAction().getObjectActor() == mainCharacter) {
			speaker = nodeList.get(i).getAction().getSubjectActor();
		} else {
			speaker = nodeList.get(i).getAction().getObjectActor();
		}
		// TODO: This text should be more varied
		String plotText = mainCharacter + " met " + speaker + ". \n ";
		
		String plotThing = "";
		String otherThing = "";
		String nextStar = "";
		
		// If the goal in the game is to kill the final boss
		if (finalBoss != null) {
			if (finalBoss == speaker) {
				plotText += "\"You think you can defeat me? ";
			} else {
				plotText += "\"You're looking to defeat @t? ";
			}
			plotThing = finalBoss.toString();
		}
		// If the goal in the game is to take the final loot
		if (finalLoot != null) {
			plotText += "\"You're looking to find the @t? ";
			plotThing = finalLoot.toString();
		}
		
		// What the speaker will say is determined by the next action
		Action nextAction = nodeList.get(i+1).getAction();
		
		if (nextAction.getActionType() == Action.ActionType.KILL) {
			if (nextAction.getObjectActor() == speaker && finalBoss == speaker) {
				plotText += "First you'll have to find me! Have at you!\"";
			} else if (nextAction.getObjectActor() == speaker) {
				plotText += "First you'll have to defeat me!\"";
			} else if (nextAction.getObjectActor() == finalBoss) {
				plotText += "You'll find that bastard here on this planet! Good luck!\"";
			} else {
				plotText += "Then, first you'll have to defeat @b, here on this planet. Good luck!\"";
				otherThing = nextAction.getObjectActor().toString();
			}
		}
		
		if (nextAction.getActionType() == Action.ActionType.TAKE) {
			if (nextAction.getObjectProp() == finalLoot) {
				plotText += "Then you're lucky! It's right here on this planet!\"";
			} else {
				plotText += "Then, first you'll have to find the @b hidden somewhere on this planet. Good luck!\"";
			}
		}
		
		if (nextAction.getActionType() == Action.ActionType.MEET) {
			plotText += "Good luck with that!\"";
		}
		
		if (nextAction.getActionType() == Action.ActionType.VISIT) {
			nextStar = nextAction.getObjectScene().toString();
			plotText += "Then, you'll have to go to the @s star, where ";
			
			// When the next action is a VISIT action, we check the next action yet
			Action nextNextAction = nodeList.get(i+2).getAction();
			
			if (nextNextAction.getActionType() == Action.ActionType.KILL) {
				if (nextNextAction.getObjectActor() == speaker) {
					plotText += "'cause I won't fight here!\"";
				} else if (nextNextAction.getObjectActor() == finalBoss) {
					plotText += "you'll find that bastard! Good luck!\"";
				} else {
					plotText += "you'll first have to defeat @b! Good luck with that!\"";
					otherThing = nextNextAction.getObjectActor().toString();
				}
			}
			
			if (nextNextAction.getActionType() == Action.ActionType.TAKE) {
				if (nextNextAction.getObjectProp() == finalLoot) {
					plotText += "you'll find it, hidden beneath the surface.\"";
				} else {
					plotText += "you'll find the @b, which you'll need!";
					otherThing = nextNextAction.getObjectProp().toString();
				}
			}
			
			if (nextNextAction.getActionType() == Action.ActionType.MEET) {
				if (nextNextAction.getObjectActor() == speaker) {
					plotText += "I'll follow you.\"";
				} else {
					if (nextNextAction.getObjectActor() == finalBoss) {
						plotText += "you'll meet that bastard!\"";
					} else {
						plotText += "you'll meet @b, who will help you.\"";
						otherThing = nextNextAction.getObjectActor().toString();
					}
				}
			}
			
			if (nextNextAction.getActionType() == Action.ActionType.VISIT) {
				plotText += "you'll have to make a left turn to the @b star.\"";
				otherThing = nextNextAction.getObjectScene().toString();
			}
			
			if (nextNextAction.getActionType() == Action.ActionType.GIVE) {
				if (nextNextAction.getSubjectActor() == mainCharacter) {
					if (nextNextAction.getObjectActor() == speaker) {
						plotText += "we together can bring your @b to my place.\"";
						otherThing = nextNextAction.getObjectProp().toString();
					} else {
						plotText += "you'll have to deliver the @b.\"";
						otherThing = nextNextAction.getObjectProp().toString();
					}
				} else {
					if (nextNextAction.getSubjectActor() == speaker) {
						plotText += "I've left my @b, which I'll give you!\"";
						otherThing = nextNextAction.getObjectProp().toString();
					} else {
						plotText += "@b will give you something that you'll need in your adventure.\"";
						otherThing = nextNextAction.getSubjectActor().toString();
					}
				}
			}
		}
		
		if (nextAction.getActionType() == Action.ActionType.GIVE) {
			if (nextAction.getSubjectActor() == mainCharacter) {
				if (nextAction.getObjectActor() == speaker) {
					plotText += "Then, first I'll need your @b.\"";
					otherThing = nextAction.getObjectProp().toString();
				} else {
					plotText += "Good luck with that!\"";
					otherThing = nextAction.getObjectProp().toString();
				}
			} else {
				if (nextAction.getSubjectActor() == speaker) {
					plotText += "Then you'll need this!\"";
				} else {
					plotText += "Good luck with that!\"";
				}
			}
		}
		
		plotText += ", " + speaker + " says.";
		
		plotText = replacePlaceHolders(plotText, "", "", plotThing, nextStar, otherThing);
		
		return plotText;
	}

	private String giveAction(int i) {
		String plotThing = nodeList.get(i).getAction().getObjectProp().toString();
		String plotText;
		if (nodeList.get(i).getAction().getSubjectActor() == mainCharacter) {
			String otherActor = nodeList.get(i).getAction().getObjectActor().toString();
			plotText = replacePlaceHolders(giveGiveText, "", "", plotThing, "", otherActor);
		} else {
			String otherActor = nodeList.get(i).getAction().getSubjectActor().toString();
			plotText = replacePlaceHolders(giveReceiveText, "", "", plotThing, "", otherActor);
		}
		
		return plotText;
	}

	private String takeAction(int i) {
		String plotThing = nodeList.get(i).getAction().getObjectProp().toString();
		String scene = nodeList.get(i).getAction().getObjectProp().getLocation().toString();
		String plotText = replacePlaceHolders(takeText, "", "", plotThing, scene, "");
		return plotText;
	}

	private String killAction(int i) {
		String plotThing = nodeList.get(i).getAction().getObjectActor().toString();
		String scene = nodeList.get(i).getAction().getObjectActor().getLocation().toString();
		String plotText = replacePlaceHolders(killText, "", "", plotThing, scene, "");
		return plotText;
	}

	private String visitAction(int i) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(visitsFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String killPrefix = null;
		String killMidText = null;
		String takePrefix = null;
		String takeMidText = null;
		try {
			reader.readLine();
			killPrefix = reader.readLine();
			killMidText = reader.readLine();
			reader.readLine();
			takePrefix = reader.readLine();
			takeMidText = reader.readLine();
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String prefix;
		String midText;
		String plotThing;

		if (nodeList.get(i + 1).getAction().getActionType() == Action.ActionType.KILL) {
			prefix = killPrefix;
			midText = killMidText;
			plotThing = nodeList.get(i + 1).getAction().getObjectActor().toString();
		} else if (nodeList.get(i + 1).getAction().getActionType() == Action.ActionType.TAKE) {
			prefix = takePrefix;
			midText = takeMidText;
			plotThing = nodeList.get(i + 1).getAction().getObjectProp().toString();
		} else {
			prefix = "";
			midText = "";
			plotThing = "";
		}

		String plotText = readLine(reader, nodeList.get(i).getAction().getObjectScene().getType());
		
		plotText = replacePlaceHolders(plotText, prefix, midText, plotThing, nodeList.get(i).getAction()
				.getObjectScene().toString(), "");

		return plotText;
	}

	private String replacePlaceHolders(String plotText, String prefix, String midText,
			String plotThing, String scene, String otherThing) {

		plotText = plotText.replace("@p", prefix);
		plotText = plotText.replace("@m", midText);
		plotText = plotText.replace("@t", plotThing);
		plotText = plotText.replace("@a", mainCharacter.toString());
		plotText = plotText.replace("@s", scene);
		plotText = plotText.replace("@b", otherThing);
		
		return plotText;
	}

	private String readLine(BufferedReader reader, int finalIndex) {
		int index = 0;
		String finalString = null;
		String nextString;
		try {
			nextString = reader.readLine();
			while (nextString != null && finalString == null) {
				if (index == finalIndex) {
					finalString = nextString;
				}
				index++;
				nextString = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return finalString;
	}

	public static void main(String[] args) {
		Actor actor = new Actor("Bjarne");
		Prop prop = new Prop("banana");
		Actor boss = new Actor("Tom");
		Scene scene = new Scene("Lunarstorm");
		boss.setLocation(scene);

		ArrayList<PlotNode> nodeList = new ArrayList<PlotNode>();
		PlotNode plotNodeA = new PlotNode(new Action(Action.ActionType.MEET, actor, boss));
		nodeList.add(plotNodeA);
		
		PlotNode plotNode = new PlotNode(new Action(Action.ActionType.VISIT, actor, scene));
		nodeList.add(plotNode);
		
		PlotNode plotNode2 = new PlotNode(new Action(Action.ActionType.GIVE, actor, boss, prop));
		nodeList.add(plotNode2);
		
		PlotNode plotNode3 = new PlotNode(new Action(Action.ActionType.GIVE, boss, actor, prop));
		nodeList.add(plotNode3);
		
		PlotNode plotNode4 = new PlotNode(new Action(Action.ActionType.KILL, actor, boss));
		nodeList.add(plotNode4);

		System.out.println(new PlotTextGenerator(new PlotLine(nodeList), actor).generatePlotText());
	}
}
