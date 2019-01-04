package com.nuigfyp.services;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.view.bugReporterView;

public class DeleteEntryManager {

	private final static Logger log = Logger.getLogger(AddEntryManager.class);
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	private String returnConnectionString = "";
	
	
	public String deleteEntrywithAjax(final bugReporterView theView, final String primaryKey) {
		
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
				
				// ==== INNER CLASS BELOW, SO HAVE TO USE COPY OF VARIABLES. Error make final or effectively final ====										
				try {
					
					returnConnectionString = connectToAPIDatabase.deleteEntry(primaryKey);
					System.out.println("retruned " + returnConnectionString);

				} catch (ArrayIndexOutOfBoundsException | SQLException oob) {
					DisplayMessageInJOptionPane("Select valid item from Table", "Select Valid Item.");
					log.error("ArrayIndexOutOfBoundsException at mainController.deleteEntrywithAjax(). " + oob);
					returnConnectionString = ("Failed to delete item id: " + primaryKey + " from the database.");
				} catch (Exception e) {
					returnConnectionString = ("Cannot connect to the Database.");
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
		} catch (Exception e) {
			log.error("General Exception at DeleteEntryManager.deleteEntrywithAjax(). " + e);
			e.printStackTrace();
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
