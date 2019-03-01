package com.nuigfyp.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.nuigfyp.model.Bug;
import com.nuigfyp.services.ClearTableManager;
import com.nuigfyp.services.ImagesManager;


@SuppressWarnings("serial")
public class bugReporterView extends JFrame {

	private JPanel contentPane, tableJPanel, dbPanel, dataEntryPanel, dataViewPanel;
	public JTextField recorderField, testerField, searchField; 
	public JTextArea descriptionArea;
	private JLabel lblSeverityNum, lblTesterName, lblDescription, lblRecorderName, lblProjectId, lblClassification, lblStatus, lblSearch;
	public JLabel lblDatabase;
	private JScrollPane jTableScrollPane, descriptionScrollPane;
	public JTable table;
	private Color lightBlue = new Color(153,230,255), creamColor = new Color(242, 230, 217), 
			whiteColor = Color.white, greenColor = new Color(204, 229, 128); 
	private DefaultTableModel model;
	public ImageIcon resizedCheckImageIcon, resizedRedXImageIcon;
	private ImageIcon[] resizedJTableImages = new ImageIcon[2];
	private Image[] buttonImages = new Image[8];
	private String[] companyNameIDs = { "(1) SAP", "(2) NUIG", "(3) Ericsson", "(4) Medtronic", "(5) HP" };
	public JButton btnScreenshot, btnPDF, btnAnalytics; 
	
	private JButton btnUpdateDB, btnDeleteEntry, btnAddToDB, btnClearTable, btnConnDB, btnSearch, btnShowHTMLFormat, btnEmptyFields;
	public JButton btnActive;
	public JCheckBox chkUploadScreenshot, chkUploadDocument;
	@SuppressWarnings("rawtypes")
	public JComboBox projectField, severityField, classificationField;
	private GroupLayout glTableJPanel;
	public ArrayList<Bug> list;
	private Border raisedBorder;
	public boolean screenshotTableEqualsYes = false;
	public boolean documentTableEqualsYes = false;
	private final int buttonWidth = 168, buttonHeight = 38;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public bugReporterView(String user) {
		
		imagesSetup();	
		setIconImage(buttonImages[8]);
		setTitle("Bug Reporter:- User: " + user);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 595);	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(whiteColor); 
		raisedBorder = BorderFactory.createRaisedBevelBorder();

		dataEntryPanel = new JPanel();				
		dataEntryPanel.setBounds(6, 5, 813, 153); 	
		dataEntryPanel.setBorder(raisedBorder);
		dataEntryPanel.setBackground(lightBlue); 
		dataEntryPanel.setLayout(null);
		contentPane.add(dataEntryPanel);			
		
		lblRecorderName = new JLabel("Recorders Name");
		lblRecorderName.setBounds(15, 7, 117, 20);
		dataEntryPanel.add(lblRecorderName);
		
		lblProjectId = new JLabel("Project ID");
		lblProjectId.setBounds(15, 35, 117, 20);
		dataEntryPanel.add(lblProjectId);		

		lblClassification = new JLabel("Classification Error");
		lblClassification.setBounds(15, 64, 117, 20);
		dataEntryPanel.add(lblClassification);		
		
		lblSeverityNum = new JLabel("Severity");
		lblSeverityNum.setBounds(15, 93, 117, 20);
		dataEntryPanel.add(lblSeverityNum);		
			
		lblTesterName = new JLabel("Tester Name");
		lblTesterName.setBounds(15, 122, 117, 20);
		dataEntryPanel.add(lblTesterName);		

		lblDescription = new JLabel("Description:");
		lblDescription.setBounds(280, 7, 110, 20);
		dataEntryPanel.add(lblDescription);
		
		lblSearch = new JLabel("Search by Recorders Name");
		lblSearch.setBounds(385, 7, 170, 20);
		dataEntryPanel.add(lblSearch);	
		
		recorderField = new JTextField();
		recorderField.setColumns(10);
		recorderField.setBounds(140, 7, 121, 20);
		dataEntryPanel.add(recorderField);
		
		projectField = new JComboBox();
		projectField.setModel(new DefaultComboBoxModel(companyNameIDs));
		projectField.setBounds(140, 35, 121, 20);
		dataEntryPanel.add(projectField);
		
