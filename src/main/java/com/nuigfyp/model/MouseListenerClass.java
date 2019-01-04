package com.nuigfyp.model;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.nuigfyp.services.ImagesManager;
import com.nuigfyp.view.bugReporterView;

import org.apache.log4j.Logger;

//The working code uses the pdfbox-app-2.0.11.jar(8mb) file. Remove the pdf-renderer.jar as not used
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class MouseListenerClass implements MouseListener {

	private final static Logger log = Logger.getLogger(MouseListenerClass.class);
	private final static String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files";
	private final static String DOWNLOADED_FILES = System.getProperty("user.dir") + "\\Downloaded_Files";
	private Image resizedScreenshotImage = null, bpi = null;
	private ImagesManager im = new ImagesManager();
	private ImageIcon[] jlabelImages = new ImageIcon[5];
	private bugReporterView theView;
	private ArrayList<Bug> bugListFromJTable;

	// Constructor
	public MouseListenerClass(bugReporterView theView) {
		this.theView = theView;
		setup();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		bugListFromJTable = theView.getBugFromTable();
		int row = theView.table.getSelectedRow();
		Bug bug = new Bug(); // reduce to one line of code
		bug = bugListFromJTable.get(row);

		theView.recorderField.setText(bug.getReporterName());
		theView.testerField.setText(bug.getTesterName());
		theView.descriptionArea.setText(bug.getDescription());
		int severityIndex = bug.getSeverity();
		theView.severityField.setSelectedIndex(--severityIndex);
		int projectIndex = bug.getProject();
		theView.projectField.setSelectedIndex(--projectIndex);
		int classificatioErrorIndex = bug.getBugClassification();
		theView.classificationField.setSelectedIndex(--classificatioErrorIndex);

		File screenFile = new File(bug.getScreenshot());
		String screenshotFileName = screenFile.getName();

		// Screenshot: displaying the Screenshot on the JButton

		if (((Bug) theView.list.get(row)).getScreenshot().equals("No")) {
			System.out.println("Screenshot set in <Array Bug> is No, so image should not be in focus. "
					+ ((Bug) theView.list.get(row)).getScreenshot());
			theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));
			theView.btnScreenshot.setEnabled(false);
			theView.chkUploadScreenshot.setSelected(false);
			theView.screenshotTableEqualsYes = false;
		} else {
			theView.btnScreenshot.setEnabled(true); // Screenshot JLabel is visible
			theView.chkUploadScreenshot.setSelected(true);
			theView.screenshotTableEqualsYes = true;

			System.out.println("the Screenshot file named " + screenshotFileName + ", Exist? "
					+ checkIfFileExists(DOWNLOADED_FILES + "\\" + screenshotFileName));

			// CALL METHOD TO DISPLAY THE IMAGE STORED LOCALLY ON THE LOCALMACHINE
			// Reason for two different Downloaded constants: 1st: Requires one forward
			// slash. 2nd: two back slashes
			if (checkIfFileExists(DOWNLOADED_FILES + "\\" + screenshotFileName)) {

				File file = new File(DOWNLOADED_FILES_DIRECTORY + "\\" + screenshotFileName);
				BufferedImage bi = null;

				try {

					InputStream input = new FileInputStream(file);
					bi = ImageIO.read(input);

				} catch (Exception ex) {
					if (ex instanceof FileNotFoundException || ex instanceof IOException) {
						theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));
						log.error("FileNotFoundException, IOException at MouseListenerClass.mouseClicked(). " + ex);
					}
				}

				ImageIcon imageIcon = new ImageIcon(bi);
				int height = theView.btnScreenshot.getHeight();
				int width = theView.btnScreenshot.getWidth();
				bpi = imageIcon.getImage();
				resizedScreenshotImage = bpi.getScaledInstance(height, width, Image.SCALE_SMOOTH);
				theView.btnScreenshot.setIcon(new ImageIcon(resizedScreenshotImage));
				theView.btnScreenshot.setEnabled(true);

			} else {
				theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));
			}
		}

		// PDF: displaying the first page on the JButton
		File pdfFile = new File(bug.getDocument());
		String pdfFileName = pdfFile.getName();

		if (((Bug) theView.list.get(row)).getDocument().equals("No")) {
			theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));
			theView.btnPDF.setEnabled(false);
			theView.chkUploadDocument.setSelected(false);
			theView.documentTableEqualsYes = false;
		} else {
			theView.btnPDF.setEnabled(true);
			theView.chkUploadDocument.setSelected(true);
			theView.documentTableEqualsYes = true;

			System.out.println("the PDF file named " + pdfFileName + ", Exist? "
					+ checkIfFileExists(DOWNLOADED_FILES + "\\" + pdfFile.getName()));

			if (checkIfFileExists(DOWNLOADED_FILES + "\\" + pdfFileName)) {

				File file = new File(DOWNLOADED_FILES_DIRECTORY + "\\" + pdfFileName);
				BufferedImage image = null;
				PDDocument document = null;

				try {
					// Enables rendering of the PDF file faster
					System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
					document = PDDocument.load(file);
					PDFRenderer renderer = new PDFRenderer(document);
					image = renderer.renderImage(0); // page number 0

				} catch (Exception ex) {
					if (ex instanceof FileNotFoundException || ex instanceof IOException) {
						theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));
						log.error("FileNotFoundException, IOException at MouseListenerClass.mouseClicked(). " + ex);
					}
					theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));
				} finally {
					try {
						document.close();
					} catch (IOException ioe) {
						log.error("General Exception at MouseListenerClass.mouseClicked(). " + ioe);
						ioe.printStackTrace();
					}
				}

				ImageIcon imageIcon = new ImageIcon(image);
				int height = theView.btnScreenshot.getHeight();
				int width = theView.btnScreenshot.getWidth();
				bpi = imageIcon.getImage();
				resizedScreenshotImage = bpi.getScaledInstance(height, width, Image.SCALE_SMOOTH);
				theView.btnPDF.setIcon(new ImageIcon(resizedScreenshotImage));
				theView.btnPDF.setEnabled(true);

			} else {
				theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));
			}
		}

		// Removes image labels and Change Status Button (grayed out)
		if (theView.list.get(row).getActive() == 0) {
			// If ticket is closed then the screenshot and pdf label gets the default image
			/*theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));
			theView.btnPDF.setIcon(new ImageIcon(jlabelImages[1].getImage()));*/
			theView.btnActive.setEnabled(false);
			theView.btnScreenshot.setEnabled(false);
			theView.btnPDF.setEnabled(false);
		}
		/*if (theView.list.get(row).getActive() == 1) {
			theView.btnActive.setEnabled(true);
			theView.btnScreenshot.setEnabled(true);
			theView.btnPDF.setEnabled(true);
		}*/
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public boolean checkIfFileExists(String screenshotFileDirectory) {

		File f = new File(screenshotFileDirectory);

		if (f.exists() && !f.isDirectory()) {
			return true;
		}

		return false;
	}

	public void setup() {

		int imageDimension = theView.btnScreenshot.getHeight();
		jlabelImages = im.loadCheckBoxImages(imageDimension);

	}
}
