package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet03Rotate extends Packet{
	/*
	 * takes in the rotation of the player
	 * then in controller class it sets the 
	 * rotation of the player with that id
	 * to the rotation it just received.
	 */

	private int rot;
	private int onlineId;
	
	public Packet03Rotate(byte[] data){
		super(03);
		String[] rotMsgs = new String(data).trim().split("[|]");
		//pos 0 in array = type of message
		int onlineId = Integer.parseInt(rotMsgs[1]);
		int rot = Integer.parseInt(rotMsgs[2]);
		this.onlineId = onlineId;
		this.rot = rot;
	}
	
	public Packet03Rotate(int onlineId,int rot) {
		super(03);
		this.onlineId = onlineId;
		this.rot = rot;
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
		return ("03|" + this.onlineId + "|" + this.rot).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public int getRotation(){
		return rot;
	}
}
