package com.nuigfyp.services;

import java.awt.BorderLayout;
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
import com.nuigfyp.view.bugReporterView;

public class SearchDBManager {

	private final static Logger log = Logger.getLogger(SearchDBManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	
	
	public ArrayList<Bug> searchDBwithAjax(final bugReporterView theView, final String filterId) {

		// === this is going to be the dimension of the two pdf, screenshot JButtons
		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		final ArrayList<Bug> returnBugList = new ArrayList<Bug>();
		connectToAPIDatabase = new ConnectToAPIDatabase();
		
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {
			
				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);
				
				try {	
					
					ArrayList<Bug> bugList = (connectToAPIDatabase.getAllBugs());	
					
					for (Bug bug : bugList) {
						if (bug.getReporterName().equalsIgnoreCase(filterId)) {
							returnBugList.add(bug);
						}
					}

					if (returnBugList.isEmpty()) {
						theView.setStatus(filterId + " is not a valid search criteria.");
						DisplayMessageInJOptionPane(filterId + " is not a valid search criteria.",
								"Name does not exist in the Database.");
						return null; 
					};
					
				} catch (Exception ex) {				
					theView.setStatus("Error occured while searching the database. Please try again.");
					log.error("General Exception at SearchDBManager.searchDBwithAjax(). " + filterId + " is not a valid search criteria. " + ex);
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
			log.error("General Exception at SearchDBManager.searchDBwithAjax(). " + e);
			e.printStackTrace();
		}
		
		return returnBugList;
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
