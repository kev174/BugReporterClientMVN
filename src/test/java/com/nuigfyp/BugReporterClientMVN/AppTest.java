package com.nuigfyp.BugReporterClientMVN;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.nuigfyp.database.ConnectToAPIDatabase;
import com.nuigfyp.database.JUnitTestingInterface;


public class AppTest 
{
	
	// ConnectToAPIDatabase implements JUnitTestingInterface interface i.e. returnString(); method
	// returnString() method returns a string "kevin".
	JUnitTestingInterface test = new ConnectToAPIDatabase();
	
    @Test
    public void shouldAnswerWithTrue()
    {
    	
        assertEquals(test.returnString(), "kevin");
        
    } 
}
