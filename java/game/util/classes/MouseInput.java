package game.util.classes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.id.ObjectID;
import game.main.Controller;

public class MouseInput extends MouseAdapter{
	/*
	 * Get direct mouse input from java, then
	 * calls the mouseInput method in each game
	 * object that is in the current object
	 * pool in the controller class.
	 */

	Controller controller;
	
	public MouseInput(Controller controller){
		this.controller = controller;
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		for(GameObject gm: controller.gameObjects){
			gm.mouseInput(e);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		for(GameObject gm: controller.gameObjects){
			gm.mouseInput(e);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		for(GameObject gm: controller.gameObjects){
			if(gm.getId() == ObjectID.Input){
				gm.mouseInput(e);
			}
		}
	}
}
