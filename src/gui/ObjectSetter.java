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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean valid;
	private Data data;
	private Data parentData;
	
	private JLabel name;
	private JLabel description;
	private JLabel error;
	private JButton edit;
	private JButton delete;
	
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
	
	private ConfigSetterGUI panel;
	
	private Controller controller;
	
	private JPanel namePanel;
	private JPanel descriptionPanel;
	private JPanel deletePanel;
	
	public ObjectSetter(Data data, Font font, JFrame parent, JSONConfig config, Data parentData, ConfigSetterGUI panel) {
		super();
		this.setBorder(BorderFactory.createTitledBorder(data.getName()));
		this.data  = data;
		this.valid = true;
		this.font = font;
		this.descriptionFont = new Font(font.getName(), Font.ITALIC, font.getSize());
		this.parent = parent;
		this.config = config;
		this.parentData = parentData;
		this.panel = panel;
		this.controller = new Controller(parent,this.config, this.data,this.parentData, this.panel);
		
		constructPanel();
	}
	
	private void constructPanel() {
		
		this.setLayout(new GridLayout(3,1));
		
		
		namePanel = new JPanel();
		
		name = new JLabel("Value: ");
		namePanel.add(name);
		
		if (data.getType() == ETYPE.LIST || data.getType() == ETYPE.OBJECT) {
			edit = new JButton("Edit");
			edit.setActionCommand("EDIT");
			edit.addActionListener(controller);
		} else {
			throw new IllegalArgumentException("Unsupported type " + data.getType().toString());
		}
		
		namePanel.add(edit);
		this.add(namePanel);
		
		descriptionPanel = new JPanel();
		
		description = new JLabel(InsertNewLines(data.getDescription(),font,200));
		description.setFont(descriptionFont);
		descriptionPanel.add(description);
		
		error = new JLabel();
		descriptionPanel.add(error);
		setValid(this.valid);
		
		this.add(descriptionPanel);
		
		error.setHorizontalAlignment(SwingConstants.RIGHT);
		
		deletePanel = new JPanel();
		
		delete = new JButton("Delete");
		delete.setActionCommand("DELETE");
		delete.addActionListener(controller);
		deletePanel.add(delete);
		
		if (!this.data.getDeletable()) {
			delete.setEnabled(false);
		}
		
		this.add(deletePanel);
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
		private Data parentData;
		private ConfigSetterGUI panel;
		
		private Controller(JFrame parent, JSONConfig config, Data data, Data parentData, ConfigSetterGUI panel) {
			this.parent = parent;
			this.config = config;
			this.data   = data;
			this.panel  = panel;
			this.parentData = parentData;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			switch (arg0.getActionCommand()) {
					
			case "DELETE":
				parentData.removeSubData(data.getName());
				this.panel.reload();
				break;
			case "EDIT":
				this.parent.setEnabled(false);
	
				JFrame frame = new JFrame();
				
				ConfigSetterGUI c = new ConfigSetterGUI(this.data, frame, this.parent, this.config);
				frame.setContentPane(c);
				
				frame.setLocation(new Point(this.parent.getLocation())); 
				frame.setSize(550,650);
				frame.setVisible(true);
				frame.addWindowListener(new EnableParentOnClose(this.parent));
				break;
			}
		}
	}
	

	@Override
	public boolean isDataValid() {
		return this.valid;
	}

	@Override
	public Data getData() {
		return this.data;
	} 
}
