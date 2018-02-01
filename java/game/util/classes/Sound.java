package game.util.classes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound{
	/*
	 * Sound class
	 * plays sound, ends sound
	 * anything to do with sound
	 * sets volume of sound
	 * is in this class.
	 */
	
	String path;
	InputStream input;
	Clip sound;
	boolean isOver = false;
	float volume = 0.0f;
	FloatControl gainControl;
	
	public Sound(String path){
		this.path = path;
		try {
			input = getClass().getResourceAsStream(path);
			InputStream inStream = new BufferedInputStream(input);
			AudioInputStream audio = AudioSystem.getAudioInputStream(inStream);
			sound = AudioSystem.getClip();
			sound.open(audio);
			gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return path;
	}
	
	public void playSound(){
		if(sound!=null){
			reset();
			sound.start();
		}
	}
	
	public void close(){
		if(sound!=null && sound.isOpen()){
			sound.stop();
			sound.close();
		}
	}
	
	public void stopSound(){
		if(sound!=null){
			sound.stop();
		}
	}
	
	public void reset(){
		sound.setMicrosecondPosition(0);
	}
	
	public boolean isOver(){
		if(sound.getMicrosecondPosition()>=sound.getMicrosecondLength()){
			isOver = true;
			return isOver;
		}else{
			isOver = false;
			return isOver;
		}
	}
	
	public long getLength(){
		//returns milliseconds...hopefully
		return sound.getMicrosecondLength()/1000;
	}
	
	public void setVolume(float gain){
		//gain = decibels 
		gainControl.setValue(gain);
	}
	public void loop(int loop){
		if(sound!=null){
			if(loop==-1){
				sound.loop(Clip.LOOP_CONTINUOUSLY);
				sound.start();
			}else{
				sound.loop(loop);
			}
		}
	}
}
