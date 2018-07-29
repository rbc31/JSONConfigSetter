package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.JSONException;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.Data;
import config.ETYPE;
import config.JSONConfig;

public class ConfigSetterGUI extends JPanel {

	private static final long serialVersionUID = 1L;

	private JSONConfig config;
	//private Data array;
	
	private JButton save;
	private JButton cancel;
	private JButton add;
	
	private JPanel savePnl;
	private JPanel scrollPanel;
	private JPanel addPnl;
	
	private JFrame parent;
	private JFrame caller;
	
	private ArrayList<ISetter> setters;
	
	private MyActionListener myActionListener;
	
	private Data data;
	private boolean saveOnExit;
	
	private void addComponent(Data data, GridBagConstraints cb) {
		ISetter temp;
		switch (data.getType()) {
		
			case BOOLEAN:
				temp = new BooleanSetter(data, getFont(), this.data, this);
				setters.add(temp);
				scrollPanel.add((Component)temp, cb);
				break;
			case INTEGER:
			case DOUBLE:
			case STRING:
				temp = new Setter(data, getFont(), this.data, this);
				setters.add(temp);
				scrollPanel.add((Component)temp, cb);
				break;
			case LIST:
				temp = new ObjectSetter(data, getFont(), this.parent, this.config, this.data, this);
				setters.add(temp);
				scrollPanel.add((Component)temp, cb);
				break;
			case OBJECT:
				temp = new ObjectSetter(data, getFont(), this.parent, this.config, this.data, this);
				setters.add(temp);
				scrollPanel.add((Component) temp, cb);
				break;
			default:
				//error
			}
	}
	
	private void addCanelSaveAndAddButtons(boolean commitInsteadOfSave, GridBagConstraints cb) {
		
		myActionListener = new MyActionListener(this.config, this.parent, this.caller, this.setters, this.data, this);
		
		addPnl = new JPanel();
		savePnl = new JPanel();
		
		add = new JButton("+ Add ");
		add.addActionListener(myActionListener);
		add.setActionCommand("ADD");
		addPnl.add(add,cb);
		scrollPanel.add(addPnl,cb);
		cb.gridy+=1;
		
		if (!this.data.getExtendable()) {
			add.setEnabled(false);
		}

		if (!commitInsteadOfSave) {
			cancel = new JButton("Cancel");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(myActionListener);
			
			save = new JButton("Save");
			save.setActionCommand("SAVE");
			save.addActionListener(myActionListener);
		}else {
			cancel = new JButton("Discard changes");
			cancel.setActionCommand("CANCEL");
			cancel.addActionListener(myActionListener);
			
			save = new JButton("Commit changes");
			save.setActionCommand("COMMIT");
			save.addActionListener(myActionListener);
		}
		
		savePnl.add(cancel);
		savePnl.add(save);
		scrollPanel.add(savePnl,cb);
	}
	
	public void reload() {
		this.removeAll();
		this.setLayout(new BorderLayout ());
		

		GridBagLayout c = new GridBagLayout();
		scrollPanel = new JPanel(c);

		GridBagConstraints cb = new GridBagConstraints();
		cb.fill = GridBagConstraints.HORIZONTAL;
		cb.gridx = 0;
		cb.gridy = 0;
		cb.ipady = 15;
		
		JScrollPane p = new JScrollPane(scrollPanel);
		p.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(p,BorderLayout.CENTER);
		
		if (data.getType() == ETYPE.OBJECT) {
			HashMap<?, ?> obj = (HashMap<?,?>) data.getData();
			for (Object key : obj.keySet()) {
				addComponent((Data)obj.get(key),cb);
				cb.gridy+=1;
			}
		}else if (data.getType() == ETYPE.LIST) {
			ArrayList<?> list = (ArrayList<?>) data.getData();
			for (int i=0;i< list.size(); i++) {
				addComponent((Data)list.get(i),cb);
				cb.gridy+=1;
			}
		}
		
		addCanelSaveAndAddButtons(!saveOnExit,cb);
		
		this.repaint();
		this.revalidate();
	}

