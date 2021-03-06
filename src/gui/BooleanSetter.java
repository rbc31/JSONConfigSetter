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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import config.Data;
import config.ETYPE;

public class BooleanSetter extends JPanel implements ISetter {

	
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
	private JCheckBox value;
	
	private Font font;
	private Font descriptionFont;
	
	
	private JPanel valuePanel;
	private JPanel descriptionPanel;
	private JPanel deletePanel;
	
	private Data parentData;
	private ConfigSetterGUI parentPanel;
	
	public BooleanSetter(Data data, Font font, Data parentData, ConfigSetterGUI parentPanel) {
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
		
		if (data.getType() == ETYPE.BOOLEAN) {
			value = new JCheckBox();
			value.setSelected((Boolean) this.data.getData());
		}else {
			throw new IllegalArgumentException("Unsupported type " + data.getType().toString());
		}
		
		if (!this.data.getSettable()) {
			value.setEnabled(false);
			value.setToolTipText("Can not set this value");
		}
		
		value.setHorizontalAlignment(JTextField.RIGHT);
		valuePanel.add(value);
		value.addChangeListener(new MyChangeListener(this,this.data));
		
		this.add(valuePanel);
		
		
		descriptionPanel = new JPanel(new GridLayout(1,2));
		description = new JLabel(ISetter.insertNewLines(data.getDescription(),font,200));
		description.setFont(descriptionFont);
		descriptionPanel.add(description);
		
		error = new JLabel();
		descriptionPanel.add(error);
		setValid(this.valid);
		error.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(descriptionPanel);
		
		this.deletePanel = new JPanel();
		JButton delete = new JButton("Delete");
		delete.addActionListener(new MyActionListener(this.parentData, this.parentPanel));
		delete.setActionCommand("DELETE");
		this.deletePanel.add(delete);
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
	
	private class MyChangeListener implements ChangeListener {

		Data data;
		BooleanSetter parent;
		
		private MyChangeListener(BooleanSetter parent, Data data) {
			this.parent = parent;
			this.data = data;
		}
		
		private boolean validate(ChangeEvent evt) {
			try {
				this.data.setData(((JCheckBox) evt.getSource()).isSelected());
				return true;
			}catch (Exception e) {
				return false;
			}
		}
		
		@Override
		public void stateChanged(ChangeEvent arg0) {
			parent.setValid(validate(arg0));
		}
		
	}

	private class MyActionListener implements ActionListener {

		private Data parentData;
		private ConfigSetterGUI panel;
		
		public MyActionListener(Data parentData, ConfigSetterGUI panel) {
			this.parentData = parentData;
			this.panel = panel;
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
