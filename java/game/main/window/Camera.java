package game.main.window;

import game.main.Game;
import game.util.classes.GameObject;

public class Camera {
	/*
	 * Follows the player, then in
	 * graphics class the Graphics
	 * translate to the cameras x&y
	 * Camera x&y is centered on player.
	 */
	
	int x,y;
	GameObject player;
	
	public Camera(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void update(GameObject player){
		if(player!=null){			
			this.player = player;
		}
		x=(-player.getX()+Game.WIDTH/2);
		y=(-player.getY()+Game.HEIGHT/2);
	}
	
	public void setPlayer(GameObject player){
		this.player = player;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
