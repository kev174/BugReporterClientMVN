package com.nuigfyp.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.nuigfyp.controller.mainController;
import com.nuigfyp.database.ConnectToAPIDatabase;

public class BugReporterLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2679566463301715120L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPassword;
	private JLabel consoleLabel;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BugReporterLogin() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 382, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Bug Reporter Login");

		JLabel bugLabel = new JLabel("");
		bugLabel.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("bug.png")));
		bugLabel.setBounds(229, 11, 137, 180);
		contentPane.add(bugLabel);

		consoleLabel = new JLabel("Select User");
		consoleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		consoleLabel.setBounds(32, 21, 158, 29);
		contentPane.add(consoleLabel);

		final JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Kevin", "Desmond" }));
		comboBox.setBounds(32, 62, 158, 23);
		contentPane.add(comboBox);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				consoleLabel.setText("Checking Database.");
				//consoleLabel.repaint();
				//consoleLabel.setVisible(true);

				String username = txtUser.getText();
				String password = txtPassword.getText();
				String user = String.valueOf(comboBox.getSelectedItem());
				
				
				launchMainGUI(username, password, user);
					
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(250, 202, 89, 23);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Enter Username");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(32, 107, 158, 23);
		contentPane.add(lblNewLabel_1);

		txtUser = new JTextField();
		txtUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtUser.setText("user");
		txtUser.setBounds(32, 130, 158, 23);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Enter Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(32, 175, 158, 23);
		contentPane.add(lblNewLabel_2);

		txtPassword = new JTextField();
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPassword.setText("password");
		txtPassword.setBounds(32, 201, 158, 26);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
	}

	
	public void launchMainGUI(String username, String password, String user) {
		
		bugReporterView frame = new bugReporterView(user);
		ConnectToAPIDatabase connectToAPIDatabase = new ConnectToAPIDatabase(); 
		boolean validLogin = connectToAPIDatabase.authentication(username, password);
		if(validLogin) {
			new mainController( frame, connectToAPIDatabase);
			frame.setVisible(true);
			
			this.setVisible(false);
			
		} else {
			consoleLabel.setText("Incorrect Login.");
		}
	}
	
}
