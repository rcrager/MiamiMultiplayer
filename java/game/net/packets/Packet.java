package game.net.packets;

import game.net.Client;
import game.net.Server;

public abstract class Packet {
	/*
	 * The core class for all packets.
	 */

	public static enum PacketTypes{
		
		INVALID(-1), CONNECT(00), DISCONNECT(01), 
		POSITION(02), ROTATE(03), IMAGE(04), SHOOT(05), 
		WEAPON(06),INVALIDNAME(07), STARTGAME(10),
		ENDGAME(11);
		
		private int packetId;
		
		private PacketTypes(int packetId){
			this.packetId = packetId;
		}
		
		public int getId(){
			return packetId;
		}
	}
	
	public byte packetId;
	
	public Packet(int packetId){
		this.packetId = (byte) packetId;
	}
	
	public abstract void writeData(Client client);
	public abstract void writeData(Server server);
	
	
	public String readData(byte[] data){
		String msg = new String(data).trim();
		return msg.substring(2);
	}
	
	public abstract byte[] getData();
	
	public static PacketTypes lookupPacket(String packetId){
		try{
		return lookupPacket(Integer.parseInt(packetId));
		} catch(NumberFormatException e){
			return PacketTypes.INVALID;
		}
	}
	
	public static PacketTypes lookupPacket(int id){
		for(PacketTypes type: PacketTypes.values()){
			if(type.getId()==id){
				return type;
			}
		}
		return PacketTypes.INVALID;
	}
}
