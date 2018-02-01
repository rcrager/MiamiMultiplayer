package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet06Weapon extends Packet{
	/*
	 * Takes in an int action and weapon as a string
	 * which then depending on the action(-1,0, or 1)
	 * it will pickup,swap, or add weapon to the 
	 * weapon pool in the controller class.
	 */

	private int onlineId;
	private int action;
	private String weapon;
	
	public Packet06Weapon(byte[] data){
		super(06);
		String[] wpnMsgs = new String(data).trim().split("[|]");
		//pos 0 in array = type of message
		this.onlineId = Integer.parseInt(wpnMsgs[1]);
		this.weapon = wpnMsgs[2];
		this.action = Integer.parseInt(wpnMsgs[3]);
	}
	
	public Packet06Weapon(int onlineId, String weapon, int action) {
		super(06);
		this.onlineId = onlineId;
		this.weapon = weapon;
		this.action = action;
	}
	
	@Override
	public void writeData(Client client) {
//		if(client!=null){
			client.sendData(getData());
//		}else{
//			System.out.println("client = null");
//		}
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("06|" + this.onlineId + "|" + this.weapon + "|" + this.action).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public String getWeapon(){
		return weapon;
	}
	
	public int getActionNum(){
		return action;
	}
	public String getAction(){
		if(action>=1){
			return "drop";
		}else if(action<=-1){
			return "pickup";
		}else if(action==0){
			return "swap";
		}else{
			return "No Action.";
		}
	}
}
