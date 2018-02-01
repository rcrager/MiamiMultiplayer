package game.net;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.List;

import game.id.ObjectID;
import game.id.Weapons;
import game.main.Controller;
import game.main.Game;
import game.net.packets.Packet02Position;
import game.net.packets.Packet03Rotate;
import game.net.packets.Packet04Image;
import game.net.packets.Packet05Shoot;
import game.net.packets.Packet06Weapon;
import game.objects.Player;
import game.objects.Weapon;
import game.util.classes.GameObject;

public class OnlinePlayer extends Player{
	/*
	 * the online version of player...
	 * every player is an onlineplayer even
	 * the clientside instance of the game.
	 * has many methods for sending certain
	 * information to the server.
	 */
	
	public InetAddress ip;
	public int port;
	boolean isLocal;
	boolean isHost;
	String name;
	int onlineId;
	Client client;
	Controller controller;
	boolean visible = true;
	
	private int lastX,lastY;
	
	public OnlinePlayer(int x, int y, ObjectID id, InetAddress ip, int port,Controller controller){
		super(x,y,id);
		this.ip = ip;
		this.port = port;
		this.controller = controller;
		setName(id.toString());
	}
	public OnlinePlayer(int x, int y, ObjectID id, int onlineId, String username, InetAddress ip, int port, Controller controller){
		super(x,y,id);
		this.ip = ip;
		this.port = port;
		this.controller = controller;
		this.onlineId = onlineId;
		setName(username);
	}
	public OnlinePlayer(Client client,int x, int y, ObjectID id, int onlineId, String username, InetAddress ip, int port,Controller controller){
		super(x,y,id);
		this.client = client;
		this.ip = ip;
		this.port = port;
		this.controller = controller;
		this.onlineId = onlineId;
		setName(username);
	}
	
	public void sendPos(){
		Packet02Position posPack = new Packet02Position(this.getOnlineId(),this.x,this.y);
		posPack.writeData(client);
	}
	private void sendRot(){
		Packet03Rotate rotPack = new Packet03Rotate(this.getOnlineId(),(int)getRotation());
		rotPack.writeData(client);
	}
	private void sendImage(){
		if(currentAnim!=null){
		Packet04Image imgPack = new Packet04Image(this.getOnlineId(),currentAnim.getName(),currentAnim.getImageIndex());
		imgPack.writeData(client);
		}
	}
	private void sendShoot(int bulX, int bulY){
		Packet05Shoot shotPack = new Packet05Shoot(this.getOnlineId(),bulX,bulY,weapon.toString());
		shotPack.writeData(client);
	}
	@Override
	protected void sendWeaponAction(int action){
		//send weapon pickup/drop packet
		Packet06Weapon wpnPack = new Packet06Weapon(this.getOnlineId(),this.weapon.toString(),action);
		wpnPack.writeData(client);
	}
	
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public Weapons getWeapon(){
		return this.weapon;
	}
	
	public void setOnlineId(int onlineId){
		this.onlineId = onlineId;
	}
	
	public boolean isHost(){
		return isHost;
	}
	
	public void setHost(boolean isHost){
		this.isHost = isHost;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	@Override
	public void mouseInput(MouseEvent e){
		if(isLocal){
			super.mouseInput(e);
		}
	}
	
	@Override
	public void keyInput(KeyEvent e){
		if(isLocal){
		super.keyInput(e);
		}
	}
	
	@Override
	public void update(List<GameObject> objects){
		//add x & y from last frame
		if(visible){
			if(health<=0){
				if(weapon!=Weapons.Fist){
					sendWeaponAction(1);
					this.controller.addGameObject(new Weapon(getX(),getY(),weapon));
				}
				respawn();
				health = 100;
				weapon = Weapons.Fist;
			}
			super.update(objects);
			if(lastX!=x && getLocal() || lastY!=y && getLocal()){
				sendPos();
			}
			lastX = x;
			lastY = y;
		}
	}
	
	@Override
	protected void rotateToMouse(int centerX,int centerY){
		if(isLocal){
			sendRot();
			super.rotateToMouse(centerX, centerY);
		}
	}
	
	@Override
	protected void checkMouseInput(List<GameObject> checker) {
		if(isLocal){
			super.checkMouseInput(checker);
		}
	}
	
	@Override
	public void shoot(int startx, int starty){
		super.shoot(startx, starty);
		sendShoot(startx,starty);
	}
	
	@Override
	protected void updateAnimations(){
		if(this.isLocal){
			sendImage();
			super.updateAnimations();
		}
	}
	
	
	@Override
	public void render(Graphics g){
		if(visible){
			Graphics2D g2d = (Graphics2D)g;
			String nameandHealth = this.getName() + " " + this.health;
			g2d.setColor(Color.white);
			g2d.drawString(nameandHealth,x-(Game.SCALE*10),y-(getImage().getHeight()/2 + 10*Game.SCALE));
			super.render(g);
		}
	}
	
	public boolean inLobby(){
		return controller.inLobby;
	}
	
	public boolean inGame(){
		return controller.inGame;
	}
	
	public boolean inMenu(){
		return controller.inMenu;
	}
	
	@Override
	protected void collision(GameObject col){
		if(isLocal){
			super.collision(col);
		}
	}
	
	public void respawn(){
		controller.respawnOnlinePlayer(this.onlineId);
	}
	
	public void setLocal(boolean local){
		this.isLocal = local;
	}
	
	public boolean getLocal(){
		return isLocal;
	}
}
