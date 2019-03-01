package com.nuigfyp.services;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.model.Bug;
import com.nuigfyp.view.ShowGeneratedHTML;
import com.nuigfyp.view.bugReporterView;

public class ViewInHTMLManager {

	private final static String DOWNLOADED_FILES = System.getProperty("user.dir") + "\\Downloaded_Files";
	private final static Logger log = Logger.getLogger(ConnectToDBManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	
	
	public void dispalyInHTML(final bugReporterView theView, final ArrayList<Bug> bugList, final int id) {
		
		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			
			protected String doInBackground() throws InterruptedException {

				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				Bug searchedBug = new Bug();

				try {

					for(Bug bug : bugList) {
				          if(bug.getId() == id) {
				        	  searchedBug = bug;
				        	  break;
				          }
				     }
					
					String scrDir = searchedBug.getScreenshot();
					String pdfDir = searchedBug.getDocument();
					File screenFile = new File(scrDir);
					File documentFile = new File(pdfDir);
										
					if (!scrDir.equals("No") && (!checkIfFileExists(DOWNLOADED_FILES + "\\" + screenFile.getName()))) {

						try {
							ConnectToAPIDatabase.GETRequest(screenFile.getName());
						} catch (Exception e) {
							theView.setStatus("Cannot Download Screenshot File.");
							log.error("General Exception at ViewInHTMLManager.dispalyInHTML(). " + e);
							return null;
						}
					}

					if(!pdfDir.equals("No") && (!checkIfFileExists(DOWNLOADED_FILES + "\\" + documentFile.getName()))) {
						
						try {
							ConnectToAPIDatabase.GETRequest(documentFile.getName());
						} catch (Exception e) {
							theView.setStatus("Cannot Download Document File.");
							log.error("General Exception at ViewInHTMLManager.dispalyInHTML(). " + e);
							return null;
						}
					} 					

					new ShowGeneratedHTML(searchedBug);

				} catch (Exception ex) {
					DisplayMessageInJOptionPane(
							"ViewInHTMLManager.dispalyInHTML(): You have not selected an appropiate ID from the Table." + ex,
							"Please highlight a Valid Item.");
					log.error("General Exception at ViewInHTMLManager.dispalyInHTML(). " + ex);
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
		} catch (Exception ex) {
			log.error("General Exception at ViewInHTMLManager.dispalyInHTML(). " + ex);
		}
	}
		
	
	public boolean checkIfFileExists(String fileDirectory) {

		File f = new File(fileDirectory);

		if (f.exists() && !f.isDirectory()) {
			return true;
		}

		return false;
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
}
