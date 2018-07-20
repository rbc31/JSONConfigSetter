package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class EnableParentOnCloseAndReload implements WindowListener {

	JFrame caller;
	ConfigSetterGUI panel;
	
	public EnableParentOnCloseAndReload(JFrame caller, ConfigSetterGUI panel) {
		this.caller = caller;
		this.panel = panel;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		this.panel.reload();
		this.caller.setEnabled(true);
		this.caller.setVisible(true);
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.panel.reload();
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