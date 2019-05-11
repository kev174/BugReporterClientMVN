import java.awt.*;
import javax.swing.*;
import com.nuigfyp.view.BugReporterLogin;

@SuppressWarnings("serial")
public class login extends JFrame {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {															
					
					BugReporterLogin login = new BugReporterLogin();
					login.setVisible(true);	
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}