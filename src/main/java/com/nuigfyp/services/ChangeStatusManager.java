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
import com.nuigfyp.view.bugReporterView;

public class ChangeStatusManager {
	
	private final static Logger log = Logger.getLogger(UpdateDBManager.class);
	//private final static String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files";
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	private ConnectToAPIDatabase connectToAPIDatabase;
	
	public String changeStatusWithAjax(final bugReporterView theView, final int id) {

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
	
				try {
					theView.setStatus(connectToAPIDatabase.changeStatusInDB(Integer.toString(id)));
				} catch (Exception e) {
					DisplayMessageInJOptionPane(
							"mainController.changeStatusWithAjax(); Exception caught, possibly due to the following...\\nYou have not selected an appropiate item from the Table.\nCheck if this ID exists in DB.",
							"Please select a valid item.");
					theView.setStatus("UpdateDBManagr.changeStatusWithAjax(): Multiple possible exceptions.");
					log.error("General Exception at ChangeStatusManager.changeStatusWithAjax():. " + e);
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
		
		return "Status Changed successfully.";
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
