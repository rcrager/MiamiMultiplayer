package game.net.packets;

import game.id.Weapons;
import game.net.Client;
import game.net.Server;

public class Packet05Shoot extends Packet{
	/*
	 * Takes in player x&y and weapon(as string)
	 * then it calls a method in controller, which
	 * creates a bullet at the x&y(possibly an offset
	 * depending on weapon), the weapon also decides
	 * how many bullets the controller creates, for
	 * instance when a shotgun is used 3 bullets spawn.
	 */

	private int onlineId;
	private int x,y;
	private String weapon;
	
	public Packet05Shoot(byte[] data){
		super(05);
		String[] bulMsgs = new String(data).trim().split("[|]");
		//pos 0 in array = type of message
		int onlineId = Integer.parseInt(bulMsgs[1]);
		String[] posMsg = new String(bulMsgs[2]).trim().split(",");
		this.x = Integer.parseInt(posMsg[0]);
		this.y = Integer.parseInt(posMsg[1]);
		this.onlineId = onlineId;
		this.weapon = bulMsgs[3];
	}
	
	public Packet05Shoot(int onlineId, int x, int y, String weapon) {
		super(05);
		this.onlineId = onlineId;
		this.x = x;
		this.y = y;
		this.weapon = weapon;
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
		return ("05|" + this.onlineId + "|" + this.x + "," + this.y + "|" + weapon).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public Weapons getWeapon(){
		return Weapons.valueOf(weapon);
	}
}
