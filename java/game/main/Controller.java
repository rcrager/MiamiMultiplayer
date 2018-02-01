
package game.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import game.id.ObjectID;
import game.id.Weapons;
import game.levels.Level;
import game.main.window.Camera;
import game.main.window.graphics;
import game.net.Client;
import game.net.OnlinePlayer;
import game.net.Server;
import game.net.packets.Packet00Connect;
import game.net.packets.Packet10StartGame;
import game.net.packets.Packet11EndGame;
import game.objects.Player;
import game.util.classes.Animation;
import game.util.classes.Button;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;
import game.util.classes.InputBox;
import game.util.classes.Slider;
import game.util.classes.Sound;
import game.util.classes.WindowHandler;

public class Controller{
	/*
	 * Controls everything in the game,
	 * the update for each object,
	 * the render for each object,
	 * and the update for the menus.
	 */
	public List<GameObject> gameObjects;
	
	
	ImageLoader loader;
	WindowHandler WH;
	ArrayList<Level> levels = new ArrayList<Level>();
	Level currentLevel;
	public Server server;
	public Client client;
	public Player player;
	public Camera camera;
	public static ArrayList<Sound> sounds;
	
	//main menu variables
	public boolean inMenu = true;
	public boolean inLobby = false;
	public boolean inGame = false;
	private Button startServer;
	private Button startClient;
	private Button startGame;
	private InputBox nameInput;
	private InputBox ipInput;
	private boolean showIp = false;
	private int guiCenterX = 0;
	private int guiCenterY = 0;
	private int guiScale = Game.SCALE;
	private int gameTimer = ((2*60)*1000);
	private long lastMusicLoop = 0;
	private Sound menuMusic = null;
	public Slider volumeControl;
	private long startTime = 0;
	//lobby variables
	private ArrayList<GameObject> gui = new ArrayList<GameObject>();
	//add console so host can end game
	private InputBox consoleListen;
	
	private boolean isJar;
	
