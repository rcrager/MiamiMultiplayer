package game.util.classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import game.id.ObjectID;
import game.main.window.graphics;

public class Slider extends GameObject{
	/*
	 * Slider class literally only created
	 * for the volume function but, can
	 * be used for many other things.
	 * Allows user to slide little button
	 * along the width of the slider.
	 * Extension of GameObject class
	 */

	double slide;
	int slideX, slideY,slideW,slideH;
	boolean slidePressed = false;
	int mouseX,mouseY;
	String name;
	
	public Slider(int x, int y, int w, int h,String name) {
		super(x, y, ObjectID.Input, 1);
		setBounds(w,h);
		slide = .5;
		slideW = 10;
		slideH = h+slideW;
		slideX = x-slideW/2;
		slideY = y;
		this.name = name;
	}

	@Override
	public void mouseInput(MouseEvent e){
		switch(e.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(e.getX()>=x && e.getX()<=x+w && e.getY()>=y && e.getY()<=y+h){
				slidePressed = true;
			}
		break;
		case MouseEvent.MOUSE_RELEASED:
			if(slidePressed){
				slidePressed = false;
			}
		break;
		}
	}
	
	@Override
	public void update(List<GameObject> objects){
		if(slideX<x){
			slideX = x-slideW/2;
		}else if(slideX+slideW>x+w){
			slideX = (x+w)-slideW/2;
		}
		if(slidePressed){
			if(graphics.mouseX>=x && graphics.mouseX<=x+w && graphics.mouseY>=y && graphics.mouseY<=y+h){
				slideX = (int)(graphics.mouseX-slideW/2);
			}else if(graphics.mouseX<x){
				slideX = x-slideW/2;
			}else if(graphics.mouseX>x+w){
				slideX = (x+w)-slideW/2;
			}
		}
		slide = ((slideX+slideW/2)-x)/(double)getWidth();
		slideY = y-(slideH-h)/2;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Font font = new Font("Lucida Console", Font.PLAIN, 18);
		g2d.setFont(font);
		g2d.setColor(Color.white);
		Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(name, g2d);
		g2d.drawString(name, (int)(x-stringBounds.getWidth()-5), (int)(y+h/2+(stringBounds.getHeight()/2)));
		g2d.fillRect(x, y, w, h);
		g2d.setColor(Color.black);
		g2d.fillRect(slideX, slideY, slideW, slideH);
	}
	
	public void setSlideAmount(double slide){
		this.slide = slide;
	}
	
	public double getSlideAmount(){
		return slide;
	}
	
	public int getSlidePercent(){
		return (int)(slide*100);
	}

}
