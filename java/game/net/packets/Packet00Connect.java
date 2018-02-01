package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet00Connect extends Packet{
	/*
	 * Takes in the clients id for
	 * identification and their name
	 * for displaying it to other players.
	 */

	private String name;
	private int onlineId;
	
	public Packet00Connect(byte[] data) {
		super(00);
		String[] msgs = new String(data).trim().split("[|]");
		this.onlineId = Integer.parseInt(msgs[1]);
		this.name = msgs[2];
	}
	
	public Packet00Connect(int onlineId,String name) {
		super(00);
		this.onlineId = onlineId;
		this.name = name;
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
		return ("00" + "|" + this.onlineId + "|" + this.name).getBytes();
	}
	
	public String getName(){
		return name;
	}
	
	public int getOnlineId(){
		return onlineId;
	}

}
