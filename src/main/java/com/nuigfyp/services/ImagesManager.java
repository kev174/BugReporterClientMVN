package com.nuigfyp.services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class ImagesManager {

	private Image[] buttonImages = new Image[9];
	private ImageIcon[] jtableImages = new ImageIcon[2];
	private ImageIcon[] jlabelImages = new ImageIcon[9];
	private final static Logger log = Logger.getLogger(ImagesManager.class);
	private final int imageX = 166, imageY = 36;

	// www.iconfinder.com: http://buttonoptimizer.com/(can add Image to it
	// https://www.flaticon.com/free-icons/database	
	// https://www.w3schools.com/colors/colors_names.asp
	
	public Image[] buttonImages() {

		try {	
			
			buttonImages[0] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Show_Bugs.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);
			buttonImages[1] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Search.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT); 
			buttonImages[2] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Clear_Entry.png")).getImage()
					.getScaledInstance(166, 36, Image.SCALE_DEFAULT);
			buttonImages[3] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Create_Entry.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);
			buttonImages[4] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Update_Entry.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);
			buttonImages[5] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Remove_Entry.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);
			buttonImages[6] = new ImageIcon(this.getClass().getClassLoader().getResource("F_Change_Status.png"))
					.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);
			buttonImages[7] = new ImageIcon(this.getClass().getClassLoader().getResource("view-html-format.png"))   
					.getImage().getScaledInstance(180, 84, Image.SCALE_DEFAULT);
			buttonImages[8] = new ImageIcon(this.getClass().getClassLoader().getResource("bug.png")).getImage()
					.getScaledInstance(70, 70, Image.SCALE_DEFAULT);				
			
		} catch (Exception e) {
			log.error("General Exception at ImagesManager.buttonImages(). " + e);
			e.printStackTrace();
		}

		return buttonImages;
	}

	public ImageIcon[] jtableImages() {

		try {
			
			jtableImages[0] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("transparent-X.png"))
					.getImage().getScaledInstance(17, 17, Image.SCALE_DEFAULT));
			jtableImages[1] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("transparent-green-check.png"))
					.getImage().getScaledInstance(17, 17, Image.SCALE_DEFAULT));
			
		} catch (Exception e) {
			log.error("General Exception at ImagesManager.jtableImages(). " + e);
			e.printStackTrace();
		}

		return jtableImages;
	}

	public ImageIcon[] loadCheckBoxImages(int imageDimension) {

		double scallingFactor = 0.60;
		
		jlabelImages[0] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("screenshot.jpeg"))
				.getImage().getScaledInstance(imageDimension, imageDimension, Image.SCALE_DEFAULT));
		jlabelImages[1] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("PDF_Image.jpg"))
				.getImage().getScaledInstance(imageDimension, imageDimension, Image.SCALE_DEFAULT));
		jlabelImages[2] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("rotating.gif")) // spinner.gif good also
				.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));

		try {
			
			BufferedImage bi = ImageIO.read(this.getClass().getClassLoader().getResource("Misc-Web-Database-icon.png"));
			int scaleX = (int) (bi.getWidth() * scallingFactor);
			int scaleY = (int) (bi.getHeight() * scallingFactor);
			Image image = bi.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
			jlabelImages[3] = new ImageIcon(image);

		} catch (IOException e) {
			log.error("General Exception at ImagesManager.loadCheckBoxImages(). " + e);
			e.printStackTrace();
		}
		
		jlabelImages[4] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("animated-bin-and-trash-can-image.gif")) // spinner.gif good also // good also  delete-file.jpg
		.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));
		
		jlabelImages[5] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("graph.jpg"))
				.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));		
		jlabelImages[6] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("ringing_bells.gif")) 			
				.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));
		jlabelImages[7] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("image_to_text.jfif")) 
				.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));
		jlabelImages[8] = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("status_change.png")) 
				.getImage().getScaledInstance(imageDimension / 2, imageDimension / 2, Image.SCALE_DEFAULT));

		return jlabelImages;
	}
}
