package com.nuigfyp.services;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.model.Bug;
import com.nuigfyp.view.bugReporterView;

public class AddEntryManager {

	private final static Logger log = Logger.getLogger(AddEntryManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	//private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	public String returnConnectionString = "";
	
	
	public String addEntrywithAjax(final bugReporterView theView, final int[] filesChanged, final String screenshotDBDirectory, final String[] screenshotPath, final String documentDBDirectory, final String[] documentPath) {
		
		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		connectToAPIDatabase = new ConnectToAPIDatabase();				
		
		final String reporter = theView.recorderField.getText();
		final String tester = theView.testerField.getText();
		final String description = theView.descriptionArea.getText();
		final int severity = theView.getSeverityIndex(); 
		int project = theView.projectField.getSelectedIndex();	
		final int projectID = ++project;
		int bugClassification = theView.classificationField.getSelectedIndex();
		final int classificationIndex = ++bugClassification;
		
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {
			
				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				
				String copyScreenshotDBDirectory = screenshotDBDirectory;
				String copyDocumentDBDirectory = documentDBDirectory;
				
				try {

					if (filesChanged[0] == 0) {
						copyScreenshotDBDirectory = "No";
					}						
					if (filesChanged[0] == 1) {
						
						String postResponse = connectToAPIDatabase.POSTRequest(screenshotPath[0], projectID);				
						if(!postResponse.equals("No")) {   // % POSTResponse extracts a String 'No' from Response.entity() %
							copyScreenshotDBDirectory = (postResponse);
							copyScreenshotDBDirectory = copyScreenshotDBDirectory.replace("\"", "");
						} else {
							returnConnectionString = ("AddEntryManager.addEntrywithAjax(): Screenshot image did not get saved to the Database.");
						}
					}
					
					if (filesChanged[1] == 0) {
						copyDocumentDBDirectory = "No";
					}
					if (filesChanged[1] == 1) {
						String postResponse = connectToAPIDatabase.POSTRequest(documentPath[0], projectID);			
						if(!postResponse.equals("No")) {   // % POSTResponse extracts a String 'No' from Response.entity() %
							copyDocumentDBDirectory = (postResponse);
							copyDocumentDBDirectory = copyDocumentDBDirectory.replace("\"", "");
						} else {
							returnConnectionString = ("AddEntryManager.addEntrywithAjax(): Document PDF did not get saved to the Database.");
						}
					}

					Bug bug = new Bug(Integer.parseInt("0"), reporter, tester, description, severity, projectID, copyScreenshotDBDirectory, copyDocumentDBDirectory, "", "", 0, classificationIndex);
					
					returnConnectionString = (connectToAPIDatabase.addEntry(bug, filesChanged));

				} catch (Exception ex) {
					theView.setStatus("Failed to create a new entry. Please try again later.");
					log.error("General Exception at AddEntryManager.addEntrywithAjax(). " + ex);
					returnConnectionString = ("Failed to create a new entry. Please try again later.");
				}
				
				return returnConnectionString;
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
			log.error("General Exception at AddEntryManager.addEntrywithAjax(). " + e);
		}
		
		return returnConnectionString;
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
	
	
	/*public void DisplayMessageInJOptionPane(String title, String message) {
		JOptionPane.showMessageDialog(theView, title, message, JOptionPane.INFORMATION_MESSAGE);
	}*/
}