		classificationField = new JComboBox();
		classificationField.setModel(new DefaultComboBoxModel(new String[] { "Text", "Truncation", "Graphics", "On Click", "Software" }));
		classificationField.setBounds(140, 64, 121, 20);
		dataEntryPanel.add(classificationField);
		
		severityField = new JComboBox();
		severityField.setModel(new DefaultComboBoxModel(new String[] { "1 - Low", "2 - Low/Medium", "3 - Medium", "4 - Medium/High", "5 - High" }));
		severityField.setBounds(140, 93, 121, 20);
		dataEntryPanel.add(severityField);		

		testerField = new JTextField();
		testerField.setColumns(10);
		testerField.setBounds(140, 122, 121, 20);
		dataEntryPanel.add(testerField);	
		
		descriptionArea = new JTextArea();
		descriptionArea.setBorder(raisedBorder);
		descriptionArea.setColumns(10);
		dataEntryPanel.add(descriptionArea);	
		descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(280, 30, 350, 85);
		dataEntryPanel.add(descriptionScrollPane);

		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setBounds(550, 7, 81, 20);
		dataEntryPanel.add(searchField);

		btnSearch = new JButton(new ImageIcon(buttonImages[1]));
		btnSearch.setBounds(640, 5, buttonWidth, 50);
		btnSearch.setBackground(lightBlue);
		btnSearch.setBorder(null);
		dataEntryPanel.add(btnSearch);
		
		btnConnDB = new JButton(new ImageIcon(buttonImages[0]));
		btnConnDB.setBounds(640, 56, buttonWidth, 44);
		btnConnDB.setBackground(lightBlue);
		btnConnDB.setBorder(null);
		dataEntryPanel.add(btnConnDB);				

		btnClearTable = new JButton(new ImageIcon(buttonImages[2]));
		btnClearTable.setBounds(640, 104, buttonWidth, 42);
		btnClearTable.setBackground(lightBlue);
		btnClearTable.setBorder(null);
		dataEntryPanel.add(btnClearTable);
				
		chkUploadDocument = new JCheckBox("Add/Remove Document");		
		chkUploadDocument.setBounds(460, 122, 170, 23);
		chkUploadDocument.setBorderPainted(isEnabled());
		chkUploadDocument.setToolTipText("Uncheck box and click Update to remove the file.");
		dataEntryPanel.add(chkUploadDocument);
		
		chkUploadScreenshot = new JCheckBox("Add/Remove Screenshot");
		chkUploadScreenshot.setBounds(280 , 122, 170, 23 );
		chkUploadScreenshot.setBorderPainted(isEnabled());
		chkUploadScreenshot.setToolTipText("Uncheck box and click Update to remove the file.");
		dataEntryPanel.add(chkUploadScreenshot);
		
		contentPane.add(dataEntryPanel);

		tableJPanel = new JPanel();							
		tableJPanel.setBackground(lightBlue);               
		tableJPanel.setBorder(raisedBorder);
		tableJPanel.setBounds(6, 164, 630, 194); 
		contentPane.add(tableJPanel);

		jTableScrollPane = new JScrollPane();
		glTableJPanel = new GroupLayout(tableJPanel);
		glTableJPanel.setHorizontalGroup(glTableJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(glTableJPanel.createSequentialGroup().addContainerGap()
				.addComponent(jTableScrollPane, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
				.addContainerGap()));
		glTableJPanel.setVerticalGroup(glTableJPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				glTableJPanel.createSequentialGroup().addContainerGap()
				.addComponent(jTableScrollPane, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
				.addContainerGap()));
		
		model = new DefaultTableModel();		
		table = new JTable(model) { 
			
			// If a Row is closed, then the row will have a Light gray Color
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {
					component.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					String type = (String) getModel().getValueAt(modelRow, 1);
					if ("Open".equals(type))
						component.setBackground(Color.GREEN);
					if ("Closed".equals(type))
						component.setBackground(Color.LIGHT_GRAY);
				}

				return component;
			};
			
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			} 
			
			// cells are not editable
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
        
		table.setSelectionBackground(greenColor); // sets selected JTable Row to this color
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Ticket No.", "Status", "Recorder", "Tester", "Severity", "Project ID", "Screenshot", "Document" }));
		jTableScrollPane.setViewportView(table);
		tableJPanel.setLayout(glTableJPanel);
		
		table.setRowHeight(20);
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.setDefaultRenderer(Integer.class, leftRenderer);

