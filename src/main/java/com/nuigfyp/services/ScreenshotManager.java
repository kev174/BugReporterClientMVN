package com.nuigfyp.services;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.model.Bug;
import com.nuigfyp.view.bugReporterView;
import com.nuigfyp.view.screenshotViewer;

public class ScreenshotManager  {

	private final static Logger log = Logger.getLogger(ScreenshotManager.class);
	private final static String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files";
	private final static String DOWNLOADED_FILES = System.getProperty("user.dir") + "\\Downloaded_Files";	
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private Image resizedScreenshotImage;

	
	public void viewScreenshotWithAjax(final bugReporterView theView, final int bugFromTableId, final ArrayList<Bug> bugFromTable) {

		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {

				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				Image bpi = null;
				Bug searchedBug = new Bug();

				try {

					for (Bug bug : bugFromTable) {
						if (bug.getId() == bugFromTableId) {
							searchedBug = bug;
							break;
						}
					}

					String scrDir = searchedBug.getScreenshot();
					File screenFile = new File(scrDir);

					System.out.println("Screenfile downloaded dir is: " + DOWNLOADED_FILES + "\\" + screenFile.getName());

					if (!scrDir.equals("No") && (!checkIfScreenshotFileExists(DOWNLOADED_FILES + "\\" + screenFile.getName()))) {
					
						try {
							theView.setStatus("Downloading the Screenhot file for Bug Id " + bugFromTableId);
							ConnectToAPIDatabase.GETRequest(screenFile.getName());
							
						} catch (Exception e) {
							theView.setStatus("Cannot Download Screenshot File.");
							log.error("General Exception at ScreenshotManager.viewScreenshotWithAjax(). " + e);
							return null;
						}
					}

					File f = new File(DOWNLOADED_FILES_DIRECTORY + "\\" + screenFile.getName());
					InputStream input = new FileInputStream(f);
					BufferedImage bi = ImageIO.read(input);
					ImageIcon imageIcon = new ImageIcon(bi);
					int height = theView.btnScreenshot.getHeight();
					int width = theView.btnScreenshot.getWidth();
					bpi = imageIcon.getImage();
					resizedScreenshotImage = bpi.getScaledInstance(height, width, Image.SCALE_SMOOTH);
					theView.btnScreenshot.setIcon(new ImageIcon(resizedScreenshotImage));
					theView.btnScreenshot.setEnabled(true);
					
					new screenshotViewer(bpi, screenFile.getName());

				} catch (Exception ex) {
					DisplayMessageInJOptionPane(
							"ScreenshotManager.viewScreenshotWithAjax(): You have not selected an appropiate ID from the Table." + ex,
							"Please highlight a Valid Item.");
					log.error("General Exception at ScreenshotManager.viewScreenshotWithAjax(). " + ex);
				}

				return null;
			}

			protected void done() {
				theView.lblDatabase.setIcon(motionlessAjaxLoader);
				theView.lblDatabase.setEnabled(true);
				loading.dispose();
			}
		};

		worker.execute();
		loading.setVisible(true);

		try {
			worker.get();
		} catch (Exception e) {
			log.error("General Exception at ScreenshotManager.viewScreenshotWithAjax(). " + e);
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
	
	
	public void DisplayMessageInJOptionPane(String title, String message) {
		JOptionPane.showMessageDialog(theView, title, message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	public boolean checkIfScreenshotFileExists(String screenshotFileDirectory) {

		File f = new File(screenshotFileDirectory);

		if (f.exists() && !f.isDirectory()) {
			return true;
		}

		return false;
	}
}
