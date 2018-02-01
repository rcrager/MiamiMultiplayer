package levelEditor.main.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class LevelEditorButton{
	/*
	 * Button for the selection of tools
	 * a toggle button, has name that
	 * just gets displayed in the middle
	 * of the button.
	 */
	
	String name;
	int x,y,width,height;
	
	public LevelEditorButton(String name, int x, int y, int w, int h){
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
		g2d.fillRect(x, y, width, height);
		g2d.setColor(Color.white);
		g2d.drawString(name, x, y+height/2);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isPressed(MouseEvent e) {
		if(e.getX()>=x && e.getX()<(x+width) && e.getY()>=y && e.getY()<(y+height)){
			return true;
		}
		return false;
	}
}
