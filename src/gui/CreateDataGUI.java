package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import config.Data;
import config.ETYPE;

public class CreateDataGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame parent;
	
	private JLabel type_lbl;
	private JLabel name_lbl;
	private JLabel description_lbl;
	private JLabel value_lbl;
	
	private JComboBox<ETYPE> type_cbo;
	private JTextField name_txt;
	private JTextArea description_ta;
	private JTextField value_txt;
	
	private JButton add_btn;
	private JButton cancel_btn;

	private Controller controller;
	
	private Data parentData;
	private ConfigSetterGUI parentGUI;
	 
	 
	public CreateDataGUI(JFrame parent, Data parentData, ConfigSetterGUI parentGUI) {
		super();
		this.parentGUI = parentGUI;
		this.parent = parent;
		this.parentData = parentData;
		constuctFrame();
	}

	private void constuctFrame() {
		this.setLayout(new GridLayout(5,2));
		type_lbl = new JLabel("Type:");
		this.add(type_lbl);
		this.controller = new Controller(this.parent, this, this.parentData, this.parentGUI);
		
		
		type_cbo = new JComboBox<ETYPE>();
		type_cbo.addItem(ETYPE.BOOLEAN);
		type_cbo.addItem(ETYPE.INTEGER);
		type_cbo.addItem(ETYPE.DOUBLE);
		type_cbo.addItem(ETYPE.STRING);
		type_cbo.addItem(ETYPE.LIST);
		type_cbo.addItem(ETYPE.OBJECT);
		this.add(type_cbo);
		
		name_lbl = new JLabel("Name:");
		this.add(name_lbl);
		
		name_txt = new JTextField();
		name_txt.setColumns(65);
		this.add(name_txt);
		
		description_lbl = new JLabel("Description:");
		this.add(description_lbl);
		
		description_ta = new JTextArea();
		this.add(description_ta);
		
		value_lbl = new JLabel("Value:");
		this.add(value_lbl);
		value_lbl.setEnabled(false);
		
		value_txt = new JTextField();
		this.add(value_txt);
		value_txt.setEnabled(false);
		
		cancel_btn = new JButton("Cancel");
		cancel_btn.addActionListener(controller);
		cancel_btn.setActionCommand("CANCEL");
		this.add(cancel_btn);
		
		add_btn = new JButton("Add");
		add_btn.addActionListener(controller);
		add_btn.setActionCommand("ADD");
		this.add(add_btn);
	}
	
	public String dataIsValid() {
		String errorMsg = null;
		if (this.name_txt.getText().equals("")) {
			errorMsg = "Name must not be empty";
		}else if (this.description_lbl.getText().equals("")) {
			errorMsg = "Description must not be empty";
		}
		
		return errorMsg;
	}
	
	public Data getData() throws ConfigNotValidException, CustomValidationException {
		
		Object value = null;
		
		switch ((ETYPE)this.type_cbo.getSelectedItem()) {
		
			case BOOLEAN:
					value = false;
					break;
			case INTEGER:
					value = 0;
					break;
			case DOUBLE:
					value = 0.0;
					break;
			case STRING:
					value = "";
					break;
			case LIST:
					value = new ArrayList<Data>();
					break;
			case OBJECT:
					value = new HashMap<String,Data>();
					break;
		}
		
	
		return new Data(this.name_txt.getText(),this.description_ta.getText(), value, true, true, true);

	}
	
	private class Controller implements ActionListener {
		
		private JFrame parent;
		private CreateDataGUI panel;
		private Data parentData;
		private ConfigSetterGUI parentGUI;
		
		public Controller(JFrame parent, CreateDataGUI panel, Data parentData, ConfigSetterGUI parentGUI) {
			this.parent = parent;
			this.panel = panel;
			this.parentData = parentData;
			this.parentGUI = parentGUI;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			
			case "ADD":
				String error = this.panel.dataIsValid();
				
				if (error != null) {
					JOptionPane.showMessageDialog(this.parent,
						    "Invalid data: " + error,
						    "validation error",
						    JOptionPane.ERROR_MESSAGE);
				}else {

					try {
						this.parentData.addData(this.panel.getData());
					} catch (ConfigNotValidException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CustomValidationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.parent.dispose();
					parentGUI.reload();
				}
				
				break;
			case "CANCEL":
				this.parent.dispose();
				parentGUI.reload();
			}
		}
	}
}
