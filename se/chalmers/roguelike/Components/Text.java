package se.chalmers.roguelike.Components;

public class Text implements IComponent {
	private String text;
	
	public Text(String text){
		this.text = text; 
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public IComponent clone(){
		return new Text(text);
	}
}
