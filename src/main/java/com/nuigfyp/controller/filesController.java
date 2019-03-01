package com.nuigfyp.controller;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import com.nuigfyp.view.bugReporterView;

public class filesController {

	public static String[] fileInfo = new String[2];
	private final static Logger log = Logger.getLogger(filesController.class);

	public static String[] selectBufferedImage(bugReporterView theView) {

		String root = getRoot();
		JFileChooser fc = new JFileChooser(root);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, PNG and JPG FILES", "pdf", "png", "jpg", "PDF", "PNG", "JPG");
		fc.setFileFilter(filter);
		fc.setDialogTitle("Browse BugReporter File.");
		int result = fc.showOpenDialog(null);

		try {
			if (result == JFileChooser.APPROVE_OPTION) {
			} else {
				fileInfo[0] = null;
				fileInfo[1] = null;
				return fileInfo;
			}
		} catch (Exception e) {
			log.error("General Exception at selectBufferedImage(). " + e);
		}

		String replaceBackSlashesInDir = fc.getSelectedFile().getAbsolutePath().replace("\\", "\\\\");
		fileInfo[0] = replaceBackSlashesInDir;
		fileInfo[1] = fc.getSelectedFile().getName();
				
		return fileInfo;
	}

	
	private static String getRoot() {
		String OS = System.getProperty("os.name").toLowerCase();
		String root = "/usr/local/";

		if (OS.indexOf("win") >= 0) {
			root = "/usr/local/";
		} else {
			root = "/home/pictures/";
		}

		return root;
	}

}