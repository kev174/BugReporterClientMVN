package com.nuigfyp.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.nuigfyp.view.bugReporterView;

public class filesController {

	public static String[] fileInfo = new String[2];
	private final static Logger log = Logger.getLogger(filesController.class);
	private static bugReporterView theView;

	public static String[] selectBufferedImage(bugReporterView theView) {

		// returns the Directory of the file along with its name in the fileInfo[]
		String root = getRoot();
		JFileChooser fc = new JFileChooser(root);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, PNG and JPG FILES", "pdf", "png", "jpg");
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

		String replaceSlashesInDir = fc.getSelectedFile().getAbsolutePath().replace("\\", "\\\\");
		fileInfo[0] = replaceSlashesInDir;
		fileInfo[1] = fc.getSelectedFile().getName();
		
		//System.out.println("File Extension of selected file is : " + FilenameUtils.getExtension(fileInfo[0])); // import commons-io-jar
		
		// Read Text From Image.
		return fileInfo;
	}

	private static String getRoot() {
		String OS = System.getProperty("os.name").toLowerCase();
		String root = "/usr/local/";

		if (OS.indexOf("win") >= 0) {
			root = "C:\\Users\\kevin\\Desktop\\AWS_Images_Files\\";
		} else {
			root = "/home/pictures/";
		}

		return root;
	}

	// DELETE THIS
	
	// This Method reads a file given a full directory such as "/usr/local/share/SmtProperties/properties.txt"
	// Might have to replace "\\" with "\". use .getAbsolutePath. Or root = "C:\\Users\\i348229\\Pictures\\";
	// where root is a method above and returns a Windows or Linux directory.
	public static String getdatabase_URIInfo() throws IOException {
		
		// REMEBER to close file when finished reading or writing
		// InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("show-all-bugs-blue.png");
		
		File file = new File("resources/database_URI.txt"); 
		String directory = file.getAbsolutePath();
		// database_URI absolute path is C:\Development\Eclipse\Oxygen_WorkSpace\ConnectToBugAPI_Rev1\resources\database_URI.txt
		// System.out.println("database_URI absolute path is " + directory);
		// PASS absolutePath to the variable 'directory' below and you can now read two lines of the file.
		
		try {
			String absolutePath = new File(filesController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		String line = "";

		try {

			InputStream fis = new FileInputStream(directory);
			OutputStream fos = new FileOutputStream(directory);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			// bw.newLine(); // deletes first line and writes then to next line below
			
			bw.write("Hello World Kevin........");
			bw.close();
			
			line = br.readLine();
			br.close();

		} catch (IOException e) {
			log.error("IOException at InstallationManager.getSmtDirectory(). Cannot get find the file "
					+ directory + ". " + e);
			throw e;
		} catch (Exception e) {
			log.error("General Exception at InstallationManager.getSmtDirectory(). "
					+ e);
			throw e;
		}

		//System.out.println("database_URI contains " + line);
		return line;
	}
}