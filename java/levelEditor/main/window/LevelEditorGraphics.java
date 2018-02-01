package levelEditor.main.window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.main.Game;
import game.util.classes.ImageLoader;
import game.util.classes.SpriteSheet;
import levelEditor.main.LevelEditor;
import levelEditor.main.window.LevelEditorPoint.LevelEditorID;

public class LevelEditorGraphics extends JPanel implements ActionListener, MouseListener, KeyListener,MouseMotionListener{

	/*
	 * graphics for the level editor.
	 * get the file that the user selects, then creates
	 * a copy of the file in the folder that the jar is
	 * in, which it then writes the coordinates and type 
	 * of object in a file that is the selected file's 
	 * name but instead of an image file it's a .txt file.
	 */
	private static final long serialVersionUID = 1L;
	private static final int DELAY = 10;
	private static int xOffset = 75;
	private Timer timer;
	private JFrame frame;
	private String currentTool = "";
	private int yCamOffset,xCamOffset;
	private boolean wDown,aDown,sDown,dDown;
	private ArrayList<LevelEditorButton> buttons;
	private File levelpath;
	private BufferedImage level;
	private FileDialog imageChoose;
	private ImageLoader loader;
	private PrintWriter out;
	private BufferedImage pistol,rifle,shotgun,paintbrush;
	private SpriteSheet weapons;
	private ArrayList<LevelEditorPoint> points;
	private boolean inJar;
	
	private int startX,startY,endX,endY;
	
	
	
