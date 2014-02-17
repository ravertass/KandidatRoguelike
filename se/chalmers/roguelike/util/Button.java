package se.chalmers.roguelike.util;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import se.chalmers.roguelike.Components.Sprite;


public class Button {

    public Texture buttonTexture;
//	public Sprite sprite;
    public boolean isClicked = false;
    Rectangle bounds = new Rectangle();


    public void addButton(int x, int y , String fileName){
    	
//    	sprite = new Sprite(fileName);
    	
    	
        try {
            buttonTexture = TextureLoader.getTexture("PNG", 
            		new FileInputStream(new File("./resources/" + fileName + ".png")));
            } catch (IOException e) {
            e.printStackTrace();
        }
        bounds.x=x;
        bounds.y=y;
        bounds.height=buttonTexture.getImageHeight();
        bounds.width=buttonTexture.getImageWidth();
        System.out.println("WHY DO I GET PRINTED ALL THE TIME?!");
    }

    public void draw(){
        if(bounds.contains(Mouse.getX(),(600 - Mouse.getY()))&&Mouse.isButtonDown(0)){
            isClicked=true;
            System.out.println("YOU CLICKED ME!");
        }else{
            isClicked=false;
        }
        Color.white.bind();
        buttonTexture.bind(); // or GL11.glBind(texture.getTextureID());
        int x = bounds.x;
        int y = bounds.y;
        
        GL11.glBegin(GL11.GL_QUADS);
        	GL11.glTexCoord2f(0,0);
        	GL11.glVertex2f(x,y);
        	GL11.glTexCoord2f(1,0);
        	GL11.glVertex2f(x+buttonTexture.getTextureWidth(),y);
        	GL11.glTexCoord2f(1,1);
        	GL11.glVertex2f(x+buttonTexture.getTextureWidth(),y+buttonTexture.getTextureHeight());
        	GL11.glTexCoord2f(0,1);
        	GL11.glVertex2f(x,y+buttonTexture.getTextureHeight());
        GL11.glEnd();
        }

    }