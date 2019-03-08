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

	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	private String returnConnectionString = "";
	
	
	public String updateEntrywithAjax(final bugReporterView theView, final int[] filesChanged, final String screenshotDBDirectory, final String[] screenshotPath, final String documentDBDirectory, final String[] documentPath) {

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

				String copyScreenshotDBDirectory = screenshotDBDirectory;
				String copyDocumentDBDirectory = documentDBDirectory;

				try {

					String primaryKey = theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString();
					String reporter = theView.recorderField.getText();
					String tester = theView.testerField.getText();
					String description = theView.descriptionArea.getText();
					int severity = theView.getSeverityIndex(); 
					int project = theView.projectField.getSelectedIndex();
					int bugClassification = theView.classificationField.getSelectedIndex();
					int classificationIndex = ++bugClassification;
										
					Bug specificBugSearch = new Bug(); 
					specificBugSearch = (connectToAPIDatabase.GETSpecificBugObject(primaryKey));
					bug.setScreenshot(specificBugSearch.getScreenshot());
					bug.setDocument(specificBugSearch.getDocument());

					if (theView.screenshotTableEqualsYes() && (!theView.chkUploadScreenshot.isSelected())) {
						int reply = JOptionPane.showConfirmDialog(null,
								"This Screenshot will be removed from the database.", "Screenshot marked for Deletion.",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[4]);
						if (reply == JOptionPane.YES_OPTION) {
							bug.setScreenshot("No");
						}
					}
					
					if (theView.documentTableEqualsYes() && (!theView.chkUploadDocument.isSelected())) {
						int reply = JOptionPane.showConfirmDialog(null,
								"This Document will be removed from the database.", "Document marked for Deletion.",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[4]);
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

					if (filesChanged[0] == 1) {

						String postResponse = connectToAPIDatabase.POSTRequest(screenshotPath[0], bug.getProject());
						if (!postResponse.equals("No")) { 
							copyScreenshotDBDirectory = (postResponse);
							copyScreenshotDBDirectory = copyScreenshotDBDirectory.replace("\"", "");
							bug.setScreenshot(copyScreenshotDBDirectory);
						} else {
							theView.setStatus("UpdateDBManagr.updateEntrywithAjax(): Screenshot did not get saved to the Database.");
						}
					}
					
					if (filesChanged[1] == 1) {

						String postResponse = connectToAPIDatabase.POSTRequest(documentPath[0], bug.getProject());
						if (!postResponse.equals("No")) { 
							copyDocumentDBDirectory = (postResponse);
							copyDocumentDBDirectory = copyDocumentDBDirectory.replace("\"", "");
							bug.setDocument(copyDocumentDBDirectory);
							
						} else {
							returnConnectionString = ("UpdateDBManagr.updateEntrywithAjax(): Document not saved to the Database.");
						}
					}

					returnConnectionString = (connectToAPIDatabase.updateEntry(bug, filesChanged));

				} catch (Exception e) {
					DisplayMessageInJOptionPane(
							"mainController.updateDB(); Exception caught, possibly due to the following...\\nYou have not selected an appropiate item from the Table.\nCheck if this ID exists in DB.",
							"Please select a valid item.");
					theView.setStatus("UpdateDBManagr.updateEntrywithAjax(): Multiple possible exceptions.");
					log.error("General Exception at UpdateDBManagr.updateEntrywithAjax():. " + e);
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
		} catch (Exception ex) {
			log.error("General Exception at UpdateDBManagr.updateEntrywithAjax():. " + ex);
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
	
	
	public void DisplayMessageInJOptionPane(String title, String message) {
		JOptionPane.showMessageDialog(theView, title, message, JOptionPane.INFORMATION_MESSAGE);
	}
}
