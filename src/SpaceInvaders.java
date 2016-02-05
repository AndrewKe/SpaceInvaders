import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JApplet;

import acm.program.*;
import acm.util.RandomGenerator;
import acm.graphics.*;

public class SpaceInvaders extends GraphicsProgram{
	
	public static final int WINDOW_HEIGHT = 800;
	public static final int ALIEN_ROWS = 3;
	public static final int ALIENS_PER_ROW = 11;
	public static final int SIDE_ALIEN_OFFSET = 10;
	public static final int TOP_ALIEN_OFFSET = 50;
	public static final int ALIEN_WIDTH = 50;
	public static final int ALIEN_HEIGHT = 50;
	public static final int ALIEN_SEPERATION = 10;
	public static final int ALIEN_VERTICAL_OFFSET = 10;
	public static final int WINDOW_WIDTH = ((SIDE_ALIEN_OFFSET*2) + (ALIEN_SEPERATION * (ALIENS_PER_ROW-1)) + (ALIENS_PER_ROW * ALIEN_WIDTH))+ 100;
	private static final int SHIP_OFFSET = 20;
	private static final int SHOT_WAIT = 5;
	
	//Initialize graphics
	public void init(){
		addMouseListeners();
		addKeyListeners(this);
		setBackground(Color.BLACK);
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		spaceShip = new Spaceship("spaceShip.png",getWidth()/2,getHeight()-SHIP_OFFSET-50);
		add(spaceShip);
		invaders = new AlienImage[ALIEN_ROWS * ALIENS_PER_ROW];
		for(int i = 0; i<ALIEN_ROWS; i++){
			for(int j = 0; j<ALIENS_PER_ROW; j++){
				AlienImage square = new AlienImage(invaderNames[i],invadernames2[i],j*ALIEN_WIDTH + j*ALIEN_SEPERATION + SIDE_ALIEN_OFFSET,i*ALIEN_HEIGHT + i*ALIEN_VERTICAL_OFFSET + TOP_ALIEN_OFFSET,(3-i)*1000);
				invaders[i*11 + j] = square;
				add(square);
			}
		}
		healthLabel = new GLabel("HEALTH: ",getWidth()-195,20);
		healthLabel.setColor(Color.GREEN);
		healthLabel.setFont("Copperplate Gothic Light-20");
		add(healthLabel);
		health = new GLabel("1000",getWidth()-100,20);
		health.setColor(Color.GREEN);
		health.setFont("Copperplate Gothic Light-20");
		add(health);
		
		pointsLabel = new GLabel("Points: ",10,20);
		pointsLabel.setColor(Color.GREEN);
		pointsLabel.setFont("Copperplate Gothic Light-20");
		add(pointsLabel);
		points = new GLabel("0",90,20);
		points.setColor(Color.GREEN);
		points.setFont("Copperplate Gothic Light-20");
		add(points);
		initializeSounds();
	}
	
	private void initializeSounds(){
		collection = new AudioSounds();
		collection.addSound("shoot.wav");
		collection.addSound("invaderkilled.wav");
		collection.addSound("explosion.wav");
	}
	
	public void run(){
		while(true){
			move();
			checkHit();
			checkDeath();
			makeUFO();
		}
	}
	
	private void moveUFO() {
		if(UFO!=null){
			UFO.sendToBack();
			UFO.move(-5,0);
			if(UFO.getX()+ 80 <= 0){
				remove(UFO);
				UFO = null;
				print("GONE!");
			}
		}
	}
	
	public void mouseMoved(MouseEvent e){
		spaceShip.setLocation(e.getX(), spaceShip.getY());
	}
	
