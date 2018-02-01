package game.util.classes;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import game.main.Controller;
import game.net.OnlinePlayer;
import game.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener{
	/*
	 * This is literally only here so when the user closes
	 * their client it sends a disconnect packet to the
	 * server so everything can stay synced.
	 */
	
	private final JFrame frame;
	private final Controller controller;
	
	public WindowHandler(JFrame frame,Controller controller){
		this.frame = frame;
		this.frame.addWindowListener(this);
		this.controller = controller;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if(!controller.inMenu){
			Packet01Disconnect packet = new Packet01Disconnect(((OnlinePlayer)this.controller.player).getOnlineId());
			packet.writeData(this.controller.client);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

}
