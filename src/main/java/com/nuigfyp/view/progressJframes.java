package com.nuigfyp.view; 

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")	// ******* NOT USED *******
public class progressJframes extends JFrame {

	private JPanel contentPane;
	private static JLabel lblDatabaseProgress, lblProgressImage;
	private static javax.swing.Timer timer;
	private static double degree = 0.0;
	private static boolean active = true;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (active) {
						progressJframes frame = new progressJframes();
						frame.setVisible(true);
						frameTimer();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public progressJframes() {

		setTitle("Database Status");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 128, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblDatabaseProgress = new JLabel("In.Progress");
		lblDatabaseProgress.setBounds(10, 11, 109, 31);
		contentPane.add(lblDatabaseProgress);

		lblProgressImage = new JLabel();
		lblProgressImage.setBounds(10, 39, 124, 109);
		contentPane.add(lblProgressImage);

	}


	public static void frameTimer() {

		timer = new javax.swing.Timer( 100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("I am a timer.");

				double scallingFactor = 0.09; // 0.065
				BufferedImage bImage = null;
				BufferedImage newBufferedImage = null;

				try {
					bImage = ImageIO.read(this.getClass().getClassLoader().getResource("curvedarrow.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				int scaleX = (int) (bImage.getWidth() * scallingFactor);
				int scaleY = (int) (bImage.getHeight() * scallingFactor);
				Image image = bImage.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
				BufferedImage buffered = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_RGB);
				buffered.getGraphics().drawImage(image, 0, 0, null);

				AffineTransform tx = AffineTransform.getRotateInstance(degree, buffered.getWidth() / 2,
						buffered.getHeight() / 2); // change angel to a constant degree and not math.pi
				AffineTransformOp at = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				newBufferedImage = new BufferedImage(buffered.getWidth(), buffered.getHeight(), buffered.getType());
				at.filter(buffered, newBufferedImage);

				lblProgressImage.setIcon(new ImageIcon(newBufferedImage));
				degree -= 6.0;

			}
		});
		
		timer.start();
	}
}