	public Controller(){
		//instantiate all variables
		String isJarString = getClass().getResource("/levels/").toString();
		if(isJarString.startsWith("jar")){
			isJar = true;
		}else{
			isJar = false;
		}
		
		WH = new WindowHandler(Game.frame,this);
		camera = new Camera(0,0);
		sounds = new ArrayList<Sound>();
		loadSounds();
		menuMusic = Controller.sounds.get(0);
		
		guiCenterX = Game.frame.getWidth()/2;
		guiCenterY = Game.frame.getHeight()/2;
		consoleListen = new InputBox(guiCenterX-Game.frame.getWidth()/4,guiCenterY-Game.frame.getHeight()/4,Game.frame.getWidth()/2,Game.frame.getHeight()/2,"Console");
		nameInput = new InputBox(guiCenterX,guiCenterY-25/2,100*Game.SCALE,25*Game.SCALE,"Enter Name:");
		ipInput = new InputBox(guiCenterX-50/2,guiCenterY+50/2,100*Game.SCALE,25*Game.SCALE,"Enter IP:");
		
		startServer = new Button(guiCenterX-(100*Game.SCALE)/2,guiCenterY-150/2,100*Game.SCALE,25*Game.SCALE,"Server");
		startClient = new Button(guiCenterX+200/2,guiCenterY-150/2,100*Game.SCALE,25*Game.SCALE,"Client");
		startGame = new Button(guiCenterX,guiCenterY+150/2,100*Game.SCALE,25*Game.SCALE,"Start Game");
		
		volumeControl = new Slider(guiCenterX-100*Game.SCALE/2,guiCenterY-(25*3)*Game.SCALE,100*Game.SCALE,10*Game.SCALE,"Volume:");
		
		gameObjects = new ArrayList<GameObject>();
		gameObjects.add(nameInput);
		gameObjects.add(startServer);
		gameObjects.add(startClient);
		gameObjects.add(startGame);
		
		gameObjects.add(volumeControl);
		
		loader = new ImageLoader();
		//******load in all levels here*******
		String jarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String levelFolder = jarLoc.substring(0,jarLoc.lastIndexOf("/"));
		File f = new File(levelFolder + "/levels/");
		File v = new File(levelFolder + "/levelImgs/");
		String pasteLoc = f.getPath();
		String imgsLoc = v.getPath();
		while(pasteLoc.indexOf("%20")!=-1){
			String temp = pasteLoc.substring(0,pasteLoc.indexOf("%20")) + " "
					+ pasteLoc.substring(pasteLoc.indexOf("%20")+3,pasteLoc.length());
			pasteLoc = temp;
			levelFolder = pasteLoc;
			f = new File(levelFolder);
		}
		while(imgsLoc.indexOf("%20")!=-1){
			String temp = imgsLoc.substring(0,imgsLoc.indexOf("%20")) + " "
					+ imgsLoc.substring(imgsLoc.indexOf("%20")+3,imgsLoc.length());
			imgsLoc = temp;
			levelFolder = imgsLoc;
			v = new File(levelFolder);
		}
		if(f.exists() && v.exists()){
			for(String s: f.list()){
				String levelName = s.substring(0, s.indexOf("."));
				for(String g: v.list()){
					if(levelName.equals(g.substring(0, g.indexOf(".")))){
						if(!s.equals(".DS_Store")&&!g.equals(".DS_Store")){
							if(!s.equals("defaultLevelLoad.txt") && !g.equals("defaultLevelLoad.jpg")){
								File levelFile = new File(levelFolder+"/" + g);
								Level temp = new Level(levelFile.getPath(),false);
								levels.add(temp);
							}
						}
					}
				}
			}
		}else{
			f.mkdir();
			v.mkdir();
		}
		if(isJar){
			Level temp = new Level("/levelImgs/House.png",true);
			levels.add(temp);
			temp = new Level("/levelImgs/Level2.png",true);
			levels.add(temp);
		}
		consoleListen.console = true;
		consoleListen.setVisible(false);
	}
	private void loadSounds(){
		sounds.add(new Sound("/sounds/music/main_menu_music.wav"));
		sounds.add(new Sound("/sounds/sfx/weapon_pickup.wav"));
		sounds.add(new Sound("/sounds/sfx/weapon_drop.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_shot01.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_shot02.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_shot03.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_shot04.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_hitBody.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_hit01.wav"));
		sounds.add(new Sound("/sounds/sfx/paintball_hit02.wav"));
	}
	
	public void addGameObject(GameObject GM){
		gameObjects.add(GM);
	}
	public void removeGameObject(GameObject GM){
		GameObject removeGM = null;
		for(GameObject gm: gameObjects){
			if(gm.equals(GM)){
				removeGM = gm;
			}
		}
		if(removeGM!=null){
			gameObjects.remove(removeGM);
		}	
	}
	
	public void changeLevel(Level level){
		//remove all objects from current level then changes to new level
		inLobby = false;
		inMenu = false;
		inGame = true;
		ArrayList<GameObject> removeObjs = new ArrayList<GameObject>();
		ArrayList<OnlinePlayer> players = new ArrayList<OnlinePlayer>();
			for(GameObject gm: gameObjects){
				if(gm instanceof OnlinePlayer){
					((OnlinePlayer)gm).setVisible(false);
					((OnlinePlayer)gm).setWeapon(Weapons.Fist);
					players.add((OnlinePlayer)gm);
				}else{
					removeObjs.add(gm);
				}
			}
			if(removeObjs!=null){
				gameObjects.removeAll(removeObjs);
			}
		//****set CurrentLevel****
		currentLevel = level;
		gameObjects.addAll(currentLevel.loadCollisions(currentLevel.getCollisionPath()));
		//add console listener
		gameObjects.add(consoleListen);
		for(OnlinePlayer p: players){
			p.respawn();
			//^^^^^^^^^^
			//make player respawn either far
			//away from their current pos or
			//just not the closest spawn point.
			p.setVisible(true);
		}
		startTime = System.currentTimeMillis();
	}
	
