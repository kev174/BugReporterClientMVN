package com.nuigfyp.services;

import java.awt.BorderLayout;
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

public class AddEntryManager {

	private final static Logger log = Logger.getLogger(AddEntryManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	
	// === Possibly add theView in this constructor so as not to pass it in the method below all the time
	// Test to ensure that every time you change theView and you create a new entry, that it is added.

	public String addEntrywithAjax(final bugReporterView theView, final int[] filesChanged, final String screenshotDBDirectory, final String[] screenshotPath, final String documentDBDirectory, final String[] documentPath) {
		
		// === this is going to be the dimension of the two pdf, screenshot JButtons
		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		connectToAPIDatabase = new ConnectToAPIDatabase();				
		
		final String reporter = theView.recorderField.getText();
		final String tester = theView.testerField.getText();
		final String description = theView.descriptionArea.getText();
		final int severity = theView.getSeverityIndex(); // theView.severityField.getSelectedIndex();
		int project = theView.projectField.getSelectedIndex();	
		final int projectID = ++project;
		int bugClassification = theView.classificationField.getSelectedIndex();
		final int classificationIndex = ++bugClassification;
		
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {
			
				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				
				// ==== INNER CLASS BELOW, SO HAVE TO USE COPY OF VARIABLES. Error make final or effectively final ====
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
							return ("AddEntryManager.addEntrywithAjax(): Screenshot image did not get saved to the Database.");
							//theView.setStatus("AddEntryManager.addEntrywithAjax(): Screenshot image did not get saved to the Database.");
						}
					}
					
					if (filesChanged[1] == 0) {
						copyDocumentDBDirectory = "No";
					}
					if (filesChanged[1] == 1) {
						// === if a return value of 'No', then the file never got saved to the database
						String postResponse = connectToAPIDatabase.POSTRequest(documentPath[0], projectID);			
						if(!postResponse.equals("No")) {   // % POSTResponse extracts a String 'No' from Response.entity() %
							copyDocumentDBDirectory = (postResponse);
							copyDocumentDBDirectory = copyDocumentDBDirectory.replace("\"", "");
						} else {
							return ("AddEntryManager.addEntrywithAjax(): Document PDF did not get saved to the Database.");
							//theView.setStatus("mainController.addToDB(): Document PDF did not get saved to the Database.");
						}
						
						/*documentDBDirectory = connectToAPIDatabase.POSTRequest(documentPath[0], projectID);
						documentDBDirectory = documentDBDirectory.replace("\"", "");*/
					}

					// === try creating a Bug object in the Controller and assign the two copy variables by the bugs accessor methods ===
					//Bug bug = new Bug(Integer.parseInt("0"), reporter, tester, description, severity, projectID, copyScreenshotDBDirectory, copyDocumentDBDirectory);
					Bug bug = new Bug(Integer.parseInt("0"), reporter, tester, description, severity, projectID, copyScreenshotDBDirectory, copyDocumentDBDirectory, "", "", 0, classificationIndex);
					
					@SuppressWarnings("unused") // PKGenerated not used, but it does call the function required
					int PKGenerated = (connectToAPIDatabase.addEntry(bug, filesChanged));

				} catch (Exception ex) {
					System.out.println("NOTE: If null in two files in DB, then this shows. AddEntryManager.addEntrywithAjax(): I could remove this Exception as i caught it. " + ex);
					theView.setStatus("Failed to create a new entry. Please try again later.");
					log.error("General Exception at AddEntryManager.addEntrywithAjax(). " + ex);
					return ("Failed to create a new entry. Please try again later.");
					//return null;
				}
				
				// <html>Status: " + message + "<BR>Operating System."
				// theView.setStatus(reporter + ", your new entry was added to the Database <BR>Successfully.");
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
			log.error("General Exception at AddEntryManager.addEntrywithAjax(). " + e);
			e.printStackTrace();
		}
		
		return (reporter + ", your new entry was added Successfully to the Database.");
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
