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


public class UpdateDBManager {

	private final static Logger log = Logger.getLogger(UpdateDBManager.class);
	/*private final static String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files";
	private String[] bugClasses = { "Text", "Truncation", "Graphics", "On Click", "Software" };*/
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	
	
	public String updateEntrywithAjax(final bugReporterView theView, final int[] filesChanged, final String screenshotDBDirectory, final String[] screenshotPath, final String documentDBDirectory, final String[] documentPath) {

		// === this is going to be the dimension of the two pdf, screenshot JButtons
		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		connectToAPIDatabase = new ConnectToAPIDatabase();

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {

				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				Bug bug = new Bug(0, "", "", "", 0, 0, "", "", "", "", 0, 0);

				// ==== INNER CLASS BELOW, SO HAVE TO USE COPY OF VARIABLES. Error make final or
				// effectively final ====
				String copyScreenshotDBDirectory = screenshotDBDirectory;
				String copyDocumentDBDirectory = documentDBDirectory;

				try {

					String primaryKey = theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString();
					String reporter = theView.recorderField.getText();
					String tester = theView.testerField.getText();
					String description = theView.descriptionArea.getText();
					int severity = theView.getSeverityIndex(); // theView.severityField.getSelectedIndex();
					int project = theView.projectField.getSelectedIndex();
					int bugClassification = theView.classificationField.getSelectedIndex();
					int classificationIndex = ++bugClassification;
					
					
					Bug specificBugSearch = new Bug(); // this contains directory to screenshot and document
					specificBugSearch = (connectToAPIDatabase.GETSpecificBugObject(primaryKey));
					bug.setScreenshot(specificBugSearch.getScreenshot());
					bug.setDocument(specificBugSearch.getDocument());

					if (theView.screenshotTableEqualsYes() && (!theView.chkUploadScreenshot.isSelected())) {
						System.out.println(
								"Screenshot Table says 'Yes', but Screenshot upload is Unticked. So setting setScreenshot() to 'No'.");
						int reply = JOptionPane.showConfirmDialog(null,
								"This Screenshot will be removed from the database.", "Screenshot marked for Deletion.",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[6]);
						if (reply == JOptionPane.YES_OPTION) {
							bug.setScreenshot("No");
						}
					}
					
					if (theView.documentTableEqualsYes() && (!theView.chkUploadDocument.isSelected())) {
						System.out.println(
								"Document Table says 'Yes', but Document upload is Unticked. So setting setDocument() to 'No'.");
						int reply = JOptionPane.showConfirmDialog(null,
								"This Document will be removed from the database.", "Document marked for Deletion.",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[6]);
						if (reply == JOptionPane.YES_OPTION) {
							bug.setDocument("No");
						}
					}

					bug.setId(Integer.parseInt(primaryKey));
					bug.setReporterName(reporter);
					bug.setTesterName(tester);
					bug.setDescription(description);
					bug.setSeverity(severity);
					bug.setProject(++project);
					bug.setBugClassification(classificationIndex);

					System.out.println("Update: Screenshot Changed[0] " + filesChanged[0] + ", Screenshot Changed[1] " + filesChanged[1]);

					if (filesChanged[0] == 1) {

						// ----------------------------------------------------------------------------------------------------------------
						// If Screenshot has changed then i have to upload this file by calling below
						// and adding to bug.setScreenshot(...);
						// ----------------------------------------------------------------------------------------------------------------
						System.out.println("A Screenshot fileChanged[0] " + filesChanged[0]);

						String postResponse = connectToAPIDatabase.POSTRequest(screenshotPath[0], bug.getProject());
						if (!postResponse.equals("No")) { // % POSTResponse extracts a String 'No' from Response.entity() %
							copyScreenshotDBDirectory = (postResponse);
							copyScreenshotDBDirectory = copyScreenshotDBDirectory.replace("\"", "");
							bug.setScreenshot(copyScreenshotDBDirectory);
						} else {
							theView.setStatus("UpdateDBManagr.updateEntrywithAjax(): Screenshot did not get saved to the Database.");
						}

						/*
						 * screenshotDBDirectory = connectToAPIDatabase.POSTRequest(screenshotPath[0], bug.getProject()); 
						 * screenshotDBDirectory = screenshotDBDirectory.replace("\"", "");
						 * bug.setScreenshot(screenshotDBDirectory);
						 */
					}
					if (filesChanged[1] == 1) {

						// ------------------------------------------------------------------------------------------------------------
						// If Document has changed then i have to upload this file by calling below and
						// adding to bug.setDocument(...);
						// ------------------------------------------------------------------------------------------------------------
						System.out.println("A Document fileChanged[1] " + filesChanged[1] + ", with directroy of "
								+ documentPath[0]);

						String postResponse = connectToAPIDatabase.POSTRequest(documentPath[0], bug.getProject());
						if (!postResponse.equals("No")) { // % POSTResponse extracts a String 'No' from Response.entity() %
							copyDocumentDBDirectory = (postResponse);
							copyDocumentDBDirectory = copyDocumentDBDirectory.replace("\"", "");
							bug.setDocument(copyDocumentDBDirectory);
							
						} else {
							theView.setStatus("UpdateDBManagr.updateEntrywithAjax(): Document not saved to the Database.");
						}

						// Look into moving this to the POSTRequest method and not here and the AddEntryManager.
						// The following works in copying the users selected Screenshot file but, i need to 
						// append the time stamp to the name of this file. POssibly move this method to 
						// If the directory does not exist already you will get an Exception. Linux!!!
						/*File source = new File(postResponse);
					    File dest = new File(DOWNLOADED_FILES_DIRECTORY + "/" + source.getName());
						try {
							FileUtils.copyFile(source, dest);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}*/
					}

					theView.setStatus(connectToAPIDatabase.updateEntry(bug, filesChanged));
					/*theView.clearTable();
					theView.setTable((connectToAPIDatabase.getAllBugs()));*/
					//filesChanged = new int[] { 0, 0, 0, 0 };

				} catch (Exception e) {
					DisplayMessageInJOptionPane(
							"mainController.updateDB(); Exception caught, possibly due to the following...\\nYou have not selected an appropiate item from the Table.\nCheck if this ID exists in DB.",
							"Please select a valid item.");
					theView.setStatus("UpdateDBManagr.updateEntrywithAjax(): Multiple possible exceptions.");
					log.error("General Exception at UpdateDBManagr.updateEntrywithAjax():. " + e);
					e.printStackTrace();
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
			log.error("General Exception at UpdateDBManagr.updateEntrywithAjax():. " + ex);
			ex.printStackTrace();
		}
		
		return "Updated database successfully.";
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
