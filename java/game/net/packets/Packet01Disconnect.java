package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet01Disconnect extends Packet{
	/*
	 * Takes in the online id of the player
	 * that is disconnecting, so the server
	 * knows which player in the arraylist
	 * to remove.
	 */

	private int onlineId;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		String[] msgs = new String(data).trim().split("[|]");
		this.onlineId = Integer.parseInt(msgs[1]);
	}
	
	public Packet01Disconnect(int onlineId) {
		super(01);
		this.onlineId = onlineId;
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
		return ("01" + "|" + this.onlineId).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}

}
