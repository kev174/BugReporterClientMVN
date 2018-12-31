package com.nuigfyp.services;

import com.nuigfyp.view.bugReporterView;

public class ClearTableManager {

	public void emptyTextFields(bugReporterView theView) {
		
		theView.chkUploadScreenshot.setSelected(false);
		theView.chkUploadDocument.setSelected(false);
		theView.btnPDF.setEnabled(false);
		theView.btnScreenshot.setEnabled(false);
		theView.projectField.setSelectedIndex(0);
		theView.severityField.setSelectedIndex(0);
		theView.recorderField.setText("");
		theView.testerField.setText("");
		theView.descriptionArea.setText("");
		theView.searchField.setText("");
		
	}
}
