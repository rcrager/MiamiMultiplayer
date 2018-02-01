package game.util.classes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.main.Controller;

public class KeyInput extends KeyAdapter{
	/*
	 * Takes in the physical keypresses from
	 * java then sends them to each game objects
	 * that is in the object pool from the 
	 * controller class.
	 */

	Controller controller;
	
	public KeyInput(Controller con){
		controller = con;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		for(GameObject gm: controller.gameObjects){
			gm.keyInput(e);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		for(GameObject gm: controller.gameObjects){
			gm.keyInput(e);
		}
	}
	
}
