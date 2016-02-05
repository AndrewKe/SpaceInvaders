import java.awt.Color;
import java.awt.Image;

import acm.graphics.*;

public class AlienImage extends GImage{

	public AlienImage(String imageName,String imageName2, int x, int y, int pointsWorth){
		super(imageName,x,y);
		this.imageName = imageName;
		this.imageName2= imageName2;
		this.pointsWorth = pointsWorth;
	}
	
	public int getPoints(){
		return pointsWorth;
	}
	public void setPoints(int pointsWorth){
		this.pointsWorth = pointsWorth;
		
	}
	public String changeLook(){
		if(proper == true){
			proper = false;
			return imageName2;
		}
		else{
			proper = true;
			return imageName;
		}
	}
	
	public void moveIt(int dist) {
		this.move(dist, 0);
		this.setImage(changeLook());
	}
	
	private int pointsWorth;
	private String imageName;
	private String imageName2;
	private boolean proper = true;
}
