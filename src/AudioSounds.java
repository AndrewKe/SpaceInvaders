import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JApplet;


public class AudioSounds {
	
	public AudioSounds(){
		
	}
	
	public void addSound(String fileName){
		URL hit = this.getClass().getResource(fileName);
		sounds.add(JApplet.newAudioClip(hit));
		fileNames.add(fileName);
	}
	
	public void play(String fileName){
		int index = fileNames.indexOf(fileName);
		AudioClip sound = sounds.get(index);
		sound.play();
	}
	private ArrayList<AudioClip> sounds = new ArrayList<AudioClip>();
	private ArrayList<String> fileNames = new ArrayList<String>();
}
