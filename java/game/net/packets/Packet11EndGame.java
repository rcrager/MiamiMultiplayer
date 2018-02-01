package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet11EndGame extends Packet{
	/*
	 * Takes in the online id of the host
	 * but it doesn't really need to.
	 * This is just a send signal to all
	 * clients saying that the game is over
	 * or has been ended by the host.
	 */

	private int onlineId;
	
	public Packet11EndGame(byte[] data) {
		super(11);
		String[] msgs = new String(data).trim().split("[|]");
		this.onlineId = Integer.parseInt(msgs[1]);
	}
	
	public Packet11EndGame(int onlineId) {
		super(11);
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
		return ("11" + "|" + this.onlineId).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
}
