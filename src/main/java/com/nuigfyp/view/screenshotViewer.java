package com.nuigfyp.view;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class screenshotViewer extends JFrame {

	private JPanel contentPane;
	public Image resizedScreenshotImage;
	private Image bug;

	public screenshotViewer(Image image, String fileName) {				

		bug = new ImageIcon(this.getClass().getClassLoader().getResource("bug.png")).getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT);	
		setIconImage(bug);
		setTitle("Screenshot Viewer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 0, 750, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.CYAN, null, null, null));
		panel.setBounds(0, 0, 730, 650);
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(0, 0, 720, 740);
		panel.add(lblNewLabel);
		int width = lblNewLabel.getWidth();
		int height = lblNewLabel.getHeight();	
		Image newimg = image.getScaledInstance(width-10, height-106, Image.SCALE_SMOOTH);
		ImageIcon scrshotImageIcon = new ImageIcon(newimg);
		lblNewLabel.setIcon(scrshotImageIcon);

		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.ORANGE);
		panel1.setBounds(9, 655, 720, 51);
		contentPane.add(panel1);
		
		JLabel fileNameLbl = new JLabel(new File("").getAbsolutePath() + "\\Downloaded_Files\\" + fileName);
		panel1.add(fileNameLbl);
		this.setVisible(true);
	}
}