package game.util.classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import game.id.ObjectID;
import game.main.Game;

public class InputBox extends GameObject{
	/*
	 * Receives key input from the Parent and
	 * writes it inside of the box, which has
	 * a toggle for if it's selected or not.
	 * Also doubles as a console class,
	 * because I wanted to add a console but,
	 * didn't want many features and didn't 
	 * want to make a new class for it.
	 */
	String input;
	boolean isSelected = false;
	boolean allowInput = true;
	int scale;
	String name;
	int noScaleX;
	int noScaleY;
	boolean visible = true;
	public boolean console = false;
	private ArrayList<String> consoleHistory;
	public boolean endGame = false;
	
	public InputBox(int x, int y,int w, int h, String name){
		super(x,y,ObjectID.Input,0);
		this.w = w;
		this.noScaleX = x;
		this.h = h;
		this.noScaleY = y;
		setBounds(w,h);
		input = "";
		this.name = name;
		consoleHistory = new ArrayList<String>();
	}
	
	public void reset(){
		consoleHistory.clear();
		input = "";
	}
	
	public void setScale(int scale){
		this.scale = scale;
	}
	
	@Override
	public void update(List<GameObject> objects) {
		
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
	}
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public String getText(){
		return input;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	@Override
	public void keyInput(KeyEvent e){
		if(!console){
			if(isSelected){
				if(e.getKeyCode() == 8){
					//if backspace is pressed
					if(input.length()>0){
						if(e.getID() == KeyEvent.KEY_PRESSED){
							input=input.substring(0, input.length()-1);
						}
					}
				}else{
					if(allowInput){
						if(e.getKeyCode()>= 32 && e.getKeyCode()<=127){
							if(!e.isControlDown() && !e.isAltDown()){
								if(e.getID() == KeyEvent.KEY_PRESSED){
									input += e.getKeyChar();
								}
							}
						}
					}
				}
			}
		}else{
			if(e.getID() == KeyEvent.KEY_PRESSED){
				if(e.getKeyChar() == '`'){
					setVisible(!visible);
				}else{
					if(e.getKeyCode() == 8){
						//if backspace is pressed
						if(input.length()>0){
							input=input.substring(0, input.length()-1);
						}
					}else if(e.getKeyCode() == 10){
						//if enter pressed
						finishInput();
					}else{
						if(allowInput){
							if(e.getKeyCode()>= 32 && e.getKeyCode()<=127){
								if(!e.isControlDown() && !e.isAltDown()){
										input += e.getKeyChar();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void finishInput(){
		if(!input.equals("")){
			consoleHistory.add(0,input);
		}
		switch(input.toLowerCase()){
		case "end game":
			endGame = true;
		break;
		case "help":
			consoleHistory.add(0,"Available Commands: end game");
		break;
		default:
			consoleHistory.add(0,"Error: No Command Recognized");
			consoleHistory.add(0,"Type \"help\" for available commands.");
		break;
		}
		input = "";
	}
	
	@Override
	public void mouseInput(MouseEvent e){
		switch(e.getID()){
		case MouseEvent.MOUSE_PRESSED:
			if(e.getX()>=x && e.getX()<=x+w){
				if(e.getY()>=y && e.getY()<=y+h){
					isSelected = !isSelected;
				}else{
					isSelected = false;
				}
			}else{
				isSelected = false;
			}
		break;
		}
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Font font = new Font("Lucida Console", Font.PLAIN, 18);
		g2d.setFont(font);
		setBounds(getWidth(),getHeight());
		if(!console){
			g2d.setColor(Color.white);
			g2d.fillRect(x, y, w, h);
			Rectangle2D nameBounds = g2d.getFontMetrics().getStringBounds(name,g2d);
			g2d.drawString(name, (int)((x-h)-nameBounds.getWidth()),(int)(y+h/2+nameBounds.getHeight()/2));
			Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(input,g2d);
			if(stringBounds.getWidth()+2*Game.SCALE>=w-2*Game.SCALE){
				allowInput = false;
			}else{
				allowInput = true;
			}
			g2d.setColor(Color.black);
			g2d.fillRect(x-h, y, h, h); 
			g2d.drawString(input,x+2*Game.SCALE,(int)(y+stringBounds.getHeight()/2)+h/2);
			if(isSelected){
				g2d.setColor(Color.white);
				g2d.fillRect((x-h)+h/5, y+h/5, h-(2*h/5),h-(2*h/5));
			}
		}else{
			if(visible){
				Color c = new Color(0,0,0,50);
				g2d.setColor(c);
				g2d.fillRect(x,y,w,h);
				g2d.setColor(Color.white);
				Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(input,g2d);
				if(stringBounds.getWidth()+4*Game.SCALE>=w-4*Game.SCALE){
					allowInput = false;
				}else{
					allowInput = true;
				}
				g2d.drawString(">", x,(int)(y+getHeight()-stringBounds.getHeight()+(5*Game.SCALE)));
				g2d.drawString(input,x+4*Game.SCALE,(int)(y+getHeight()-stringBounds.getHeight()+(5*Game.SCALE)));
				int totalHeight = (int)(stringBounds.getHeight());
				for(String s: consoleHistory){
					stringBounds = g2d.getFontMetrics().getStringBounds(s, g2d);
					g2d.drawString(s,x,(int)((y+getHeight())-(stringBounds.getHeight()*consoleHistory.indexOf(s))-stringBounds.getHeight()));
					totalHeight+=stringBounds.getHeight();
				}
				if(totalHeight>=getHeight()){
					consoleHistory.remove(consoleHistory.size()-1);
				}
			}else{
				allowInput = false;
			}
		}
	}
}
