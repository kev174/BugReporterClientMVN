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

	// https://www.w3schools.com/colors/colors_names.asp
	// www.iconfinder.com     http://buttonoptimizer.com/  (can add Image to it
	// https://www.flaticon.com/free-icons/database	
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
			whiteColor = Color.white, greenColor = new Color(204, 229, 128); // lighter blue 153,230,255
	private DefaultTableModel model;
	public ImageIcon resizedCheckImageIcon, resizedRedXImageIcon;
	private ImageIcon[] resizedJTableImages = new ImageIcon[2];
	private Image[] buttonImages = new Image[8];
	private String[] companyNameIDs = { "(1) SAP", "(2) NUIG", "(3) Ericsson", "(4) Medtronic", "(5) HP" };
	public JButton btnScreenshot, btnPDF, btnAnalytics; // Changed from private to public for JLabel to JButton
	
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
	//secondDBPanel secondPanel = new secondDBPanel();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public bugReporterView() {
		
		imagesSetup();	
		setIconImage(buttonImages[7]);
		setTitle("Connect To Bug API Updating GUI - MAVEN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 595);	// setBounds(100, 100, 819, 584); increased by 21px
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(whiteColor); // main background color (new Color(255, 218, 185)); light Pink
		raisedBorder = BorderFactory.createRaisedBevelBorder();

		dataEntryPanel = new JPanel();				// Top Panel
		dataEntryPanel.setBounds(6, 5, 813, 153); 	// increased x by 20
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
		dataEntryPanel.add(lblClassification);		// lblClassification
		
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

		//btnSearch = new JButton(new ImageIcon(buttonImages[1]));
		btnSearch = new JButton("Search");
		btnSearch.setBounds(640, 5, buttonWidth, 23);
		btnSearch.setIcon(new ImageIcon(buttonImages[1]));
		btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
		//btnSearch.setBorder(null);
		//btnSearch.setContentAreaFilled(false);
		dataEntryPanel.add(btnSearch);
		
		//btnConnDB = new JButton(new ImageIcon(buttonImages[0]));
		btnConnDB = new JButton("Show Bugs");
		btnConnDB.setBounds(640, 58, buttonWidth, 44);
		btnConnDB.setIcon(new ImageIcon(buttonImages[0]));
		btnConnDB.setHorizontalAlignment(SwingConstants.LEFT);
		btnConnDB.setBackground(greenColor);
		//btnConnDB.setBorder(null);
		//btnConnDB.setMargin(new Insets(0, 0, 0, 0)); // works without this
		//btnConnDB.setContentAreaFilled(false);
		dataEntryPanel.add(btnConnDB);				

		btnClearTable = new JButton("Clear Table");
		//btnClearTable = new JButton(new ImageIcon(buttonImages[2]));
		btnClearTable.setBounds(640, 104, buttonWidth, 42);
		btnClearTable.setIcon(new ImageIcon(buttonImages[2]));
		btnClearTable.setHorizontalAlignment(SwingConstants.LEFT);
		//btnClearTable.setBorder(null);
		//btnClearTable.setContentAreaFilled(false);
		//btnClearTable.setMnemonic(KeyEvent.VK_C);      // add Mnemonic to clear the table
		dataEntryPanel.add(btnClearTable);
				
		chkUploadDocument = new JCheckBox("Upload Document");		
		chkUploadDocument.setBounds(460, 122, 170, 23);
		chkUploadDocument.setBorderPainted(isEnabled());
		chkUploadDocument.setToolTipText("Uncheck box and click Update to remove the file.");
		dataEntryPanel.add(chkUploadDocument);
		
		chkUploadScreenshot = new JCheckBox("Upload Screenshot");
		chkUploadScreenshot.setBounds(280 , 122, 170, 23 );
		chkUploadScreenshot.setBorderPainted(isEnabled());
		chkUploadScreenshot.setToolTipText("Uncheck box and click Update to remove the file.");
		dataEntryPanel.add(chkUploadScreenshot);
		
		contentPane.add(dataEntryPanel);

		tableJPanel = new JPanel();							// Middle panel (JTable Only)
		tableJPanel.setBackground(lightBlue);               // sets JTable to blueColor
		tableJPanel.setBorder(raisedBorder);
		//tableJPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
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
		
		table = new JTable(model) { // allows to add images, without code just path to images
			
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {			
					Component c = super.prepareRenderer(renderer, row, column);

					if (!isRowSelected(row)) {
						c.setBackground(getBackground());
						int modelRow = convertRowIndexToModel(row);
						String type = (String) getModel().getValueAt(modelRow, 1);
						if ("Open".equals(type))
							c.setBackground(Color.GREEN);
						if ("Closed".equals(type))
							c.setBackground(Color.LIGHT_GRAY);
					}
					
					return c;			
			};
			
			// Displays javax.swing... instead of the screenshot and document check and x image
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			} 
			
			// cells are not editable
			public boolean isCellEditable(int row, int column) { // return column == 2;
				return false;
			};
		};
        
		table.setSelectionBackground(greenColor); // sets selected JTable Row to this color
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Ticket No.", "Status", "Recorder", "Tester", "Severity", "Project ID", "Screenshot", "Document" }));
		jTableScrollPane.setViewportView(table);
		tableJPanel.setLayout(glTableJPanel);
		
		table.setRowHeight(20);
		/*table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(4).setPreferredWidth(45);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setPreferredWidth(60);
		table.getColumnModel().getColumn(7).setPreferredWidth(60);*/
		
		// Colors Row to Yellow 
		// Possibly use table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		//table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		
		// Aligns Integers to left of cells
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.setDefaultRenderer(Integer.class, leftRenderer);

		// these four paragraphs can be commented out, and toggle with code below
		dbPanel = new JPanel();
		dbPanel.setLayout(null);
		dbPanel.setBorder(new TitledBorder(null, "Database Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dbPanel.setBounds(642, 163, 177, 195);  	// Middle Panel (database options)
		// dbPanel.setBorder(raisedBorder);			// Database Options disappear when this is active
		dbPanel.setBackground(lightBlue);   		// sets Database JButtons to this color 
		
		//btnAddToDB = new JButton(new ImageIcon(buttonImages[3]));
		btnAddToDB = new JButton("Create Entry");
		btnAddToDB.setBounds(4, 20, buttonWidth, buttonHeight);
		btnAddToDB.setIcon(new ImageIcon(buttonImages[3]));
		btnAddToDB.setHorizontalAlignment(SwingConstants.LEFT);
		//btnAddToDB.setBorder(null);
		//btnAddToDB.setContentAreaFilled(false);
		dbPanel.add(btnAddToDB);

		//btnUpdateDB = new JButton(new ImageIcon(buttonImages[4]));
		btnUpdateDB = new JButton("Update Entry");
		btnUpdateDB.setBounds(4, 65, buttonWidth, buttonHeight);
		btnUpdateDB.setIcon(new ImageIcon(buttonImages[4]));
		btnUpdateDB.setHorizontalAlignment(SwingConstants.LEFT);
		//btnUpdateDB.setBorder(null);
		//btnUpdateDB.setContentAreaFilled(false);
		dbPanel.add(btnUpdateDB);

		//btnDeleteEntry = new JButton(new ImageIcon(buttonImages[5]));
		btnDeleteEntry = new JButton("Remove Entry");
		btnDeleteEntry.setBounds(4, 110, buttonWidth, buttonHeight);
		btnDeleteEntry.setIcon(new ImageIcon(buttonImages[5]));
		btnDeleteEntry.setHorizontalAlignment(SwingConstants.LEFT);
		//btnDeleteEntry.setBorder(null);
		//btnDeleteEntry.setContentAreaFilled(false);
		dbPanel.add(btnDeleteEntry);
		
		// Change Status of Active component. Create ActionListener to launch JOptionPane to confirm change status and just update DB to Inactive
		btnActive = new JButton("Change Status");
		btnActive.setBounds(4, 155, buttonWidth, buttonHeight);
		btnActive.setIcon(new ImageIcon(buttonImages[5]));
		btnActive.setHorizontalAlignment(SwingConstants.LEFT);
		dbPanel.add(btnActive);
							

		// toggle between these if you want the JPanel to be displayed from
		// the code in this class or from a separate class with extends JPanel.
		// NOTE that action events are not recorded when importing the jpanalDBEvents()
		contentPane.add(dbPanel);
		//contentPane.add(secondPanel);	
		
		lblStatus = new JLabel("Status: ");
		lblStatus.setBounds(10, 106, 460, 62);
		//lblStatus.setForeground(blueColor); // setForeground changes the Text. background is outline of JLabel
		lblStatus.setFont(new Font("Ariel", Font.BOLD, 16)); // set to 16
		lblStatus.setBorder(raisedBorder);
	
		dataViewPanel = new JPanel();
		dataViewPanel.setLayout(null); 
		dataViewPanel.setBounds(6, 363, 814, 178);   
		dataViewPanel.setBorder(raisedBorder);	
		dataViewPanel.setBackground(creamColor); // Bottom Panel. lblDatabase takes this color also
		dataViewPanel.add(lblStatus);	
		contentPane.add(dataViewPanel);
		
		btnScreenshot = new JButton();
		btnScreenshot.setEnabled(false); // This has to be set to true in order for the action listener to work
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
		btnAnalytics.setEnabled(true); // Displays Analytics 
		btnAnalytics.setBorder(raisedBorder);
		btnAnalytics.setBounds(292, 11, 85, 85);
		dataViewPanel.add(btnAnalytics);
		
		// Database Ajax Loader JLabel
		lblDatabase = new JLabel();
		lblDatabase.setEnabled(false); 
		lblDatabase.setBorder(raisedBorder);
		lblDatabase.setBounds(385, 11, 85, 85);
		lblDatabase.setForeground(Color.PINK); // takes the color of the background from line 223 above
		dataViewPanel.add(lblDatabase);

		btnShowHTMLFormat = new JButton(new ImageIcon(buttonImages[6])); 
		//btnShowDocs.setFont(new Font("Serif", Font.BOLD, 22));
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
	
	public ArrayList<Bug> getBugFromTable() {	// remove int bugIndex
		return list;
	}

	public void setStatus(String message) {
		lblStatus.setText("<html>Status: " + message + "</html>");
	}

	public void clearTable() {
		
		// Possibly move this to the mainController with an action listener
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
	
	
	/*public class CustomTableCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			
			Component comp = super.getTableCellRendererComponent(table,  value, isSelected, hasFocus, row, column);
			
			//super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			comp.setBackground(row == 0 ? Color.YELLOW:Color.WHITE);
			if (row == 0) {
				comp.setBackground(Color.PINK);
			} else {
				comp.setBackground(Color.WHITE);
			}
			return comp;
		}
	}*/
}