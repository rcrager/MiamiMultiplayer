package game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.id.ObjectID;
import game.id.Weapons;
import game.levels.Level;
import game.main.Controller;
import game.main.window.graphics;
import game.net.packets.Packet;
import game.net.packets.Packet.PacketTypes;
import game.net.packets.Packet00Connect;
import game.net.packets.Packet01Disconnect;
import game.net.packets.Packet02Position;
import game.net.packets.Packet03Rotate;
import game.net.packets.Packet04Image;
import game.net.packets.Packet05Shoot;
import game.net.packets.Packet06Weapon;
import game.net.packets.Packet07InvalidName;
import game.net.packets.Packet10StartGame;
import game.net.packets.Packet11EndGame;
import game.objects.Bullet;
import game.objects.Weapon;
import game.util.classes.GameObject;
import game.util.classes.Sound;

public class Client extends Thread{
	/*
	 * controls the internet aspect of the client
	 * allows the client to receive and send packets
	 * packets work through the big switch statement
	 * below, which just looks for what packettype it
	 * is, then will usually call a method in the 
	 * controller class.
	 */
	private InetAddress ip;
	private DatagramSocket socket;
	private int port;
	private Controller controller;
	
	public Client(Controller controller,String ip, int port){
		try {
			this.ip = InetAddress.getByName(ip);
			this.socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			System.err.println("Cannot connect to host, Check IP and try again.");
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.port = port;
		this.controller = controller;
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
			try {
				//receive data from server.
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(),packet.getAddress(),packet.getPort());
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(Integer.parseInt(msg.substring(0, 2)));
		Packet packet = null;
		
		switch(type){
		default:
			break;
		case INVALID:
			break;
		case CONNECT:
			packet = new Packet00Connect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]"
			+ ((Packet00Connect)packet).getName() + " has joined the game.");
			OnlinePlayer player = new OnlinePlayer(0,0,ObjectID.Player,
					((Packet00Connect)packet).getOnlineId(), ((Packet00Connect)packet).getName(),address,port,controller);
			player.setLocal(false);
			this.controller.addGameObject(player);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			String playerName = controller.getOnlinePlayer(((Packet01Disconnect)packet).getOnlineId()).getName();
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
			+ playerName + " has left the Game.");
			this.controller.removeOnlinePlayer(((Packet01Disconnect)packet).getOnlineId());
			break;
		case POSITION:
			packet = new Packet02Position(data);
			controller.moveOnlinePlayer(((Packet02Position)packet).getOnlineId(),((Packet02Position)packet).getX(),((Packet02Position)packet).getY());
			break;
		case ROTATE:
			packet = new Packet03Rotate(data);
			this.controller.rotateOnlinePlayer(((Packet03Rotate)packet).getOnlineId(), ((Packet03Rotate)packet).getRotation());
			break;
		case IMAGE:
			packet = new Packet04Image(data);
			this.controller.setOnlinePlayerImage(((Packet04Image)packet).getOnlineId(),
					((Packet04Image)packet).getAnimation(),((Packet04Image)packet).getAnimIndex());
			break;
		case INVALIDNAME:
			packet = new Packet07InvalidName(data);
			player = this.controller.getOnlinePlayer(-1);
			if(player!=null){
			((OnlinePlayer)player).setOnlineId(((Packet07InvalidName)packet).getOnlineId());
			}
			break;
		case SHOOT:
			packet = new Packet05Shoot(data);
			player = this.controller.getOnlinePlayer(((Packet05Shoot)packet).getOnlineId());
			if(!player.getLocal()){
				switch(((Packet05Shoot)packet).getWeapon()){
				case Shotgun:
					Bullet bullet0 = new Bullet(((Packet05Shoot)packet).getX(),((Packet05Shoot)packet).getY(),player);
					bullet0.setRotation(player.getRotation()-5);
					this.controller.addGameObject(bullet0);
					Bullet bullet1 = new Bullet(((Packet05Shoot)packet).getX(),((Packet05Shoot)packet).getY(),player);
					bullet1.setRotation(player.getRotation());
					this.controller.addGameObject(bullet1);
					Bullet bullet2 = new Bullet(((Packet05Shoot)packet).getX(),((Packet05Shoot)packet).getY(),player);
					bullet2.setRotation(player.getRotation()+5);
					this.controller.addGameObject(bullet2);
					break;
				default:
					Bullet bullet = new Bullet(((Packet05Shoot)packet).getX(),((Packet05Shoot)packet).getY(),player);
					this.controller.addGameObject(bullet);
					break;
				}
			}
			break;
		case WEAPON:
			packet = new Packet06Weapon(data);
			GameObject removeObj = null;
			String wpn = ((Packet06Weapon)packet).getWeapon();
			player = this.controller.getOnlinePlayer((((Packet06Weapon)packet).getOnlineId()));
			if(!player.getLocal()){
				String action = ((Packet06Weapon)packet).getAction();
				Weapons weapon = Weapons.valueOf(wpn);
				for(int g = 0; g<=this.controller.gameObjects.size()-1; g++){
					GameObject gm = this.controller.gameObjects.get(g);
					if(gm.getId() == ObjectID.Weapon){
						if(player.getRectBounds().intersects(gm.getRectBounds())){
							if(action.equals("pickup")){
//								Sound pickup = new Sound("sounds/sfx/weapon_pickup.wav");
								Sound pickup = Controller.sounds.get(1);

								pickup.setVolume(graphics.VOLUME);
								pickup.playSound();
								removeObj = gm;
							} else if(action.equals("swap")){
								removeObj = gm;
							}
							else{
								System.err.println("No case found for weapon pickup/drop packet.");
							}
						}
					}
				}
				if(action.equals("drop")){
//					Sound drop = new Sound("sounds/sfx/weapon_drop.wav");
					Sound drop = Controller.sounds.get(2);

					drop.setVolume(graphics.VOLUME);
					drop.playSound();
					this.controller.addGameObject(new Weapon(player.getX(),player.getY(),weapon));
				}
				if(action.equals("swap")){
					this.controller.addGameObject(new Weapon(player.getX(),player.getY(),weapon));
				}
				if(removeObj!=null){
					this.controller.removeGameObject(removeObj);
				}
			}
			break;
		case STARTGAME:
			packet = new Packet10StartGame(data);
			player = controller.getOnlinePlayer(((Packet10StartGame)packet).getOnlineId());
			if(!player.isLocal){
				Level nextLvl = new Level(((Packet10StartGame)packet).getImagePath(),false);
				nextLvl.setCollisionPath(((Packet10StartGame)packet).getCollisionPath());
				controller.inLobby = false;
				controller.changeOnlineLevel(nextLvl,((Packet10StartGame)packet).getOnlineId());
			}
			break;
			
		case ENDGAME:
			packet = new Packet11EndGame(data);
			player = controller.getOnlinePlayer(((Packet11EndGame)packet).getOnlineId());
			if(!player.isLocal){
				controller.inLobby = true;
				controller.inGame = false;
			}
			break;
		}
		
	}

	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data,data.length,ip,port);
		try {
			//send data to server.
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
