package game.main.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.id.ObjectID;
import game.main.Controller;
import game.main.Game;
import game.net.OnlinePlayer;
import game.util.classes.GameObject;
import game.util.classes.KeyInput;
import game.util.classes.MouseInput;

public class graphics extends JPanel implements ActionListener{

	/*
	 * Graphics for the whole game.
	 * Runs the controller render method
	 * Handles camera update and render
	 * calls.
	 * Updates mouse X&Y for camera.
	 * Changes color of background when
	 * controller.inMenu = false.
	 */
	private static final long serialVersionUID = 1L;
	private static final int DELAY = 10;
	public static float VOLUME = -50.0f;
	private Timer timer;
	private JFrame frame;
	private long colorBG = 0;
	public static double mouseX,mouseY;
	
	public static Controller controller;
	public static KeyInput keyInput;
	public static MouseInput mouseInput;
	
	public graphics(){
		initGraphics();
	}
	
	private void initGraphics() {
		addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
			
		});
		setFocusable(true);
		setBackground(Color.BLUE);
		
		controller = new Controller();
		keyInput = new KeyInput(controller);
		mouseInput = new MouseInput(controller);
		
		addKeyListener(keyInput);
		
		addMouseListener(mouseInput);
		
		timer = new Timer(DELAY,this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		draw(g);
		
		//keep game width & height updated
		Game.WIDTH = Game.frame.getWidth();
		Game.HEIGHT = Game.frame.getHeight();
		
		Toolkit.getDefaultToolkit().sync();
	}
	public void draw(Graphics g){
//		Graphics2D g2d = (Graphics2D) g;
		//color background
		if(!controller.inMenu && !controller.inLobby){
			if(System.currentTimeMillis()-colorBG>=.5*1000){
				int r = (int)(Math.random()*255);
				int green = (int)(Math.random()*255);
				int b = (int)(Math.random()*255);
				while(r<=50 && green<=50 && b<=50){
					r = (int)(Math.random()*255);
					green = (int)(Math.random()*255);
					b = (int)(Math.random()*255);
				}
				setBackground(new Color(r,green,b));
				colorBG = System.currentTimeMillis();
			}
		}else{
			setBackground(Color.blue);
		}
		controller.render(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.update();
		if(controller.volumeControl.getSlidePercent()==0){
			VOLUME = -50.0f;
		}else{
			VOLUME = (float)((controller.volumeControl.getSlidePercent()-75)/5);
		}
		repaint();
	}
}