	public LevelEditorGraphics(){
		//initialize all vars
		loader = new ImageLoader();
		points = new ArrayList<LevelEditorPoint>();
		imageChoose = new FileDialog(frame, "Load Level Image",FileDialog.LOAD);
		imageChoose.setVisible(true);
			level = loader.loadImage(imageChoose.getDirectory()+imageChoose.getFile(),false);
			try {
				String tempStr = imageChoose.getFile();
				if(tempStr==null){
					imageChoose.setDirectory("/levelImgs/");
					imageChoose.setFile("defaultLevelLoad.jpg");
					tempStr = imageChoose.getFile();
					level = loader.loadImage(imageChoose.getDirectory()+imageChoose.getFile(),false);
				}
				String jarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String levelFolder = "";
				String jarCheck = LevelEditor.class.getResource("LevelEditor.class").toString();
				boolean isJar = jarCheck.startsWith("jar:");
				inJar = isJar;
				if(!isJar){
					levelFolder = jarLoc.substring(0,jarLoc.lastIndexOf("/",jarLoc.lastIndexOf("/")-1));
				}else{
					levelFolder = jarLoc.substring(0,jarLoc.lastIndexOf("/"));
				}
				String fileName = tempStr.substring(0,tempStr.indexOf('.'));
				File source = new File(imageChoose.getDirectory()+imageChoose.getFile());
				String pasteLoc = levelFolder;
				//make loop to loop through entire string
				while(pasteLoc.indexOf("%20")!=-1){
					String temp = pasteLoc.substring(0,pasteLoc.indexOf("%20")) + " "
							+ pasteLoc.substring(pasteLoc.indexOf("%20")+3,pasteLoc.length());
					pasteLoc = temp;
					levelFolder = pasteLoc;
				}
				File tempDir = new File(levelFolder+"/levels/");
				if(!tempDir.exists()){
					tempDir.mkdirs();
				}
				tempDir = new File(levelFolder+"/levelImgs/");
				if(!tempDir.exists()){
					tempDir.mkdirs();
				}
				File levelText = new File(levelFolder+"/levels/"+fileName);
				pasteLoc = levelText.getPath();
				while(pasteLoc.indexOf("%20")!=-1){
					String temp = pasteLoc.substring(0,pasteLoc.indexOf("%20")) + " "
							+ pasteLoc.substring(pasteLoc.indexOf("%20")+3,pasteLoc.length());
					pasteLoc = temp;
					levelFolder = pasteLoc;
				}
				out = new PrintWriter(levelText+".txt", "UTF-8");
				File pasteFile = new File(levelFolder+"/levelImgs/"+source.getName());
				if(!pasteFile.exists()){
					pasteFile.mkdir();
				}
				Files.copy(source.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		initGraphics();
	}
	
	private void initGraphics() {
		//add listeners to jpanel and init more vars
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		setFocusable(true);
		setBackground(Color.white);

		buttons = new ArrayList<LevelEditorButton>();
		buttons.add(new LevelEditorButton("Save",0,40,50,50));
		buttons.add(new LevelEditorButton("Rectangle",0,100,50,50));
		buttons.add(new LevelEditorButton("Paintbrush",0,160,50,50));
		buttons.add(new LevelEditorButton("Pistol",0,220,50,50));
		buttons.add(new LevelEditorButton("Rifle",0,280,50,50));
		buttons.add(new LevelEditorButton("Shotgun",0,350,50,50));
		buttons.add(new LevelEditorButton("SpawnPoint",0,410,50,50));
		
		
		initWeapons();
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	private void initWeapons(){
		//get images for weapons
//		weapons = new SpriteSheet("res/sprites/weaponSpriteSheet.png");
		if(inJar){
			weapons = new SpriteSheet("/resources/sprites/weaponSpriteSheet.png");
		}else{
			weapons = new SpriteSheet("/sprites/weaponSpriteSheet.png");
		}

		pistol = weapons.getSprite(204,19,17,10);
		rifle = weapons.getSprite(13, 18, 33, 11);
		shotgun = weapons.getSprite(13, 81, 30, 12);
		paintbrush = weapons.getSprite(200,200,25,15);
	}

	@Override
	public void paintComponent(Graphics g){
		//calls draw & syncs it
		super.paintComponent(g);
		
		draw(g);
		Toolkit.getDefaultToolkit().sync();
	}
	public void draw(Graphics g){
		//draws graphics to screen
		Graphics2D g2d = (Graphics2D)g;
		if(level!=null){
			g2d.drawImage(level,75-xCamOffset,0-yCamOffset,level.getWidth()*LevelEditor.SCALE,level.getHeight()*LevelEditor.SCALE,this);
		}
		for(LevelEditorButton button: buttons){
			button.render(g);
		}
		for(LevelEditorPoint s: points){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			g2d.setColor(Color.black);
			switch(s.getId()){
			case RECT:
				if(s.getEndPoint().x-s.getStartPoint().x>0 && s.getEndPoint().y-s.getStartPoint().y>0){
					g2d.fillRect(s.getStartPoint().x-xCamOffset, s.getStartPoint().y-yCamOffset, 
							s.getEndPoint().x-s.getStartPoint().x, s.getEndPoint().y-s.getStartPoint().y);
				}else if(s.getEndPoint().x-s.getStartPoint().x<0 && s.getEndPoint().y-s.getStartPoint().y<0){
					g2d.fillRect(s.getEndPoint().x-xCamOffset, s.getEndPoint().y-yCamOffset, 
							s.getStartPoint().x-s.getEndPoint().x, s.getStartPoint().y-s.getEndPoint().y);
				}else if(s.getEndPoint().x-s.getStartPoint().x<0){
					g2d.fillRect(s.getEndPoint().x-xCamOffset, s.getStartPoint().y-yCamOffset, 
							s.getStartPoint().x-s.getEndPoint().x, s.getEndPoint().y-s.getStartPoint().y);
				}else if(s.getEndPoint().y-s.getStartPoint().y<0){
					g2d.fillRect(s.getStartPoint().x-xCamOffset, s.getEndPoint().y-yCamOffset,
							s.getEndPoint().x-s.getStartPoint().x, s.getStartPoint().y-s.getEndPoint().y);
				}
			break;
			case SPAWN:
				g2d.fillOval(s.getStartPoint().x-xCamOffset, s.getStartPoint().y-yCamOffset, 10,10);
			break;
			case PISTOL:
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, 25,25);
				g2d.drawImage(pistol, s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, null);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			break;
			case RIFLE:
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, 25,25);
				g2d.drawImage(rifle, s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, null);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			break;
			case SHOTGUN:
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, 25,25);
				g2d.drawImage(shotgun, s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, null);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			break;
			case PAINTBRUSH:
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, 25,25);
				g2d.drawImage(paintbrush, s.getStartPoint().x-xCamOffset,s.getStartPoint().y-yCamOffset, null);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			break;
			}
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
		}
		switch(currentTool){
		case "Rectangle":
			if(Math.abs(endX-startX)>=2 && Math.abs(endY-startY)>=2){
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
				g2d.setColor(Color.black);
				if(endX-startX>0 && endY-startY>0){
					g2d.fillRect(startX, startY, endX-startX, endY-startY);
				}else if(endX-startX<0 && endY-startY<0){
					g2d.fillRect(endX, endY, startX-endX, startY-endY);
				}else if(endX-startX<0){
					g2d.fillRect(endX, startY, startX-endX, endY-startY);
				}else if(endY-startY<0){
					g2d.fillRect(startX, endY, endX-startX, startY-endY);
				}
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
			}
		break;
		case "SpawnPoint":
			g2d.setColor(Color.BLACK);
			g2d.fillOval(startX, startY, 10,10);
		break;
		case "Pistol":
			g2d.setColor(Color.blue);
			g2d.fillRect(startX, startY, 25, 25);
		break;
		case "Rifle":
			g2d.setColor(Color.yellow);
			g2d.fillRect(startX, startY, 25, 25);
		break;
		case "Shotgun":
			g2d.setColor(Color.green);
			g2d.fillRect(startX, startY, 25, 25);
		break;
		case "PaintBrush":
			g2d.setColor(Color.red);
			g2d.fillRect(startX, startY, 25, 25);
		break;
		}
		
