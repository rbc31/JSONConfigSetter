package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import config.Data;
import config.ETYPE;
import config.JSONConfig;

public class ObjectSetter extends JPanel implements ISetter {

	
	@Override
	public boolean isDataValid() {
		return this.valid;
	} 
	
	
	@Override
	public Data getData() {
		return this.data;
	} 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean valid;
	private Data data;
	
	private JLabel name;
	private JLabel description;
	private JLabel error;
	private JButton edit;
	
	private Font font;
	private Font descriptionFont;
	
	private JFrame parent;
	private JSONConfig config;
	
	private String InsertNewLines(String text, Font font, int size) {
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     

		String toReturn = "<HTML>"; 
		ArrayList<String> words = new ArrayList<String>();
		for (String word : text.split(" ")) {
			words.add(word);
		}
		
		while (!words.isEmpty()) {
			
			String temp = "";
			String curr = temp;
			
			while (font.getStringBounds(temp, frc).getWidth() <= size && !words.isEmpty()) {
				temp += " " + words.remove(0);
				 curr = temp;
			}
			
			toReturn += curr + "<BR></BR>";
			
		}
		
		toReturn += "</HTML>";
		return toReturn;
	}
	
	public ObjectSetter(Data data, Font font, JFrame parent, JSONConfig config) {
		super();
		this.setBorder(BorderFactory.createTitledBorder(data.getName()));
		this.data  = data;
		this.valid = true;
		this.font = font;
		this.descriptionFont = new Font(font.getName(), Font.ITALIC, font.getSize());
		this.parent = parent;
		this.config = config;
		
		constructPanel();
	}
	
	private void constructPanel() {
		this.setLayout(new GridLayout(2,2));
		
		name = new JLabel("Value: ");
		this.add(name);
		
		if (data.getType() == ETYPE.OBJECT) {
			edit = new JButton("Edit");
			edit.setActionCommand("EDIT");
			edit.addActionListener(new Controller(parent,this.config, this.data));
		}else {
			throw new IllegalArgumentException("Unsupported type " + data.getType().toString());
		}
		
		this.add(edit);
		
		description = new JLabel(InsertNewLines(data.getDescription(),font,200));
		description.setFont(descriptionFont);
		this.add(description);
		
		error = new JLabel();
		this.add(error);
		setValid(this.valid);
		error.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	private void setValid(boolean valid) {
		this.valid = valid;
		if (this.valid) {
			error.setText("valid");
			error.setFont(descriptionFont);
			error.setForeground(new Color(0,153,0));
		}else {
			error.setText("invalid");
			error.setFont(descriptionFont);
			error.setForeground(Color.RED);
		}
	}
	
	private class Controller implements ActionListener {

		private JFrame parent;
		private JSONConfig config;
		private Data data;
		
		private Controller(JFrame parent, JSONConfig config, Data data) {
			this.parent = parent;
			this.config = config;
			this.data   = data;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			this.parent.setEnabled(false);

			JFrame frame = new JFrame();

			
			ConfigSetterGUI c = new ConfigSetterGUI(data, frame, this.parent, this.config);
			frame.setContentPane(c);
			
			frame.setLocation(new Point(this.parent.getLocation())); 
			frame.setSize(500,650);
			frame.setVisible(true);
			frame.addWindowListener(new EnableParentOnClose(this.parent));
		}
	}
}