	public void changeOnlineLevel(Level nextLvl, int onlineId) {
		if(!getOnlinePlayer(onlineId).getLocal()){
			changeLevel(nextLvl);
			inLobby = false;
		}else{
			Packet10StartGame lvlPack = new Packet10StartGame(((OnlinePlayer)player).getOnlineId(),nextLvl.getImagePath(),nextLvl.getCollisionPath());
			lvlPack.writeData(client);
			changeLevel(nextLvl);
		}
	}

	private void startServer(){
		//start server and connect yourself as host
		server = new Server(this,4445);
		server.start();
		client = new Client(this,"127.0.0.1",4445);
		client.start();
		if(!nameInput.getText().equals("")){
			player = new OnlinePlayer(client,0, 0, ObjectID.Player,-1,nameInput.getText(), null, -1,this);
		}else{
			player = new OnlinePlayer(client,0,0, ObjectID.Player,-1,"Player", null, -1, this);
		}
		((OnlinePlayer)player).setLocal(true);
		addGameObject(player);
		if(currentLevel==null){
			((OnlinePlayer)player).setVisible(false);
		}
		((OnlinePlayer)player).setHost(true);
		//connect client to server
		Packet00Connect conPack = new Packet00Connect(((OnlinePlayer)player).getOnlineId(), ((OnlinePlayer)player).getName());
		conPack.writeData(client);
	}
	
	private void startClient(){
		//connect to ip providing it's valid
		if(!ipInput.getText().equals("")){
			client = new Client(this,ipInput.getText(),4445);
			client.start();
		}else{
			client = new Client(this,"127.0.0.1",4445);
			client.start();
		}
		if(!nameInput.getText().equals("")){
			player = new OnlinePlayer(client,0, 0, ObjectID.Player,-1,nameInput.getText(), null, -1,this);
		}else{
			player = new OnlinePlayer(client,0,0, ObjectID.Player,-1,"Player", null, -1, this);
		}
		((OnlinePlayer)player).setLocal(true);
		addGameObject(player);
		if(currentLevel==null){
			((OnlinePlayer)player).setVisible(false);
		}
		((OnlinePlayer)player).setHost(false);
		//connect client to server
		Packet00Connect conPack = new Packet00Connect(((OnlinePlayer)player).getOnlineId(), ((OnlinePlayer)player).getName());
		conPack.writeData(client);
	}
	
