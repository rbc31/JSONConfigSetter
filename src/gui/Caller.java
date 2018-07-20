package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;

import Exceptions.ConfigNotValidException;
import Exceptions.InvalidTypeException;
import config.JSONConfig;

public class Caller extends JPanel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Caller(JFrame master) {
		
		JButton btn = new JButton("configer");
		btn.addActionListener(new MyActionListner2(master));
		
		this.add(btn);

		
	}
	private class MyActionListner2 implements ActionListener {
		
		JFrame caller;
		
		private MyActionListner2(JFrame caller) {
			this.caller = caller;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			this.caller.setEnabled(false);
			try {
				JFrame frame = new JFrame();
				frame.setLocation(new Point(this.caller.getLocation())); 
				
				JSONConfig config = new JSONConfig("config.json");
				ConfigSetterGUI c = new ConfigSetterGUI(config.getTopLevelData(),frame, this.caller,config, true);
				frame.setContentPane(c);
				frame.setVisible(true);
				
				frame.setSize(500,650);
			} catch (JSONException | IOException | InvalidTypeException | ConfigNotValidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	
	public static void main(String[] args) throws FileNotFoundException, JSONException, IOException, InvalidTypeException, ConfigNotValidException {
		JFrame frame = new JFrame();
		
		frame.setContentPane(new Caller(frame));
		frame.setVisible(true);
		frame.pack();
		frame.setSize(500,650);
	}

}
