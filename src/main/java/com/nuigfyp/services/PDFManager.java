package com.nuigfyp.services;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
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
import com.nuigfyp.model.MouseListenerClass;
import com.nuigfyp.model.OperatingSystemEnvironment;
import com.nuigfyp.view.bugReporterView;

public class PDFManager {

	private final static Logger log = Logger.getLogger(PDFManager.class);
	private final static String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files";
	private final static String DOWNLOADED_FILES = System.getProperty("user.dir") + "\\Downloaded_Files";
	private ImageIcon ajaxLoader = null, motionlessAjaxLoader = null;
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private static bugReporterView theView;
	private ImagesManager imagesManager = new ImagesManager();
	
	
	public void viewPDFwithAjax(final bugReporterView theView, final int bugFromTableId, final ArrayList<Bug> bugArraylistFromTable) {

		int imageDimension = theView.btnScreenshot.getHeight(); // 157
		jlabelImages = imagesManager.loadCheckBoxImages(imageDimension);
		ajaxLoader = new ImageIcon(jlabelImages[2].getImage());
		motionlessAjaxLoader = new ImageIcon(jlabelImages[3].getImage());
		final JDialog loading = loadingModel();
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			protected String doInBackground() throws InterruptedException {

				theView.lblDatabase.setIcon(ajaxLoader);
				theView.lblDatabase.setEnabled(true);

				try {

					String openPDFCmd = "";
					String OS = OperatingSystemEnvironment.getOperatingSystem();
					String documentReaderCMD = OperatingSystemEnvironment.pdfReaderCMD();				
					Bug searchedBug = new Bug();

					for (Bug bug : bugArraylistFromTable) {
						if (bug.getId() == bugFromTableId) {
							searchedBug = bug;
							break;
						}
					}

					String pdfDir = searchedBug.getDocument();
					File documentFile = new File(pdfDir);

					if (!pdfDir.equals("No") && (!checkIfPDFFileExists(DOWNLOADED_FILES + "\\" + documentFile.getName()))) {

						try {
							ConnectToAPIDatabase.GETRequest(documentFile.getName());
						} catch (Exception e) {
							log.error("General Exception at PDFManager.viewPDFwithAjax(). " + e);
						}
					}

					new MouseListenerClass(theView).mouseClicked(null);				
			
					if (OS.equals("Windows")) {
						openPDFCmd = (documentReaderCMD + DOWNLOADED_FILES_DIRECTORY + "\\" + documentFile.getName());
					} else if (OS.equals("Linux")) {
						openPDFCmd = (documentReaderCMD + DOWNLOADED_FILES_DIRECTORY + "/" + documentFile.getName());
					} else {
						DisplayMessageInJOptionPane(
								"PDFManager.viewPDFwithAjax(): You can only view files on Windows and Linux Operating Systems.",
								"Not a valid Operating System.");
					}

					try {
						Runtime.getRuntime().exec(openPDFCmd);
					} catch (IOException e) {
					}

				} catch (Exception ex) {
					DisplayMessageInJOptionPane(
							"PDFManager.viewPDFwithAjax(): You have not selected an appropiate ID from the Table." + ex,
							"Please highlight a Valid Item.");
					log.error("General Exception at PDFManager.viewPDFwithAjax(). " + ex);
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
			log.error("General Exception at PDFManager.viewPDFwithAjax(). " + e);
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
	
	
	public boolean checkIfPDFFileExists(String pdfDirectory) {

		File f = new File(pdfDirectory);

		if (f.exists() && !f.isDirectory()) {
			return true;
		}

		return false;
	}

}