	private void menuUpdate(){
		//update menu while in menu
		guiCenterX = (int)(Game.frame.getWidth()/2.0);
		guiCenterY = (int)(Game.frame.getHeight()/2.0);
		double dec = 0.0;
		guiScale = Game.frame.getWidth()/(12*9)/3;
		if(inMenu && !inLobby){		
			if(startGame.isPressed()){
				if(startServer.isPressed()){
					startServer();
					inMenu = false;
					inLobby = true;
					startGame.setPressed(false);
					startServer.setPressed(false);
					gameObjects.remove(nameInput);
					gameObjects.remove(ipInput);
					gameObjects.remove(startServer);
					gameObjects.remove(startClient);
					gameObjects.remove(startGame);
					gameObjects.remove(volumeControl);
	
				}else if(startClient.isPressed()){
					startClient();
					inMenu = false;
					inLobby = true;
					startGame.setPressed(false);
					startClient.setPressed(false);
					gameObjects.remove(nameInput);
					gameObjects.remove(ipInput);
					gameObjects.remove(startServer);
					gameObjects.remove(startClient);
					gameObjects.remove(startGame);
					gameObjects.remove(volumeControl);
				}else{
					JOptionPane.showMessageDialog(Game.frame, "Please select Server or Client.");
					startGame.setPressed(false);
				}
			}
		}
		
		nameInput.setScale(guiScale);
		nameInput.setX(guiCenterX-nameInput.getWidth()/2);
		nameInput.setY(guiCenterY-((nameInput.getHeight())/2));
		ipInput.setScale(guiScale);
		ipInput.setX(guiCenterX-ipInput.getWidth()/2);
		ipInput.setY(guiCenterY+(ipInput.getHeight()/2));
		startServer.setScale(guiScale);
		dec = startServer.getWidth()*(guiScale/2.0);
		startServer.setX(guiCenterX-(int)(dec));
		startServer.setY(guiCenterY-startServer.getHeight()*2);
		startClient.setScale(guiScale);
		dec = startClient.getWidth();
		if(guiScale > 2){
			startClient.setX(guiCenterX+(int)(dec));
		}else{
			startClient.setX(guiCenterX);
		}
		startClient.setY(guiCenterY-startClient.getHeight()*2);
		startGame.setScale(guiScale);
		startGame.setX(guiCenterX-startGame.getWidth()/2);
		startGame.setY(guiCenterY+guiCenterY/2);
		
		volumeControl.setX(guiCenterX-100*Game.SCALE/2);
		volumeControl.setY(guiCenterY-(25*3)*Game.SCALE);
		
		if(startClient.isPressed() && !showIp){
			gameObjects.add(ipInput);
			showIp = true;
			startServer.setPressed(false);
		}
		if(startServer.isPressed()){
			gameObjects.remove(ipInput);
			showIp = false;
			startClient.setPressed(false);
		}
	}
	
	private void lobbyUpdate(){
		guiCenterX = Game.frame.getWidth()/2;
		guiCenterY = Game.frame.getHeight()/2;
		if(currentLevel!=null && !inGame){
			//clear current level if there is one
			ArrayList<GameObject> removes = new ArrayList<GameObject>();
			for(GameObject gm: gameObjects){
				if(gm instanceof OnlinePlayer){
					((OnlinePlayer)gm).setVisible(false);
					((OnlinePlayer)gm).setWeapon(Weapons.Fist);
				}else{
					removes.add(gm);
				}
			}
			if(!removes.isEmpty()){
				gameObjects.removeAll(removes);
			}
			currentLevel = null;
		}
		for(GameObject gm: gui){
			if((gui.indexOf(gm)%2)==0 || gui.indexOf(gm)==0){
				//if obj indx = even
				//put on left side of guiCenterX
				gm.setX(guiCenterX-(gui.indexOf(gm)*75)-75/2);
				gm.setY(guiCenterY);
				gm.setWidth(75);
				gm.setHeight(80);
			}else{
				//put on right side
				gm.setX(guiCenterX+(gui.indexOf(gm)*75)+75/2);
				gm.setY(guiCenterY);
				gm.setWidth(75);
				gm.setHeight(80);
			}
		}
		if(gui.isEmpty()){
			//add all gui here...called once
			if(((OnlinePlayer)player).isHost()){
				gameObjects.add(startGame);
				for(Level l: levels){
					Button levelButton;
					if((levels.indexOf(l)%2)==0 || levels.indexOf(l)==0){
						//if obj indx = even
						//put on left side of guiCenterX
						levelButton = new Button(guiCenterX-(levels.indexOf(l)*75)-75/2,guiCenterY,75,80,l.getName());	
					}else{
						//put on right side
						levelButton = new Button(guiCenterX+(levels.indexOf(l)*75)+75/2,guiCenterY,75,80,l.getName());
					}
					levelButton.setImage(loader.loadImage(l.getImagePath(),l.isLocal()));
					gui.add(levelButton);
				}
				for(GameObject gm: gui){
					gameObjects.add(gm);
				}
			}
		}
		
		startGame.setX(guiCenterX-startGame.getWidth()/2);
		startGame.setY(guiCenterY+guiCenterY/2);
		if(startGame.isPressed()){
			Level nextLevel = null;
			for(GameObject gm: gui){
				if(gm instanceof Button){
					if(((Button)gm).isPressed()){
						nextLevel = levels.get(gui.indexOf(gm));
					}
				}
			}
			if(nextLevel == null){
				JOptionPane.showMessageDialog(Game.frame, "Please select a level.");
				startGame.setPressed(false);
			}else{
				inLobby = false;
				startGame.setPressed(false);
				gui.clear();
				changeOnlineLevel(nextLevel,((OnlinePlayer)player).getOnlineId());
			}
		}
	}
	
