package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import config.Data;
import config.ETYPE;

public class Setter extends JPanel implements ISetter {

	
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
	
	private JLabel name;
	private JLabel description;
	private JLabel error;
	private JTextField value;
	
	private Font font;
	private Font descriptionFont;
	
	private JButton delete;
	
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
	
	
	private Data data;
	private Data parentData;
	
	private ConfigSetterGUI parentPanel;
	
	private JPanel valuePanel;
	private JPanel descriptionPanel;
	private JPanel deletePanel;
	
	public Setter(Data data, Font font, Data parentData, ConfigSetterGUI parentPanel) {
		super();
		this.setBorder(BorderFactory.createTitledBorder(data.getName()));
		this.data  = data;
		this.valid = true;
		this.font = font;
		this.descriptionFont = new Font(font.getName(), Font.ITALIC, font.getSize());
		this.parentData = parentData;
		this.parentPanel = parentPanel;
		
		constructPanel();
	}
	
	private void constructPanel() {
		this.setLayout(new GridLayout(3,1));
		
		valuePanel = new JPanel(new GridLayout(1,2));
		
		name = new JLabel("Value: ");
		valuePanel.add(name);
		
		if (data.getType() == ETYPE.INTEGER) {
			value = new JTextField((String.valueOf((Integer) data.getData())));
		}else if (data.getType() == ETYPE.DOUBLE) {
			value = new JTextField((String.valueOf((Double) data.getData()))); 
		}else if (data.getType() == ETYPE.STRING) {
			value = new JTextField((String) data.getData()); 
		}else {
			throw new IllegalArgumentException("Unsupported type " + data.getType().toString());
		}
		
		value.setHorizontalAlignment(JTextField.RIGHT);
		valuePanel.add(value);
		value.getDocument().addDocumentListener(new DocumentListner(this,this.data));
		this.add(valuePanel);
		
		descriptionPanel = new JPanel(new GridLayout(1,2));
		
		description = new JLabel(InsertNewLines(data.getDescription(),font,200));
		description.setFont(descriptionFont);
		descriptionPanel.add(description);
		
		error = new JLabel();
		descriptionPanel.add(error);
		this.add(descriptionPanel);
		
		setValid(this.valid);
		error.setHorizontalAlignment(SwingConstants.RIGHT);
		
		deletePanel = new JPanel();
		
		delete = new JButton("Delete");
		delete.setActionCommand("DELETE");
		delete.addActionListener(new MyActionListener(this.parentPanel, this.parentData, this.data));
		deletePanel.add(delete);
		this.add(deletePanel);
		
		if (!this.data.getDeletable()) {
			delete.setEnabled(false);
		}
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
	
	private class DocumentListner implements DocumentListener {

		Data data;
		Setter parent;
		
		private DocumentListner(Setter parent, Data data) {
			this.parent = parent;
			this.data = data;
		}
		
		private boolean validate(DocumentEvent evt) {
			try {
				String text = evt.getDocument().getText(0, evt.getDocument().getLength());
				
				if (this.data.getType() == ETYPE.INTEGER) {
					Integer value = Integer.valueOf(text);
				
					this.data.setData(value);
					return true;
				}else if (this.data.getType() == ETYPE.DOUBLE) {
					Double value = Double.valueOf(text);
					
					this.data.setData(value);
					return true;
				}else if (this.data.getType() == ETYPE.STRING) {
					
					this.data.setData(text);
					return true;
				}
				return false;
			}catch (Exception e) {
				return false;
			}
		}
		
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			parent.setValid(validate(arg0));
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			parent.setValid(validate(arg0));
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			parent.setValid(validate(arg0));
		}
		
	}

	private class MyActionListener implements ActionListener {

		private ConfigSetterGUI panel;
		private Data parentData;
		private Data data;
		
		public MyActionListener(ConfigSetterGUI panel, Data parentData, Data data) {
			this.panel = panel;
			this.parentData = parentData;
			this.data = data;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			switch (arg0.getActionCommand()) {
			
			case "DELETE":
					//TODO add check if you are sure???
					parentData.removeSubData(data.getName());
					panel.reload();
					break;
			}
		}
		
	}
}
