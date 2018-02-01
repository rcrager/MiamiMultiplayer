package game.util.classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import game.id.ObjectID;
import game.main.Game;

public class Button extends GameObject{
	/*
	 * For gui purposes.
	 * When mouse is inside the bounds
	 * of the button, and is pressed
	 * then it defaultly toggles but,
	 * that can be changed if desired.
	 * when the button is pressed it 
	 * changes color indicating it is
	 * pressed.
	 */

	String text;
	boolean isPressed = false;
	int scale;
	int noScaleW;
	int noScaleH;
	boolean toggle = true;
	
	public Button(int x, int y, int w, int h, String text) {
		super(x, y, ObjectID.Input, 0);
		this.w = w;
		this.noScaleW = w;
		this.h = h;
		this.noScaleH = h;
		setBounds(w,h);
		this.text = text;
	}
	
	public void setScale(int scale){
		this.scale = scale;
	}

	public void setText(String text){
		this.text = text;
	}
	
	@Override
	public void mouseInput(MouseEvent e){
		switch(e.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(e.getX()>=x && e.getX()<=x+w){
				if(e.getY()>=y && e.getY()<=y+h){
					if(toggle){
						isPressed = !isPressed;
					}else{
						isPressed = true;
					}
				}
			}
		break;
		}
	}
	
	public boolean isPressed(){
		return isPressed;
	}
	
	public void setPressed(boolean isPressed){
		this.isPressed = isPressed;
	}
	
	@Override
	public void update(List<GameObject> objects) {
		
	}
	
	public void setToggle(boolean toggle){
		this.toggle = toggle;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)(g);
		g2d.setColor(Color.WHITE);
		if(isPressed){
			g2d.setColor(Color.black);
		}
		setBounds(w,h);
		g2d.fillRect(x, y, w, h);
		g2d.setColor(Color.black);
		if(isPressed){
			g2d.setColor(Color.white);
		}
//		int fontSize = 
		Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(text,g2d);
		if(getImage()!=null){
			int fontSize = 18;
			Font font = new Font("Lucida Console", Font.PLAIN, fontSize);
			g2d.setFont(font);
			stringBounds = g2d.getFontMetrics().getStringBounds(text, g2d);
			while(stringBounds.getWidth()>getWidth()){
				fontSize--;
				font = new Font("Lucida Console", Font.PLAIN, fontSize);
				g2d.setFont(font);
				stringBounds = g2d.getFontMetrics().getStringBounds(text, g2d);
			}
			g2d.drawString(text,(int)((x+w/2)-stringBounds.getWidth()/2),y+getHeight());
			g2d.drawImage(getImage(), x, y,getWidth()-Game.SCALE,(int)(getHeight()-stringBounds.getHeight()),null);
		}else{			
			Font font = new Font("Lucida Console", Font.PLAIN, 18);
			g2d.setFont(font);
			stringBounds = g2d.getFontMetrics().getStringBounds(text, g2d);
			g2d.drawString(text,(int)((x+w/2)-stringBounds.getWidth()/2),(int)(y+stringBounds.getHeight()/2)+h/2);
		}
	}
	
}
