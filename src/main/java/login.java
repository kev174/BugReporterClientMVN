import java.awt.*;
import javax.swing.*;
import com.nuigfyp.view.BugReporterLogin;

@SuppressWarnings("serial")
public class login extends JFrame {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {
																	
					/*bugReporterView frame = new bugReporterView();
					ConnectToAPIDatabase connectToAPIDatabase = new ConnectToAPIDatabase();
					@SuppressWarnings("unused")
					mainController theController = new mainController( frame, connectToAPIDatabase );
					frame.setVisible(true);	*/
					
					BugReporterLogin login = new BugReporterLogin();
					login.setVisible(true);	
					
					
					/*Long sid = new Long(1234567879);
					boolean valid = connectToAPIDatabase.authentication("un", "pw");*/
					// If the above is true, then launch the following	
					
							
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}