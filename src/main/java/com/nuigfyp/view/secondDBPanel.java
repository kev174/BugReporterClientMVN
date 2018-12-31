package com.nuigfyp.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class secondDBPanel extends JPanel {
	
	private JButton btnAddToDB, btnUpdateDB, btnDeleteEntry;

	public secondDBPanel() {
		
		// No need to create a JPanel panel = new JPanel(); and adding objects
		// you add by just placing add(... to it, and no JPanel object
		setLayout(null);
		setBorder(new TitledBorder(null, "Connect to DB", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setBounds(666, 164, 133, 194);
		
		btnAddToDB = new JButton("Add to Table");
		btnAddToDB.setBounds(5, 20, 110, 23);
		add(btnAddToDB);

		btnUpdateDB = new JButton("Update Table");
		btnUpdateDB.setBounds(5, 50, 110, 23);
		add(btnUpdateDB);

		btnDeleteEntry = new JButton("Delete Entry");
		btnDeleteEntry.setBounds(5, 80, 110, 23);
		add(btnDeleteEntry);		
	}
	
	// online example show actionListeners in this class along with accessor methods
	// https://stackoverflow.com/questions/33845152/split-gui-in-few-classes
}