		//check for input
		if(wDown){
			yCamOffset-=1;
		}
		if(aDown){
			xCamOffset-=1;
		}
		if(sDown){
			yCamOffset+=1;
		}
		if(dDown){
			xCamOffset+=1;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == 'w'){
			wDown = true;
		}
		if(e.getKeyChar() == 'd'){
			dDown = true;
		}
		if(e.getKeyChar() == 'a'){
			aDown = true;
		}
		if(e.getKeyChar() == 's'){
			sDown = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == 'w'){
			wDown = false;
		}
		if(e.getKeyChar() == 'd'){
			dDown = false;
		}
		if(e.getKeyChar() == 'a'){
			aDown = false;
		}
		if(e.getKeyChar() == 's'){
			sDown = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//sets startX&Y & saves all shapes in 
		//arraylist shapes to a .txt file
		for(LevelEditorButton button: buttons){
			if(button.isPressed(e)){
				currentTool = button.getName();
			}
		}
		if(e.getX()>=xOffset){
			switch(currentTool){
			case "Rectangle":
			case "SpawnPoint":
			case "Pistol":
			case "Rifle":
			case "Shotgun":
			case "Paintbrush":
				startX = e.getX();
				startY = e.getY();
			break;
			default:
				System.err.println("Cound not find Tool");
			break;
			}
		}
		if(currentTool.equals("Save")){
			for(LevelEditorPoint s: points){
				int finalX = 0;
				int endFinalX = 0;
				switch(s.getId()){
				case RECT:
					finalX = s.getStartPoint().x-xOffset;
					endFinalX = s.getEndPoint().x-xOffset;
					
					int width = s.getEndPoint().x-s.getStartPoint().x;
					int height = s.getEndPoint().y-s.getStartPoint().y;
					if(s.getEndPoint().x-s.getStartPoint().x>0 && s.getEndPoint().y-s.getStartPoint().y>0){
						width = s.getEndPoint().x-s.getStartPoint().x;
						height = s.getEndPoint().y-s.getStartPoint().y;
						out.println("RECT:(" + finalX + "," + s.getStartPoint().y + 
						") [" + width + "," + height + "]");
					}else if(s.getEndPoint().x-s.getStartPoint().x<0 && s.getEndPoint().y-s.getStartPoint().y<0){
						width = s.getStartPoint().x-s.getEndPoint().x;
						height = s.getStartPoint().y-s.getEndPoint().y;
						out.println("RECT:(" + endFinalX + "," + s.getEndPoint().y + 
						") [" + width + "," + height + "]");
					}else if(s.getEndPoint().x-s.getStartPoint().x<0){
						width = s.getStartPoint().x-s.getEndPoint().x;
						height = s.getEndPoint().y-s.getStartPoint().y;
						out.println("RECT:(" + endFinalX + "," + s.getStartPoint().y + 
						") [" + width + "," + height + "]");
					}else if(s.getEndPoint().y-s.getStartPoint().y<0){
						width = s.getEndPoint().x-s.getStartPoint().x;
						height = s.getStartPoint().y-s.getEndPoint().y;
						out.println("RECT:(" + finalX + "," + s.getEndPoint().y + 
						") [" + width + "," + height + "]");
					}
				break;
				case SPAWN:
				case PISTOL:
				case SHOTGUN:
				case RIFLE:
				case PAINTBRUSH:
					finalX = s.getStartPoint().x-xOffset;
					out.println(s.getId().toString() + ":(" + finalX + "," + s.getStartPoint().y + ")");
				break;
				default:
					System.err.println("NO SHAPE FOUND Shape: " + s.toString());
				break;
				}
			}
			JOptionPane saved = new JOptionPane();
			this.add(saved);
			saved.setVisible(true);
			saved.showMessageDialog(this, "Level Saved!");
			out.close();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getX()>=xOffset){
			switch(currentTool){
				case "Rectangle":
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset),new Point(endX+xCamOffset,endY+yCamOffset), LevelEditorID.RECT));
				break;
				case "SpawnPoint": 
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset), new Point(startX+xCamOffset+10,startY+yCamOffset+10), LevelEditorID.SPAWN));
				break;
				case "Pistol": 
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset), new Point(startX+xCamOffset+10,startY+yCamOffset+10), LevelEditorID.PISTOL));
				break;
				case "Rifle": 
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset), new Point(startX+xCamOffset+10,startY+yCamOffset+10), LevelEditorID.RIFLE));
				break;
				case "Paintbrush": 
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset), new Point(startX+xCamOffset+10,startY+yCamOffset+10), LevelEditorID.PAINTBRUSH));
				break;
				case "Shotgun": 
					points.add(new LevelEditorPoint(new Point(startX+xCamOffset,startY+yCamOffset), new Point(startX+xCamOffset+10,startY+yCamOffset+10), LevelEditorID.SHOTGUN));
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getX()>xOffset){
			switch(currentTool){
			case "Rectangle":
				endX = e.getX();
				endY = e.getY();
			break;
			default:
			break;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
