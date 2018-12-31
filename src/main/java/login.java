import java.awt.*;
import javax.swing.*;

import com.nuigfyp.controller.mainController;
import com.nuigfyp.database.ConnectToAPIDatabase;
//import com.nuigfyp.model.bugModel;
import com.nuigfyp.view.bugReporterView;

@SuppressWarnings("serial")
public class login extends JFrame {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {
					bugReporterView frame = new bugReporterView();
					//bugModel theModel = new bugModel(); // POSSIBLY REMOVE AS METHOD BELOW TAKING OVER
					ConnectToAPIDatabase connectToAPIDatabase = new ConnectToAPIDatabase();
					@SuppressWarnings("unused")
					mainController theController = new mainController( frame, connectToAPIDatabase );
					
					//loginView frame = new loginView();
					//loginModel theModel = new loginModel();
					//loginController theController = new loginController( frame, theModel );
					
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}