package com.nuigfyp.controller;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.model.*;
import com.nuigfyp.services.AddEntryManager;
import com.nuigfyp.services.ChangeStatusManager;
import com.nuigfyp.services.ClearTableManager;
import com.nuigfyp.services.ConnectToDBManager;
import com.nuigfyp.services.DeleteEntryManager;
import com.nuigfyp.services.ImageToTextManager;
import com.nuigfyp.services.ImagesManager;
import com.nuigfyp.services.PDFManager;
import com.nuigfyp.services.ScreenshotManager;
import com.nuigfyp.services.SearchDBManager;
import com.nuigfyp.services.UpdateDBManager;
import com.nuigfyp.services.ViewInHTMLManager;
import com.nuigfyp.view.AnalyticsViewer;
import com.nuigfyp.view.bugReporterView;


public class mainController {

	private final static Logger log = Logger.getLogger(mainController.class);
	private static bugReporterView theView;
	private ConnectToAPIDatabase connectToAPIDatabase = new ConnectToAPIDatabase();;
	private String[] fileInfo = new String[] { null, null };
	private String[] documentPath = new String[2], screenshotPath = new String[2];
	private int[] filesChanged = new int[]{0, 0, 0, 0};
	//public FileInputStream screenshotInputStream, documentInputStream;
	private ImagesManager im = new ImagesManager();
	private ImageIcon motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private String screenshotDBDirectory = "", documentDBDirectory = "";

	
	public mainController(bugReporterView theView) {
		
		//this.connectToAPIDatabase = connectToAPIDatabase;
		mainController.theView = theView;
		mainController.theView.viewAnalytics(new viewAnalytics());   
		mainController.theView.viewScreenshot(new viewScreenshot()); 
		mainController.theView.viewPdf(new viewPDF());
		mainController.theView.addConnectToDB(new connectToDB());
		mainController.theView.addEntryToDB(new addToDB());
		mainController.theView.addUpdateDB(new updateDB());
		mainController.theView.addDeleteFromDB(new deleteEntry());	
		mainController.theView.addStatusChangeInDB(new statusChangeInDB());		
		mainController.theView.addSearchDB(new searchDB());
		mainController.theView.uploadScreenshot(new getScreenshotPath());
		mainController.theView.uploadDocument(new getDocumentPath());
		mainController.theView.viewInHTMLFormat(new viewInHTML());
		mainController.theView.addEmptyFields(new clearFields());
		mainController.theView.addJTableListener(new MouseListenerClass(theView)); 
		
		setup();
		loadCheckBoxImages();	
		
	}
	
	
	class viewAnalytics implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			AnalyticsViewer analyticsViewer = new AnalyticsViewer();
			analyticsViewer.viewAnalytics(theView.getBugFromTable());

		}
	}
	
	class viewScreenshot implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int bugFromTableId = Integer.parseInt(theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString());

			if (bugFromTableId >= 0) {
				ArrayList<Bug> bugFromTable = theView.getBugFromTable();
				ScreenshotManager screenshotManager = new ScreenshotManager();
				screenshotManager.viewScreenshotWithAjax(theView, bugFromTableId, bugFromTable);
			}
		}
	}
		
	
	class viewPDF implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			int bugFromTableId = Integer.parseInt(theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString());
			
			if(bugFromTableId >= 0) {
				ArrayList<Bug> bugArraylistFromTable = theView.getBugFromTable();
				PDFManager sm = new PDFManager(); 
				sm.viewPDFwithAjax(theView, bugFromTableId, bugArraylistFromTable);				
			}
		}
	}
	

	class updateDB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			try {
				theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0);
			} catch (Exception e1) {
				theView.setStatus("Select a valid row to update from the table.");
				return;
			}
			
			UpdateDBManager uem = new UpdateDBManager();
			theView.setStatus(uem.updateEntrywithAjax(theView, filesChanged, screenshotDBDirectory, screenshotPath, documentDBDirectory, documentPath));	
			theView.clearTable();		
			
			try {
				theView.setTable((connectToAPIDatabase.getAllBugs()));
			} catch (Exception e1) {
				log.error("General Exception at mainController.updateDB(). " + e1);
			}	
			
			filesChanged = new int[] { 0, 0, 0, 0 };
		}
	}

	class statusChangeInDB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			int id = 0;
			
			try {
				id = (int) theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0);
			} catch (Exception e1) {
				theView.setStatus("Select a valid row to change status from the table.");
				return;
			}

			int reply = JOptionPane.showConfirmDialog(null, "Change Status of this ticket?", "Set Status Of Ticket.",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[8]);

			String ticketStatus = theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 1).toString();
			if (reply == 0 && ticketStatus.equals("Open")) { 
				ChangeStatusManager csm = new ChangeStatusManager();
				theView.setStatus(csm.changeStatusWithAjax(theView, id));
			}

			theView.clearTable();	
			
			try {
				theView.setTable((connectToAPIDatabase.getAllBugs()));
			} catch (Exception ex) {
				log.error("General Exception at mainController.statusChangeInDB(). " + ex);
			}
			
		}
	}

	class searchDB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			SearchDBManager sdbm = new SearchDBManager();
			ArrayList<Bug> returnBugList = new ArrayList<Bug>();
			String filterId = theView.getSearchID();
			
			try {
				
				returnBugList = sdbm.searchDBwithAjax(theView, filterId);
				
			} catch (Exception ex) {
				log.error("General Exception at mainController.searchDB(). " + ex);
			}
			
			if(returnBugList.size() > 0) {
				theView.setTable(returnBugList);
			}
		}
	}

	
	class addToDB implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
		
			AddEntryManager aem = new AddEntryManager();			
			theView.setStatus(aem.addEntrywithAjax(theView, filesChanged, screenshotDBDirectory, screenshotPath, documentDBDirectory, documentPath));	
			
			theView.clearTable();	
			
			try {
				theView.setTable((connectToAPIDatabase.getAllBugs()));
			} catch (Exception ex) {
				log.error("General Exception at mainController.addToDB(). " + ex);
			}	
			
			filesChanged = new int[] { 0, 0, 0, 0 };	
		}
	}

	class connectToDB implements ActionListener {	
		public void actionPerformed(ActionEvent e) {
			
			ArrayList<Bug> returnedBugs = new ArrayList<Bug>();
			ConnectToDBManager ctdbm = new ConnectToDBManager();
			returnedBugs = (ctdbm.ConnectToDBwithAjax(theView));
			
			if(!(returnedBugs.size() == 0)) {
				theView.setTable(returnedBugs);
				theView.setStatus("Successfully connected to the database.");
				return;
			} else {
				theView.setStatus("Cannot connect to the database. Ensure you have network access, and try again.");
				return;
			}		
		}
	}
	
	class deleteEntry implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			DeleteEntryManager dem = new DeleteEntryManager();
			String primaryKey = null;
			
			try {
				primaryKey = theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString();
			} catch (Exception e1) {
				theView.setStatus("Select a valid row for deletion from the table.");
				return;
			}
			
			int reply = JOptionPane.showConfirmDialog(null,
					"This Ticket will be removed from the database.", "Remove Ticket from Database.",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[4]);
			
			if (reply == 0) {
					theView.setStatus(dem.deleteEntrywithAjax(theView, primaryKey));
			}
									
			theView.clearTable();
			
			try {
				theView.setTable((connectToAPIDatabase.getAllBugs()));
			} catch (Exception e1) {
				log.error("General Exception at mainController.deleteEntry(). " + e1);
			}
		}
	}

	public void loadCheckBoxImages() {
		
		theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));	
		theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));		
		theView.lblDatabase.setIcon(motionlessAjaxLoader);
		theView.lblDatabase.setEnabled(true);
		theView.btnAnalytics.setIcon(new ImageIcon(jlabelImages[5].getImage()));
		
	}

	public void setup() {
		
		int imageDimension = theView.btnScreenshot.getHeight();
		jlabelImages = im.loadCheckBoxImages(imageDimension);
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());

		//OS = OperatingSystemEnvironment.getOperatingSystem();
		//documentReaderCMD = OperatingSystemEnvironment.pdfReaderCMD();
		
		String downloadedFilesStoredInThisDirectory = System.getProperty("user.dir") + "\\Downloaded_Files";
		File directory = new File(downloadedFilesStoredInThisDirectory);
		
	    if (!directory.exists()){
	        directory.mkdir();
	    }   
	}
	
	public void DisplayMessageInJOptionPane(String title, String message) {
		JOptionPane.showMessageDialog(theView, title, message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	class viewInHTML implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {

			ViewInHTMLManager htmlm = new ViewInHTMLManager();		
			String id = "";
			
			try {
				id = (theView.table.getModel().getValueAt(theView.table.getSelectedRow(), 0).toString());
			} catch (Exception e1) {
				theView.setStatus("Select a valid row to view in HTML format from the table.");
				return;
			}
			int bugFromTableId = Integer.parseInt(id);
			ArrayList<Bug> bugFromTable = theView.getBugFromTable();			
			htmlm.dispalyInHTML(theView, bugFromTable, bugFromTableId);
		}
	}

	class getScreenshotPath implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			AbstractButton abstractButton = (AbstractButton) e.getSource();
			boolean screenshotIsSelected = abstractButton.getModel().isSelected();
			
			if (screenshotIsSelected) {
				try {
					
					fileInfo = filesController.selectBufferedImage(theView);
					
					if (fileInfo[0] == null) {
						DisplayMessageInJOptionPane("No Screenshot file has been selected.", "No Selected File.");
						theView.chkUploadScreenshot.setSelected(false);
						filesChanged[0] = 0;
						return;
					}
										
					String fileExtension = FilenameUtils.getExtension(fileInfo[0]);
					
					if((!fileExtension.equals("png")) && (!fileExtension.equals("jpg"))){
						DisplayMessageInJOptionPane("Please select a .png or .jpg screenshot file.", "Invalid File Selected.");
						theView.chkUploadScreenshot.setSelected(false);
						filesChanged[0] = 0;
						return;
					}
					
					int reply = JOptionPane.showConfirmDialog(null,
							"Do you wish to extract the text from this screenshot?", "Extract Text from Image.",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, jlabelImages[7]);
					if (reply == 0) {
						theView.setStatus("Extracting Text from Image.");
						ImageToTextManager itt = new ImageToTextManager();
						theView.descriptionArea.setText(itt.imageToTextwithAjax(theView, fileInfo[0]));
						theView.setStatus("Finished Extracting Text from Image.");
					}
							
					filesChanged[0] = 1;				
					screenshotPath[0] = fileInfo[0];
					screenshotPath[1] = fileInfo[1]; 
				} catch (NumberFormatException ex) {
		        	theView.setStatus("A valid image file needs to be seleceted.");
					log.error("General Exception at mainController.getScreenshotPath(). " + e);
				}
			}
		}
	}
	
	class getDocumentPath implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			AbstractButton abstractButton = (AbstractButton) e.getSource();
			boolean documentIsSelected = abstractButton.getModel().isSelected();
			
			if (documentIsSelected) {				
				try {
					fileInfo = filesController.selectBufferedImage(theView);
					if(fileInfo[0] == null){
						DisplayMessageInJOptionPane("No Document selected.", "No Files Selected.");
						theView.chkUploadDocument.setSelected(false);
						filesChanged[1] = 0;
						return;
					}
					
					String fileExtension = FilenameUtils.getExtension(fileInfo[0]);
					if(!fileExtension.equals("pdf") && (!fileExtension.equals("PDF"))) {
						DisplayMessageInJOptionPane("Please Select a .PDF Extension document File.", "Invalid File Selected.");
						theView.chkUploadDocument.setSelected(false);
						filesChanged[1] = 0;
						return;
					}
						
					filesChanged[1] = 1;
					documentPath[0] = fileInfo[0]; 
					documentPath[1] = fileInfo[1]; 
				} catch (NumberFormatException ex) {
					theView.setStatus("A valid pdf file needs to be seleceted.");
					log.error("General Exception at mainController.getDocumentPath(). " + e);
				}
			}
		}
	}
	
	public class clearFields implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			try {

				ClearTableManager ctm = new ClearTableManager();
				ctm.emptyTextFields(theView);

			} catch (Exception ex) {
				theView.setStatus("Cannot clear text area. Please try again.");
				log.error("General Exception at mainController.emptyFields(). " + e);
			}
		}
	}
	
	
	public boolean checkIfFileExists(String fileDirectory) {
		
		File f = new File(fileDirectory);

		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		
		return false;
	}
	
}
