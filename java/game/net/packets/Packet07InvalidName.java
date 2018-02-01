package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet07InvalidName extends Packet{
	/*
	 * When a client initially connects they send an id of
	 * -1 to the server which it then sends back with the
	 * correct and available onine id so that the client
	 * then creates the player with that onlineid.
	 */

	private int onlineId;
	private String name;
	
	public Packet07InvalidName(byte[] data) {
		super(07);
		String[] msgs = new String(data).trim().split("[|]");
		this.onlineId = Integer.parseInt(msgs[1]);
		this.name = msgs[2];
	}
	
	public Packet07InvalidName(int onlineId, String name) {
		super(07);
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
		return ("07|" + this.onlineId + "|" + this.name).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public String getName(){
		return name;
	}

}
