package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class EnableParentOnClose implements WindowListener {

	JFrame caller;
	
	public EnableParentOnClose(JFrame caller) {
		this.caller = caller;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		this.caller.setEnabled(true);
		this.caller.setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.caller.setEnabled(true);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}