	private void lobbyRender(Graphics g){
		String players = "Players In Game";
		Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(players,g);
		g.setColor(Color.white);
		g.drawString(players, (int)(guiCenterX-stringBounds.getWidth()/2), (int)(stringBounds.getHeight()));
		for(GameObject gm: gameObjects){
			if(gm instanceof OnlinePlayer){
				players = ((OnlinePlayer)gm).getName();
				stringBounds = g.getFontMetrics().getStringBounds(players,g);
				g.drawString(((OnlinePlayer)gm).getName(), (int)(guiCenterX-stringBounds.getWidth()/2), (int)((stringBounds.getHeight()*2)*(((OnlinePlayer)gm).getOnlineId()+1)));
			}
		}
	}

	public void update(){
		//calls update method for every object in level
		//locked to 60 fps
		guiCenterX = Game.frame.getWidth()/2;
		guiCenterY = Game.frame.getHeight()/2;
		consoleListen.setX(guiCenterX-Game.frame.getWidth()/4);
		consoleListen.setY(guiCenterY-Game.frame.getHeight()/4);
		consoleListen.setWidth(Game.frame.getWidth()/2);
		consoleListen.setHeight(Game.frame.getHeight()/2);
		
		
		if(lastMusicLoop==0){ 
			menuMusic.setVolume(graphics.VOLUME);
			menuMusic.loop(-1);
			lastMusicLoop = System.currentTimeMillis();
		}
		if(inMenu){
			menuMusic.setVolume(graphics.VOLUME);
			menuUpdate();
		}else{
			if(menuMusic!=null){
				menuMusic.stopSound();
				menuMusic.close();
			}
		}
		if(consoleListen.endGame && ((OnlinePlayer)player).isHost()){
			Packet11EndGame endPack = new Packet11EndGame(((OnlinePlayer)player).getOnlineId());
			endPack.writeData(client);
			inLobby = true;
			inGame = false;
			consoleListen.endGame = false;
		}
		if(!inMenu && !inGame && inLobby){
			lobbyUpdate();
		}
		if(!inMenu && !inLobby){
			camera.update(player);
		}
		for(int j = 0; j<=gameObjects.size()-1; j++){
			GameObject gm = gameObjects.get(j);
			gm.update(gameObjects);
		}
	}
	
