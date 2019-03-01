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


	public MouseListenerClass(bugReporterView theView) {
		this.theView = theView;
		setup();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		bugListFromJTable = theView.getBugFromTable();
		int row = theView.table.getSelectedRow();
		Bug bug = new Bug();
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
			theView.btnScreenshot.setIcon(new ImageIcon(jlabelImages[0].getImage()));
			theView.btnScreenshot.setEnabled(false);
			theView.chkUploadScreenshot.setSelected(false);
			theView.screenshotTableEqualsYes = false;
		} else {
			theView.btnScreenshot.setEnabled(true); // Screenshot JLabel is visible
			theView.chkUploadScreenshot.setSelected(true);
			theView.screenshotTableEqualsYes = true;

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
				theView.btnScreenshot.setText("Click to Download File.");
				System.out.println("file deos not exist download it");
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

			if (checkIfFileExists(DOWNLOADED_FILES + "\\" + pdfFileName)) {

				File file = new File(DOWNLOADED_FILES_DIRECTORY + "\\" + pdfFileName);
				BufferedImage image = null;
				PDDocument document = null;

				try {
					// Enables rendering of the PDF file faster
					System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
					document = PDDocument.load(file);
					PDFRenderer renderer = new PDFRenderer(document);
					image = renderer.renderImage(0); // PDF page number 0

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

		theView.btnActive.setEnabled(true);

		if (theView.list.get(row).getActive() == 0) {
			theView.btnActive.setEnabled(false);
			theView.btnScreenshot.setEnabled(false);
			theView.btnPDF.setEnabled(false);
		}

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
