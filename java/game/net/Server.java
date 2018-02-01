package game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import game.id.ObjectID;
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
import game.util.classes.Sound;

public class Server extends Thread{
	/*
	 * Controls the number of players in the game at once.
	 * Once it receives a connect packet it adds the player
	 * with the id that they send, into an arraylist of 
	 * connected players and vise versa when they disconnect.
	 * The server for the most part on all packets just relays
	 * the information back to all other clients except for 
	 * the one that the packet was sent from.
	 */
	private DatagramSocket socket;
	private int port;
	private Controller controller;
	private List<OnlinePlayer> clients;
	private int nextId = 0;
	
	public Server(Controller controller,int port){
		this.port = port;
		this.controller = controller;
		try {
			this.socket = new DatagramSocket(port);
		}catch (SocketException e) {
			e.printStackTrace();
		}
		clients = new ArrayList<OnlinePlayer>();
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
			try {
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
		case INVALID:
			break;
		case CONNECT:
			packet = new Packet00Connect(data);
			int newId = -1;
			if(nextId+1<=215000000){
				newId = nextId++;
			}else{
				nextId = 1;
			}
			if(((Packet00Connect)packet).getOnlineId()==-1){
				Packet00Connect conPack = new Packet00Connect(newId,((Packet00Connect)packet).getName());
				//if not assigned id... assign it.
				System.out.println("[" + address.getHostAddress() + ":" + port + "] "
				+ ((Packet00Connect)conPack).getName() + " has connected.");
				OnlinePlayer player = new OnlinePlayer(0,0,ObjectID.Player,newId,
						((Packet00Connect)conPack).getName(),address,port,controller);
				//add player connection after they receive the invalid name packet back
				Packet07InvalidName sendIdPack = new Packet07InvalidName(newId,((Packet00Connect)packet).getName());
				sendData(sendIdPack.getData(),player.ip,player.port);
				this.addConnection(player,(Packet00Connect)conPack);
			}
			break;
		case INVALIDNAME:
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			String playerName = getOnlinePlayer(((Packet01Disconnect)packet).getOnlineId()).getName();
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
			+ playerName + " has left.");
			this.removeConnection((Packet01Disconnect)packet);
			break;
		case POSITION:
			packet = new Packet02Position(data);
			handlePosition((Packet02Position)packet);
			break;
		case ROTATE:
			packet = new Packet03Rotate(data);
			handleRotation((Packet03Rotate)packet);
			break;
		case IMAGE:
			packet = new Packet04Image(data);
			handleImage((Packet04Image)packet);
			break;
		case SHOOT:
			packet = new Packet05Shoot(data);
			if(((Packet05Shoot)packet).getOnlineId()!=-1){
				packet.writeData(this);
			}
			break;
		case WEAPON:
			packet = new Packet06Weapon(data);
			handleWeapon((Packet06Weapon)packet);
			break;
		case STARTGAME:
			packet = new Packet10StartGame(data);
			if(((Packet10StartGame)packet).getOnlineId()!=-1){
				packet.writeData(this);
			}
			break;
		case ENDGAME:
			packet = new Packet11EndGame(data);
			if(((Packet11EndGame)packet).getOnlineId()!=-1){
				packet.writeData(this);
			}
			break;
		}
	}
	
	

	public void addConnection(OnlinePlayer player, Packet00Connect packet) {
		boolean alreadyConnected = false;
		for(OnlinePlayer p: clients){
			if(player.getOnlineId()==p.getOnlineId()){
				if(!p.isHost()){
				if(p.ip == null){
					p.ip = player.ip;
				}
				if(p.port == -1){
					p.port = player.port;
				}
				alreadyConnected = true;
				}
			}else{
				sendData(packet.getData(),p.ip,p.port);
				
				Packet00Connect conPack = new Packet00Connect(p.getOnlineId(),p.getName());
				sendData(conPack.getData(),player.ip,player.port);
			}
		}
		if(!alreadyConnected){
			this.clients.add(player);
		}else{
			System.err.println("Player joining already in Server!");
		}
	}
	
	private void removeConnection(Packet01Disconnect packet) {
		this.clients.remove(getPlayerIndex(packet.getOnlineId()));
		packet.writeData(this);
	}
	
	private void handlePosition(Packet02Position packet) {
		if(getOnlinePlayer(packet.getOnlineId())!=null){
			int index = getPlayerIndex(packet.getOnlineId());
			if(!clients.get(index).isLocal){
				clients.get(index).x = packet.getX();
				clients.get(index).y = packet.getY();
			}
			packet.writeData(this);
		}
	}
	
	private void handleRotation(Packet03Rotate packet) {
		if(getOnlinePlayer(packet.getOnlineId())!=null){
			int index = getPlayerIndex(packet.getOnlineId());
			if(!clients.get(index).isLocal){
				clients.get(index).setRotation(packet.getRotation());
			}
			packet.writeData(this);
		}
	}
	
	private void handleImage(Packet04Image packet) {
		if(getOnlinePlayer(packet.getOnlineId())!=null){
			packet.writeData(this);
		}
	}
	
	private void handleWeapon(Packet06Weapon packet){
		String action = ((Packet06Weapon)packet).getAction();
		if(action.equals("drop")){
//			Sound drop = new Sound("sounds/sfx/weapon_drop.wav");
			Sound drop = Controller.sounds.get(2);

			drop.setVolume(graphics.VOLUME);
			drop.playSound();
		}else if(action.equals("pickup")){
//			Sound pickup = new Sound("sounds/sfx/weapon_pickup.wav");
			Sound pickup = Controller.sounds.get(1);

			pickup.setVolume(graphics.VOLUME);
			pickup.playSound();
		}
		if(getOnlinePlayer(packet.getOnlineId())!=null){
			packet.writeData(this);
		}
	}

	public OnlinePlayer getOnlinePlayer(int onlineId){
		for(OnlinePlayer player: this.clients){
			if(player.getOnlineId() == onlineId){
				return player;
			}
		}
		return null;
	}
	
	public int getPlayerIndex(int onlineId){
		int index = 0;
		for(OnlinePlayer player: this.clients){
			if(player.getOnlineId() == onlineId){
				break;
			}
			index++;
		}
		return index;
	}
	
	

	public void sendDataToAllClients(byte[] data) {
		for(OnlinePlayer player: clients){
			sendData(data, player.ip, player.port);
		}
	}
	
	public void sendData(byte[] data, InetAddress ip, int port){
		DatagramPacket packet = new DatagramPacket(data,data.length,ip,port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