		// these four paragraphs can be commented out, and toggle with code below
		dbPanel = new JPanel();
		dbPanel.setLayout(null);
		dbPanel.setBorder(new TitledBorder(null, "Database Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dbPanel.setBounds(642, 163, 177, 195);  	
		dbPanel.setBackground(lightBlue);   		 
		
		btnAddToDB = new JButton(new ImageIcon(buttonImages[3]));
		btnAddToDB.setBounds(4, 20, buttonWidth, buttonHeight);
		btnAddToDB.setBackground(lightBlue);
		btnAddToDB.setBorder(null);
		dbPanel.add(btnAddToDB);

		btnUpdateDB = new JButton(new ImageIcon(buttonImages[4]));
		btnUpdateDB.setBounds(4, 65, buttonWidth, buttonHeight);
		btnUpdateDB.setBackground(lightBlue);
		btnUpdateDB.setBorder(null);
		dbPanel.add(btnUpdateDB);

		btnDeleteEntry = new JButton(new ImageIcon(buttonImages[5]));
		btnDeleteEntry.setBounds(4, 110, buttonWidth, buttonHeight);
		btnDeleteEntry.setBackground(lightBlue);
		btnDeleteEntry.setBorder(null);
		dbPanel.add(btnDeleteEntry);
		
		// Change Status of Active component. Create ActionListener to launch JOptionPane to confirm change status and just update DB to Inactive
		btnActive = new JButton(new ImageIcon(buttonImages[6]));
		btnActive.setBounds(4, 154, buttonWidth, buttonHeight);
		btnActive.setBackground(lightBlue);
		btnActive.setBorder(null);
		dbPanel.add(btnActive);

		contentPane.add(dbPanel);
		//contentPane.add(secondPanel);	
		
		lblStatus = new JLabel("Status: ");
		lblStatus.setBounds(10, 106, 460, 62);
		lblStatus.setFont(new Font("Ariel", Font.BOLD, 16));
		lblStatus.setBorder(raisedBorder);
	
		dataViewPanel = new JPanel();
		dataViewPanel.setLayout(null); 
		dataViewPanel.setBounds(6, 363, 814, 178);   
		dataViewPanel.setBorder(raisedBorder);	
		dataViewPanel.setBackground(creamColor); 
		dataViewPanel.add(lblStatus);	
		contentPane.add(dataViewPanel);
		
		btnScreenshot = new JButton();
		btnScreenshot.setEnabled(false); 
		btnScreenshot.setBorder(raisedBorder);
		btnScreenshot.setBounds(477, 11, 157, 157);
		dataViewPanel.add(btnScreenshot);
		
		btnPDF = new JButton();
		btnPDF.setEnabled(false);
		btnPDF.setBorder(raisedBorder);
		btnPDF.setBounds(643, 11, 157, 157);
		dataViewPanel.add(btnPDF);
			
		btnEmptyFields = new JButton("<html>Clear<br/>TextAreas</html>");
		btnEmptyFields.setBounds(200, 11, 85, 85);
		btnEmptyFields.setFont(new Font("Serif", Font.BOLD, 16));
		btnEmptyFields.setMargin(new Insets(0,-30, 0,-30));
		dataViewPanel.add(btnEmptyFields);
		
		btnAnalytics = new JButton();
		btnAnalytics.setEnabled(true); 
		btnAnalytics.setBorder(raisedBorder);
		btnAnalytics.setBounds(292, 11, 85, 85);
		dataViewPanel.add(btnAnalytics);
		
		lblDatabase = new JLabel();
		lblDatabase.setEnabled(false); 
		lblDatabase.setBorder(raisedBorder);
		lblDatabase.setBounds(385, 11, 85, 85);
		lblDatabase.setForeground(Color.PINK);
		dataViewPanel.add(lblDatabase);

		btnShowHTMLFormat = new JButton(new ImageIcon(buttonImages[7])); 
		btnShowHTMLFormat.setBounds(10, 11, 184, 85);
		btnShowHTMLFormat.setBorder(null);
		btnShowHTMLFormat.setContentAreaFilled(false);
		dataViewPanel.add(btnShowHTMLFormat);
		
		btnClearTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearTable();
			}
		});
	}
		
	@SuppressWarnings({"rawtypes", "unchecked" })
	public void setTable(ArrayList tableList) {

		Object rowData[] = new Object[8];
		clearTable();	
		list = new ArrayList<Bug>(tableList);
		
		if(!list.isEmpty()) {
			btnAnalytics.setEnabled(true);
		} else {
			btnAnalytics.setEnabled(false);
		}
	
		for (int i = 0; i < tableList.size(); i++) { 
			rowData[0] = ((Bug) tableList.get(i)).getId();			
			rowData[1] = (((Bug) tableList.get(i)).getActive() == 1) ? "Open" : "Closed";			
			rowData[2] = ((Bug) tableList.get(i)).getReporterName();
			rowData[3] = ((Bug) tableList.get(i)).getTesterName();	
			rowData[4] = ((Bug) tableList.get(i)).getSeverity();
			int companyIndex = ((Bug) tableList.get(i)).getProject();	
			rowData[5] = companyNameIDs[--companyIndex];	
			rowData[6] = ((Bug) tableList.get(i)).getScreenshot().equals("No") ? resizedJTableImages[0] : resizedJTableImages[1];
			rowData[7] = ((Bug) tableList.get(i)).getDocument().equals("No") ? resizedJTableImages[0] : resizedJTableImages[1];	
			((DefaultTableModel) table.getModel()).addRow(rowData);	
		}	
	}
	
	public boolean screenshotTableEqualsYes() {		
		return screenshotTableEqualsYes; 
	}
	
	public boolean documentTableEqualsYes() {		
		return documentTableEqualsYes; 
	}
	
	public String getSearchID() {
		return searchField.getText(); 
	}

	public int getSeverityIndex() {
		int severity = severityField.getSelectedIndex();
		return ++severity;
	}
	
	public ArrayList<Bug> getBugFromTable() {	
		return list;
	}

	public void setStatus(String message) {
		lblStatus.setText("<html>Status: " + message + "</html>");
	}

	public void clearTable() {
		
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		while (dm.getRowCount() > 0) {
			dm.removeRow(0);
		}
		
		ClearTableManager ctm = new ClearTableManager();
		ctm.emptyTextFields(this);
	}

	// ACTION LISTENERS for JButtons within this class 
	public void addJTableListener(MouseListener mseClickedListener) {
		table.addMouseListener(mseClickedListener);
	}
	
	public void viewAnalytics(ActionListener analyticsBtnListener) {
		btnAnalytics.addActionListener(analyticsBtnListener);
	}
	
	public void viewScreenshot(ActionListener screenshotBtnListener) {
		btnScreenshot.addActionListener(screenshotBtnListener);
	}
	
	public void viewPdf(ActionListener pdfBtnListener) {
		btnPDF.addActionListener(pdfBtnListener);
	}
	
	public void viewInHTMLFormat(ActionListener listenForDocumentUploadBtn) {
		btnShowHTMLFormat.addActionListener(listenForDocumentUploadBtn);
	}
	
	public void uploadDocument(ActionListener listenForDocumentUploadBtn) {
		chkUploadDocument.addActionListener(listenForDocumentUploadBtn);
	}
	
	public void uploadScreenshot(ActionListener listenForScreenshotUploadBtn) {
		chkUploadScreenshot.addActionListener(listenForScreenshotUploadBtn);
	}
	
	public void addConnectToDB(ActionListener listenForAddBtn) {
		btnConnDB.addActionListener(listenForAddBtn);
	}

	public void addEntryToDB(ActionListener listenForEntryBtn) {
		btnAddToDB.addActionListener(listenForEntryBtn);
	}

	public void addUpdateDB(ActionListener listenForUpdateBtn) {
		btnUpdateDB.addActionListener(listenForUpdateBtn);
	}

	public void addDeleteFromDB(ActionListener listenForDeleteBtn) { 
		btnDeleteEntry.addActionListener(listenForDeleteBtn);
	}
	
	public void addStatusChangeInDB(ActionListener listenForDeleteBtn) { 
		btnActive.addActionListener(listenForDeleteBtn);
	}

	public void addSearchDB(ActionListener listenForSearchBtn) {
		btnSearch.addActionListener(listenForSearchBtn);
	}
	
	public void addEmptyFields(ActionListener listenForSearchBtn) {
		btnEmptyFields.addActionListener(listenForSearchBtn);
	}
	
	public void imagesSetup() {		
		ImagesManager im = new ImagesManager();
		buttonImages = im.buttonImages();		
		resizedJTableImages = im.jtableImages();
	}
	
}