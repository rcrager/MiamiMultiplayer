package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet02Position extends Packet{
	/*
	 * Takes in their online id, x & y
	 * positions. Then when other clients
	 * receive it, the controller class sets
	 * the x&y of the player with that id to 
	 * the x&y that it just received.
	 */

	private int x,y;
	private int onlineId;
	
	public Packet02Position(byte[] data){
		super(02);
		String[] posMsgs = new String(data).trim().split("[|]");
		//pos 0 in array = type of message
		int onlineId = Integer.parseInt(posMsgs[1]);
		String[] posString = posMsgs[2].split(",");
		int xPos = Integer.parseInt(posString[0]);
		int yPos = Integer.parseInt(posString[1]);
		this.onlineId = onlineId;
		this.x = xPos;
		this.y = yPos;
	}
	
	public Packet02Position(int onlineId,int x, int y) {
		super(02);
		this.onlineId = onlineId;
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02|" + this.onlineId + "|" + this.x + "," + this.y).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

}
