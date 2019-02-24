package com.nuigfyp.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.nuigfyp.controller.mainController;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.services.AddEntryManager;

public class BugReporterLogin extends JFrame {

	private static final long serialVersionUID = 2679566463301715120L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPassword;
	private JLabel consoleLabel;
	private Image bug;	
	private final static Logger log = Logger.getLogger(AddEntryManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ConnectToAPIDatabase connectToAPIDatabase;
	private JLabel bugLabel;
	

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BugReporterLogin() {
		
		bug = new ImageIcon(this.getClass().getClassLoader().getResource("bug.png")).getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT);	
		setIconImage(bug);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 382, 295);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Bug Reporter Login");

		bugLabel = new JLabel("");
		bugLabel.setIcon(new ImageIcon(bug));		
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
				
				
				//launchMainGUI(username, password, user);
				validLoginUser(username, password, user);
					
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(245, 202, 89, 23);
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

	
	public void validLoginUser(final String username, final String password, final String user) {

		int imageDimension = 110; 
		ajaxLoader = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("rotating.gif")).getImage().getScaledInstance(imageDimension, imageDimension, Image.SCALE_DEFAULT));
		final JDialog loading = loadingModel();
		connectToAPIDatabase = new ConnectToAPIDatabase();

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {

				bugLabel.setIcon(ajaxLoader);
				bugReporterView frame = new bugReporterView(user);
				boolean validLogin = connectToAPIDatabase.authentication(username, password);
				
				if(validLogin) {
					
					new mainController(frame, connectToAPIDatabase);
					frame.setVisible(true);				
					setVisible(false);
					
				} else {
					consoleLabel.setText("Incorrect Login.");
					setIconImage(bug);
				}

				return null;
			}

			protected void done() {
				bugLabel.setIcon(motionlessAjaxLoader);
				bugLabel.setEnabled(true);
				loading.dispose();
			}
		};

		worker.execute();
		loading.setVisible(true);

		try {
			worker.get();
		} catch (Exception e) {
			log.error("General Exception at bugReporterLogin.validLoginUser(). " + e);
			//e.printStackTrace();
		}

	}

	private JDialog loadingModel() {
		JDialog loading = new JDialog();
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("Please Wait..."), BorderLayout.CENTER);
		loading.setUndecorated(true);
		loading.getContentPane().add(p1);
		loading.pack();
		loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		loading.setModal(true);

		return loading;
	}

}
