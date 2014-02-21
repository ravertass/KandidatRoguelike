package se.chalmers.plotgen;

public class ActionTest {

	public static void main(String[] args) {
		Actor leif = new Actor("Leif");
		Actor uffe = new Actor("Uffe");
		Actor twister = new Actor("Twister");
		Prop sword = new Prop("sword");
		Prop banana = new Prop("banana");

		Action take1 = new Action(Action.ActionType.TAKE, uffe, banana);
		System.out.println(take1);
		
		Action kill = new Action(Action.ActionType.KILL, leif, uffe);
		System.out.println(kill);
		
		Action take2 = new Action(Action.ActionType.TAKE, leif, banana);
		System.out.println(take2);
		
		Action visit = new Action(Action.ActionType.VISIT, leif, twister);
		System.out.println(visit);
		
		Action give1 = new Action(Action.ActionType.GIVE, leif, twister, banana);
		System.out.println(give1);
		
		Action give2 = new Action(Action.ActionType.GIVE, twister, leif, sword);
		System.out.println(give2);
	}
}
