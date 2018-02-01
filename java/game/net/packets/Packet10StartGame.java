package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet10StartGame extends Packet{
	/*
	 * Takes in the level path for the img
	 * and collision files. This is needed 
	 * for when the host pressed start game
	 * the controller of each client then
	 * knows that they're in game now and
	 * to start the game timer.
	 */

	private int onlineId;
	private String imagePath;
	private String collisionPath;
	
	public Packet10StartGame(byte[] data) {
		super(10);
		String[] msgs = new String(data).trim().split("[|]");
		this.onlineId = Integer.parseInt(msgs[1]);
		this.imagePath = msgs[2];
		this.collisionPath = msgs[3];
	}
	
	public Packet10StartGame(int onlineId, String imagePath, String collisionPath) {
		super(10);
		this.onlineId = onlineId;
		this.imagePath = imagePath;
		this.collisionPath = collisionPath;
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
		return ("10" + "|" + this.onlineId + "|" + imagePath + "|" + collisionPath).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public String getImagePath(){
		return imagePath;
	}
	
	public String getCollisionPath(){
		return collisionPath;
	}

}
