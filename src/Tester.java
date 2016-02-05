import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JApplet;

import acm.program.*;
import acm.graphics.*;

public class Tester extends ConsoleProgram{
	public void run(){
		AudioSounds collection = new AudioSounds();
		collection.addSound("explosion.wav");
		collection.play("explosion.wav");
	}
}
