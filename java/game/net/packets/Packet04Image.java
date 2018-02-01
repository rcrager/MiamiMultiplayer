package game.net.packets;

import game.net.Client;
import game.net.Server;

public class Packet04Image extends Packet{
	/*
	 * Takes in an animation(class) and animation index(int)
	 * it then calls a method in the controller class that
	 * gets the animation that it sent(sent it as a string)
	 * and sets the current animation to that anim, then sets
	 * the current index of the anim to that index it received.
	 */

	private int onlineId;
	private String animation;
	private int animIndex;
	
	public Packet04Image(byte[] data){
		super(04);
		String[] imgMsgs = new String(data).trim().split("[|]");
		//pos 0 in array = type of message
		int onlineId = Integer.parseInt(imgMsgs[1]);
		animation = imgMsgs[2];
		animIndex = Integer.parseInt(imgMsgs[3]);
		this.onlineId = onlineId;
	}
	
	public Packet04Image(int onlineId,String animName, int animIndex) {
		super(04);
		this.onlineId = onlineId;
		this.animation = animName;
		this.animIndex = animIndex;
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
		return ("04|" + this.onlineId + "|" + this.animation + "|" + this.animIndex).getBytes();
	}
	
	public int getOnlineId(){
		return onlineId;
	}
	
	public String getAnimation(){
		return animation;
	}
	
	public int getAnimIndex(){
		return animIndex;
	}
}