	public void render(Graphics g){
		//calls render for each object in level
		//according to their layer; limited slightly
		ArrayList<GameObject> gui = new ArrayList<GameObject>();
		g.translate(camera.getX(), camera.getY());
		//put gui outside this^^
		if(!inMenu && System.currentTimeMillis()-startTime>=gameTimer && ((OnlinePlayer)player).isHost()){
				inLobby = true;
				inGame = false;
				Packet11EndGame endPack = new Packet11EndGame(((OnlinePlayer)player).getOnlineId());
				endPack.writeData(client);
		}
		if(inGame){
			currentLevel.render(g);
		}
		int layers = 10;
		for(int x = layers; x>=-1; x--){
			for(int j = 0; j<=gameObjects.size()-1; j++){
				if(gameObjects.get(j).getLayer()==x){
					if(gameObjects.get(j).getId()!=ObjectID.Input){						
						gameObjects.get(j).render(g);
					}else{
						gui.add(gameObjects.get(j));
					}
				}
			}
		}
		g.translate(-camera.getX(), -camera.getY());
		//render gui boxes
		if(!gui.isEmpty()){
			for(GameObject gm: gui){
				gm.render(g);
			}
		}
		if(inLobby){
			Font font = new Font("Lucida Console", Font.PLAIN, 18);
			g.setFont(font);
			lobbyRender(g);
		}
		if(!inMenu && !inLobby){
			Font font = new Font("Lucida Console", Font.PLAIN, 18);
			g.setFont(font);
			int seconds = (int)(((System.currentTimeMillis()-startTime)-gameTimer)*-1)/1000;
			int min = seconds/60;
			if(min>0){
				seconds-=(min*60);
			}
			String output = "";
			if(seconds>=10){
				output = "Time Left: " + min + ":" + seconds;
			}else{
				output = "Time Left: " + min + ":0" + seconds;
			}
			Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(output,g);
			g.setColor(Color.white);
			g.fillRect((int)(Game.frame.getWidth()/2-stringBounds.getWidth()/2),0,(int)(stringBounds.getWidth()),(int)(stringBounds.getHeight()));
			g.setColor(Color.black);
			g.drawString(output, (int)(Game.frame.getWidth()/2-stringBounds.getWidth()/2),(int)(stringBounds.getHeight()));
		}
	}
	
	public void removeOnlinePlayer(int onlineId) {
		GameObject removePlayer = null;
		for(GameObject gm: gameObjects){
			if(gm instanceof OnlinePlayer){
				if(((OnlinePlayer)gm).getOnlineId()==onlineId){
					removePlayer = gm;
				}
			}
		}
		if(removePlayer!=null){
			gameObjects.remove(removePlayer);
		}
	}

	public void moveOnlinePlayer(int onlineId, int x, int y) {
		int index = getPlayerIndex(onlineId);
		if(!((OnlinePlayer)gameObjects.get(index)).getLocal()){
			this.gameObjects.get(index).x = x;
			this.gameObjects.get(index).y = y;
		}
	}
	public void rotateOnlinePlayer(int onlineId, int rot) {
		int index = getPlayerIndex(onlineId);
		if(!((OnlinePlayer)gameObjects.get(index)).getLocal()){
			this.gameObjects.get(index).setRotation(rot);
		}
	}
	
	public void setOnlinePlayerImage(int onlineId, String animName, int animIndex) {
		int index = getPlayerIndex(onlineId);
		if(!((OnlinePlayer)gameObjects.get(index)).getLocal()){
			OnlinePlayer player = (OnlinePlayer)this.gameObjects.get(index);
			for(Animation anim: player.animations){
				if(anim.getName().equals(animName)){
					anim.setImageIndex(animIndex);
					player.currentAnim = anim;
				}
			}
		}
	}
	
	public void respawnOnlinePlayer(int onlineId){
		LinkedList<GameObject> spawns = currentLevel.loadSpawns();
		int index = getPlayerIndex(onlineId);
		int spawn = (int)(Math.random()*spawns.size());
		if(spawns.size()>0){
			GameObject spawnPoint = spawns.get(spawn);
			if(((OnlinePlayer)gameObjects.get(index)).getLocal()){
				this.gameObjects.get(index).x = spawnPoint.getX();
				this.gameObjects.get(index).y = spawnPoint.getY();
				((OnlinePlayer)gameObjects.get(index)).sendPos();
			}
		}else{
				this.gameObjects.get(index).x = 100;
				this.gameObjects.get(index).y = 100;
		}
	}

	private int getPlayerIndex(int onlineId){
		int index = 0;
			for(GameObject gm: gameObjects){
			if(gm instanceof OnlinePlayer && ((OnlinePlayer)gm).getOnlineId()==onlineId){
				break;
			}
			index++;
		}
		return index;
	}

	public OnlinePlayer getOnlinePlayer(int onlineId) {
		return (OnlinePlayer)gameObjects.get(getPlayerIndex(onlineId));
	}
}