package com.nuigfyp.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

@SuppressWarnings("serial")
public class pdfViewer extends JFrame {

	public pdfViewer(String filePath) {
		
		JFrame applicationFrame = new JFrame();
		applicationFrame.setTitle("Display PDF.");
		applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SwingController controller = new SwingController();
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		applicationFrame.getContentPane().add(viewerComponentPanel);		
		applicationFrame.add(viewerComponentPanel);
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));		
		controller.openDocument(filePath);
		applicationFrame.pack();
		applicationFrame.setVisible(true);
	}
}