	public ConfigSetterGUI(Data data, JFrame parent, JFrame caller, JSONConfig config) {
		this(data, parent, caller, config, false);
	}
		
	public ConfigSetterGUI(Data data, JFrame parent, JFrame caller, JSONConfig config, boolean saveOnExit) {
		super();
		this.data = data;
		this.setters = new ArrayList<ISetter>();
		this.config = config;
		this.parent = parent;
		this.caller = caller;
		this.saveOnExit = saveOnExit;
		reload();
	}
	
	private class MyActionListener implements ActionListener {

		private JSONConfig  config;
		private JFrame parent;
		private JFrame caller;
		private ArrayList<ISetter> setters;
		private Data data;
		private ConfigSetterGUI parentGUI;
		
		private MyActionListener(JSONConfig config, JFrame parent, JFrame caller, ArrayList<ISetter> setters, Data data, ConfigSetterGUI parentGUI) {
			this.config = config;
			this.parent = parent;
			this.caller = caller;
			this.setters = setters;
			this.data    = data;
			this.parentGUI = parentGUI;
		}
		
		private ArrayList<String> errorList() {
			ArrayList<String> toReturn = new ArrayList<String>();
			for (int i=0;i<this.setters.size();i++) {
				if (!this.setters.get(i).isDataValid()) {
					toReturn.add(this.setters.get(i).getData().getName());
				}
			}
			return toReturn;
		}
		
		private String putIntoSingleString(ArrayList<String> list) {
			String toReturn = "";
			for (int i=0;i<list.size()-1;i++) {
				toReturn += list.get(i) + ", ";
			}
			toReturn += list.get(list.size()-1);
			
			return toReturn;
		}
		
		private String getConfigValidationError() {
			return "NOT IMPLEMENTED YET"; //TODO IMPLEMENT
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ArrayList<String> errorArray = errorList();
			
			switch(arg0.getActionCommand()) {

			case "ADD":
				this.parent.setEnabled(false);
				
				JFrame frame = new JFrame();
				frame.setLocation(new Point(this.parent.getLocation())); 
				frame.addWindowListener(new EnableParentOnClose(this.parent));
				frame.setContentPane(new CreateDataGUI(frame, this.data, this.parentGUI));
				frame.setSize(375,125);
				frame.setVisible(true);
				
				
				break;
			case "COMMIT":
				if (errorArray.isEmpty()) {
					caller.setEnabled(true);
					parent.dispose();
				}else {
					JOptionPane.showMessageDialog(this.parent,
					  "The following variables have errors: " + putIntoSingleString(errorArray),
					  "Validation error",
					  JOptionPane.ERROR_MESSAGE);
				}
				break;
			case "SAVE":
				if (errorArray.isEmpty()) {
					try {
						if (config.validate()) {
							config.save(config.getFilePath());
							caller.setEnabled(true);
							parent.dispose();
						}else {
							JOptionPane.showMessageDialog(this.parent,
								    "Global validation failed: " + getConfigValidationError(),
								    "Validation error",
								    JOptionPane.ERROR_MESSAGE);
						}
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(this.parent,
							    "Failed to save config: " + e.getMessage(),
							    "Save error",
							    JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(this.parent,
						    "The following variables have errors: " + putIntoSingleString(errorArray),
						    "Validation error",
						    JOptionPane.ERROR_MESSAGE);
				}
				break;
			case "CANCEL":
				try {
					config.reload();
				} catch (JSONException | IOException | InvalidTypeException | ConfigNotValidException | CustomValidationException e) {
					JOptionPane.showMessageDialog(this.parent,
						    "Failed to reload the data blob: " + e.getMessage(),
						    "reload error",
						    JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				caller.setEnabled(true);
				parent.dispose();
			}
		}
	}	
}