	public void mouseClicked(MouseEvent e){
		fireAway();
	}
	
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_LEFT){
        	spaceShip.move(-10,0);
        }
        if(e.getKeyCode()== KeyEvent.VK_RIGHT){
        	spaceShip.move(10, 0);
        }
        if(e.getKeyCode()== KeyEvent.VK_SPACE){
        	fireAway();
        }
        if(e.getKeyCode()== KeyEvent.VK_T){
        	exit();
        }
    }
    
    public void fireAway(){
    	if(lastShot>=SHOT_WAIT){
    		lastShot = 0;
	    	GRect bullet = new GRect(spaceShip.getX()+spaceShip.getWidth()/2-5,spaceShip.getY()-20 ,5,20);
	    	bullet.setFilled(true);
	    	bullet.setFillColor(Color.WHITE);     
	    	add(bullet);
	    	bullets.add(bullet);
	    	collection.play("shoot.wav");
    	}
    }
    
    private void moveBullets(){
    	for(int i =0; i<bullets.size();i++){
    		GRect bullet = bullets.get(i);
    		bullet.move(0, -20);	
    	}
    	for(int i =0; i<alienBullets.size();i++){
    		GRect bullet = alienBullets.get(i);
    		bullet.move(0,20);
    	}
    }
    
    private void fireShots(AlienImage attacker){
    	int value = rgen.nextInt(0, 300);
    	if(value == 1 && attacker.isVisible()!=false){     
    		GRect bullet = new GRect(attacker.getX()+attacker.getWidth()/2-5,attacker.getY() + ALIEN_HEIGHT,5,20);
	    	bullet.setFilled(true);
	    	bullet.setFillColor(Color.RED);     
	    	add(bullet);
	    	alienBullets.add(bullet);
	    	collection.play("shoot.wav");
    	}
    }
    
    private void checkHit(){
    	//Spaceship bullets check
    	for(int i = bullets.size()-1; i>=0; i--){
    		GRect bullet = bullets.get(i);
    		
    		//Bullet is out of bounds check
    		if(bullet.getY()<=-20){	
    			bullets.remove(i);
    			continue;
    		}
    		GObject hit =  getElementAt(new GPoint(bullet.getX(),bullet.getY()-2));
    		GObject hit2 = getElementAt(new GPoint(bullet.getX()+5,bullet.getY()-2));
    		//If hit is an Alien......
    		
    		if(hit instanceof AlienImage || hit2 instanceof AlienImage){
    			GObject good = null;
    			if(hit2 != null){
    				good = hit2;
    			}
    			if(hit != null){
    				good = hit;
    			}
    			deathCount++;
    			collection.play("invaderkilled.wav");
    			AlienImage alienImageHit = (AlienImage) good;
    			points.setLabel("" + (Integer.parseInt(points.getLabel())+alienImageHit.getPoints()));
    			good.setVisible(false);
    			remove(good);
    			remove(bullet);
    			bullets.remove(i);
    		}
    		
    		if(hit == UFO && UFO!= null){
    			remove(UFO);
    			UFO = null;
    			collection.play("invaderkilled.wav");
    			points.setLabel("" + (Integer.parseInt(points.getLabel())+rgen.nextInt(1000, 10000)));
    		}
    	}
    	
    	//Alien bullets check
    	for(int i = alienBullets.size()-1; i>=0; i--){
    		GRect bullet = alienBullets.get(i);
    		
    		//Check if the bullet is out of bounds
    		if(bullet.getY()>=getHeight()+20){
    			alienBullets.remove(i);
    			continue;
    		}
    		
    		//
    		if(bullet.getX()>= spaceShip.getX()&& bullet.getX()<=spaceShip.getX()+60 && bullet.getY()+20>= spaceShip.getY()&& bullet.getY()<=spaceShip.getY()+10){
    			health.setLabel("" + (Integer.parseInt(health.getLabel())-100));
    			remove(bullet);
    			alienBullets.remove(bullet);
    			spaceShip.flashDeath(collection, health, healthLabel);
    			if(Integer.parseInt(health.getLabel())<=400){
    				health.setColor(Color.RED);
    				healthLabel.setColor(Color.RED);
    			}
    		}
    	}
    }

    private void checkDeath(){
    	if(formationY + 10>= spaceShip.getY() || Integer.parseInt(health.getLabel()) <= 0){
    		println("GAME OVER");
    		System.exit(0);
    	}
    	if(checkAlienDeathCount()){
    		youWin();
    		System.exit(0);
    	}
    }
    

	private void move(){
		//Move alien bodies or fire shots
		for(int i = 0; i<invaders.length; i++){
			
			//If it is time to move and change position...
			if(changeWait%4==0){
				invaders[i].moveIt(alienMoveDist);
			}
			//Make the alien fire shots if they feel like it
			fireShots(invaders[i]);
		}
		
		pause(100);
		
		//If it is time, change the x tracker....
		if(changeWait%4==0){
			formationX+= alienMoveDist;
			//If the aliens are close to the side, move them down and update the y-pos
			if (formationX == 0 || formationX + ALIENS_PER_ROW * ALIEN_WIDTH + (ALIENS_PER_ROW+1) * ALIEN_SEPERATION == getWidth()){
				alienMoveDist = -alienMoveDist;
				for(int i = 0; i<invaders.length; i++){
					invaders[i].move(0, alienDropDist);
				}
				formationY+= alienDropDist;			
			}
		}
		
		//Update stats
		changeWait++;
		lastShot++;
		moveBullets();
		moveUFO();
	}
	
	private void makeUFO(){
		int chance = rgen.nextInt(0,30);
		if(chance == 20 && UFO == null){
			UFO = new GImage("UFO.png",getWidth()-10,10);
			add(UFO);
		}
	}
	
	private void youWin(){
		removeAll();
		GLabel youWin = new GLabel("YOU WIN!",getWidth()/2,100);
		youWin.setFont("Copperplate Gothic Light-90");
		youWin.setColor(Color.GREEN);
		youWin.move(-youWin.getWidth()/2, 0);
		add(youWin);
		GLabel stats = new GLabel("",getWidth()/2,200);
		int healthInt = Integer.parseInt(health.getLabel());
		if(healthInt==1000){
			stats.setLabel("Your spaceship was unscathed by alien bullets");
		}
		if(healthInt<1000){
			stats.setLabel("Your spaceship was slightly damaged by alien bullets");
		}
		if(healthInt<=500){
			stats.setLabel("Your space ship was heavily damaged by alien bullets");
		}
		if(healthInt<=300){
			stats.setLabel("Your space ship was almost completely dead");
		}
		stats.move(-stats.getWidth()/2, 0);
		stats.setColor(Color.GREEN);
		add(stats);
		while(true){
			youWin.setVisible(true);
			pause(1000);
			youWin.setVisible(false);
			pause(1000);
		}
	}
	
	private boolean checkAlienDeathCount(){
		for(AlienImage alien: invaders){
			if(alien.isVisible()==true){
				return false;
			}
		}
		return true;
	}
	
	private String[] invaderNames = {"level3.png","level2.png","level1.png"};
	private String[] invadernames2 = {"level3:2.png","level2:2.png","level1:2.png"};
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int deathCount = 0;
	private AlienImage[] invaders;
	private int lastShot = 100;
	private int formationX = 0;
	private int ALIEN_Y_OFFSET;
	private int formationY = ALIEN_HEIGHT * ALIEN_ROWS + TOP_ALIEN_OFFSET + ALIEN_Y_OFFSET*(ALIEN_ROWS+11);
	private int alienMoveDist = 10;
	private int alienDropDist = 5;
	private Spaceship spaceShip;
	private ArrayList<GRect> bullets = new ArrayList<GRect>();
	private ArrayList<GRect> alienBullets = new ArrayList<GRect>();
	private GLabel health;
	private GLabel healthLabel;
	private GLabel points;
	private GLabel pointsLabel;
	private int changeWait=0;
	private GImage UFO;
	private AudioSounds collection;
	 
}
