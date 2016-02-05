import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GCanvas;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.util.RandomGenerator;


public class Spaceship extends GImage{
	
	public Spaceship(String imageName,int x,int y){
		super(imageName,x,y);
	}
	
	public void flashDeath(AudioSounds collection, GLabel health, GLabel healthLabel){
		collection.play("explosion.wav");
    	for(int i = 0; i<4; i++){
    		health.setVisible(false);
    		this.setVisible(false);
    		healthLabel.setVisible(false);
    		pause(100);
    		this.setVisible(true);
    		health.setVisible(true);
    		health.setColor(Color.RED);
    		healthLabel.setVisible(true);
    		healthLabel.setColor(Color.RED);
    		pause(100);
    	}
    	healthLabel.setColor(Color.GREEN);
    	health.setColor(Color.GREEN);
	}
	
	
	

}
