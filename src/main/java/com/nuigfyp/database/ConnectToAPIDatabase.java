package com.nuigfyp.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nuigfyp.model.Base64Coding;
import com.nuigfyp.model.Bug;
import com.nuigfyp.view.bugReporterView;


public class ConnectToAPIDatabase {

	private final static Logger log = Logger.getLogger(ConnectToAPIDatabase.class);
	//private String bugDatabaseName = "bug_reporter", filesDatabaseName = "bug_files"; // Both used on both DB
	// NUIG
	//private String databaseLink = "jdbc:mysql://mysql1.it.nuigalway.ie:3306/mydb2976?autoReconnect=true&useSSL=false";
	//private String un = "mydb2976ck", pw = "fa4nel";  
	
	// GEAR.HOST
	//private String databaseLink = "jdbc:mysql://den1.mysql1.gear.host:3306/bugfiles?autoReconnect=true&useSSL=false";
	//private String un = "bugfiles", pw = "Cusask!";	    
	
	// AMAZON AWS => War file running on Elastic BeanStalk 
	//private static final String API_URL = "http://bugreportersunday-env.jbmbcxixcs.eu-west-1.elasticbeanstalk.com/" + "bugs/";
	private static final String API_URL = "http://localhost:8080/Bug_Reporter_Rest_Amazon_Aws/bugs/"; // this works for DELETE_BUG_URL, GET_SPECIFIC_BUG_URL and UPDATE_BUG_URL
		
	private static final String[] COMPANY = new String[] { "SAP", "NUIG", "Ericsson", "Medtronic", "HP" };
	private static final String USER_AGENT = "Mozilla/5.0";			
	private static final String DOWNLOADED_FILES_DIRECTORY = System.getProperty("user.dir") + "/Downloaded_Files"; // two back slashes also works here
	private static final String DELETE_BUG_URL = API_URL; 
	private static final String CHANGE_STATUS_BUG_URL = API_URL; 
	private static final String UPDATE_BUG_URL = API_URL; 
	private static final String GET_SPECIFIC_BUG_URL = API_URL;
	private static final String GET_ALL_BUGS_URL = API_URL + "getAll";	
	private static final String POST_BUG_URL = API_URL + "addBug";	
    private static final String BASE_URL_GETFILENAMED = API_URL + "getFileNamed/";
    private static final String FILE_UPLOAD_URL = API_URL + "upload";	
	private static final String GET_SESSIONID = API_URL + "getSessionId/";	
	private String nameOfFileToBeSavedToDatabase, returnConnectionString = "";
	private static Base64Coding base64;
	private static long sessionId;

	public String changeStatusInDB(String id) throws SQLException {

		//int bugId = Integer.parseInt(id);
		base64 = new Base64Coding();
		String encodedBugId = base64.encode(id);
		String Change_Status_Bug_Url = CHANGE_STATUS_BUG_URL + "changeBugStatus/" + encodedBugId; 
		System.out.println("Bug id is: " + id + ". Encoded it is: " + encodedBugId + ". Sending to " + Change_Status_Bug_Url);
		
		try {

			URL url = new URL(Change_Status_Bug_Url);
			URLConnection urlCon = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) urlCon;
			http.setRequestMethod("PUT"); 
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			int responseCode = http.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				returnConnectionString = "[" + responseCode + "]" + ": Success Changing Status with Bug ID " + id;
			} else {
				returnConnectionString = "[" + responseCode + "]" + ": Failed to Changing Status with Bug ID " + id;
			}

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.changeStatusInDB(). " + e);
			returnConnectionString = "Failed to update Bug with ID " + id;
		}
		return returnConnectionString; 
		
	}
	
	public String deleteEntry(String id) throws SQLException {

		// Add header info to URLConnection
		// https://stackoverflow.com/questions/12732422/adding-header-for-httpurlconnection
		
		int bugId = Integer.parseInt(id);
		base64 = new Base64Coding();
		String encodedBugId = base64.encode(id);
		String Delete_API_URL = DELETE_BUG_URL + "deletebug/" + encodedBugId; 
		System.out.println("Bug id is: " + id + ". Encoded it is: " + encodedBugId + ". Sending to " + Delete_API_URL);
		String userCredentials = "username:password";
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

		try {

			URL url = new URL(Delete_API_URL);
			URLConnection urlCon = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) urlCon;
			http.setRequestProperty ("Authorization", basicAuth);
			http.setRequestMethod("DELETE"); 
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			int responseCode = http.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				returnConnectionString = "[" + responseCode + "]" + ": Success Deleting Bug ID Number " + bugId;
			} else {
				returnConnectionString = "[" + responseCode + "]" + ": Failed to Delete Bug ID Number " + bugId + ". Please try again.";
			}

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.deleteEntry(). " + e);
			returnConnectionString = "Failed to delete Bug with ID " + id;
		}

		return returnConnectionString; 
	}
	
	
	// the returned String gets displayed on theView.setStatus();
	public String updateEntry(Bug bug, int[] filesChanged) throws SQLException { // ==== int[] filesChanged NOT Used

		base64 = new Base64Coding();
		String encodedBugId = base64.encode(Integer.toString(bug.getId()));		
		String UPDATE_URL = UPDATE_BUG_URL + encodedBugId + "/updateBug";
		String bugConvertedToJson = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			bugConvertedToJson = mapper.writeValueAsString(bug);
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.updateEntry(). " + e);
			e.printStackTrace();
		}

		System.out.println("Sending to UR " + bugConvertedToJson);
		
		byte[] JsonConvertedToByteArray = bugConvertedToJson.getBytes(StandardCharsets.UTF_8);
		int length = JsonConvertedToByteArray.length;

		try {

			URL url = new URL(UPDATE_URL);
			URLConnection urlCon = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) urlCon;
			http.setRequestMethod("PUT"); // POST is another valid option
			http.setDoOutput(true);
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			
			// transfers the byte-array (Bug json object) over http
			try (OutputStream os = http.getOutputStream()) {
				os.write(JsonConvertedToByteArray);
			}
	
			int responseCode = http.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				returnConnectionString = ("[" + responseCode + "]" + ": Updated the database successfully.");
			} else {
				returnConnectionString = ("[" + responseCode + "]" + ": Updated to the database failed.");
			}

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.updateEntry(). " + e);
			returnConnectionString = "Failed to update Bug with id " + bug.getId();
		}
		
		return returnConnectionString; 
	}

	
	public String addEntry(Bug bug, int[] filesChanged) throws SQLException {

		String bugConvertedToJson = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			
			bugConvertedToJson = mapper.writeValueAsString(bug);
			
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.addEntry(). " + e);
			e.printStackTrace();
		}

		// This is a vanilla way to POST in Java
		byte[] JsonConvertedToByteArray = bugConvertedToJson.getBytes(StandardCharsets.UTF_8);
		int length = JsonConvertedToByteArray.length;

		try {

			URL url = new URL(POST_BUG_URL);
			// HttpURLConnection to be used for POST requests
			URLConnection urlCon = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) urlCon;
			http.setRequestMethod("POST"); 
			http.setDoOutput(true);
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();

			try (OutputStream os = http.getOutputStream()) {
				os.write(JsonConvertedToByteArray);
			}

			int responseCode = http.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				returnConnectionString = ("[" + responseCode + "]" + "Success: Adding new entry to database.");
			} else {
				returnConnectionString = ("[" + responseCode + "]" + "Failure: Adding new entry to database.");
			}

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.addEntry(). " + e);
			returnConnectionString = ("Failure: Adding entry to database.");
		} 
		
		return returnConnectionString;
	}
	
	public String POSTRequest(String fileDirectory, int companyIndex) {	 
		   
		FileInputStream fis = null;
		String responseFileName;
		base64 = new Base64Coding();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh-mm-ss");  
		nameOfFileToBeSavedToDatabase = "";
		nameOfFileToBeSavedToDatabase = fileDirectory;
		File uploadThisFile = new File(nameOfFileToBeSavedToDatabase);	
		String encodedFileName = base64.encode(COMPANY[--companyIndex] + "/" + dtf.format(LocalDateTime.now()) + "-" + uploadThisFile.getName()); 

		try {
				
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);			
			MultipartEntity entity = new MultipartEntity();
			fis = new FileInputStream(uploadThisFile);
			entity.addPart("file", new InputStreamBody(fis, encodedFileName));
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			responseFileName = EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println("POSTRequest(): [" + statusCode + "]: 200 = Success saved to Web Server Database. File is saved to/as " + responseFileName);

		} catch (Exception exc) {
			if (exc instanceof ClientProtocolException || exc instanceof IOException)
				JOptionPane.showMessageDialog(null,
						"Client Protocl (Connection) or IO Exception caught on the file " + uploadThisFile,
						"Unable to Read File.", JOptionPane.INFORMATION_MESSAGE);
			log.error("General Exception at ConnectToAPIDatabase.POSTRequest(). " + exc);
			exc.printStackTrace();
			return "No";
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				return "No";
			}
		}
		
		// Copies the users selected file to the 'Downloaded_Files' directory.
		Path FROM = Paths.get(uploadThisFile.getAbsolutePath());
		File source = new File(responseFileName); 
		String filenameis = source.getName();
		String savethisfile = DOWNLOADED_FILES_DIRECTORY + "/" + filenameis;
		savethisfile = savethisfile.replaceAll("\"", ""); 
		Path TO = Paths.get(savethisfile); 
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };
		try {
			Files.copy(FROM, TO, options);
		} catch (IOException e) {
			log.error("IO Exception at ConnectToAPIDatabase.POSTRequest(). Cannot save the " + FROM + " file to th local machine. " + e);	
			System.out.println("ConnectToAPIDatabase.POSTRequest(). Cannot save the " + FROM + " file to th local machine. ");
		}
		
		return responseFileName;
	}
		
	
	public Bug GETSpecificBugObject(String primaryKey) {
		
		base64 = new Base64Coding();
		String SpecificBugURL = GET_SPECIFIC_BUG_URL + base64.encode(primaryKey) + "/" + "getSpecificBug";
			
		Bug aBug = new Bug();
		
		try {
			
			URL obj = new URL(SpecificBugURL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
						
			if (responseCode == HttpURLConnection.HTTP_OK) { 
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(response.toString()); 
				JsonObject rootobj = root.getAsJsonObject(); // May be an array, may be an object.	
				Gson gson = new GsonBuilder().setLenient().create();
				aBug = gson.fromJson(rootobj, Bug.class);
								
			} else {
				System.out.println("GET request Failed!!!");
			}
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.GETSpecificBugObject(). " + e);
			e.printStackTrace();
		} 
		
		return aBug;
	}

	
	@SuppressWarnings("resource")
	public static void GETRequest(String fileName) throws Exception {	// if changing upload filename then you have to change the name here also to save as this exact name.
        				
		/*// WORKS, but i have to add the resteasy-clent and resteasy-jaxrs jars
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(BASE_URL_GETFILENAMED + fileName);
		Response response = target.request().get();
		int value = response.getStatus();	
		System.out.println("Returned Value from GETRequest() is: [" +  value + "]. 200 = success.");*/		
		
		//-------------------------------------------------------------------------------------
		// Don't worry about a return code just handle the exception here i.e. pass the network
		// error back to theView.setStatus("Failed to download the file. try again later");
		// Error code exception = 500. The code above could be removed.
		//-------------------------------------------------------------------------------------
		FileOutputStream fos = null;
		base64 = new Base64Coding();
		
		try {
			URL website = new URL(BASE_URL_GETFILENAMED + base64.encode(fileName));
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());					
			fos = new FileOutputStream(DOWNLOADED_FILES_DIRECTORY + "/" + fileName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.GETSpecificBugObject(). " + e);
			// If the file does not exist then this exception is captured as error 500 - IOException
			e.printStackTrace();
			throw e;
		}	    
	}
	
	
	public ArrayList<Bug> getAllBugs() throws Exception {	
	    
		// http://localhost:8080/Bug_Reporter_Rest_Amazon_Aws/bugs/getAll

		//System.out.println("Session ID to be sent to the API is " + sessionId);
		String sessionIdToString = Long.toString(sessionId);
		base64 = new Base64Coding();
		String encodedSessionId = base64.encode(sessionIdToString);
		
		timerDelay();
		ArrayList<Bug> buglist = new ArrayList<Bug>();
		JSONArray jsonArray = null;
		//String userCredentials = "UserNName:PPassword";
		//String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));		
		//System.out.println("getAllBugs: Authorising " + basicAuth);
		
		try {
			
			URL url = new URL(GET_ALL_BUGS_URL + "/" + encodedSessionId);
			URLConnection request = url.openConnection();
			//request.setRequestProperty("Authorization", basicAuth);
			request.connect();
			JsonParser jp = new JsonParser(); 
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); // Convert the input stream to a json element
	
			try {
				jsonArray = new JSONArray(root.toString());
			} catch (JSONException e) {
				System.out.println("error in JSONArray.");
				e.printStackTrace();
			}
			
			for(int n = 0; n < jsonArray.length(); n++) {
			    JSONObject object = jsonArray.getJSONObject(n);			    
				Gson gson = new GsonBuilder().setLenient().create();
				Bug aBug = gson.fromJson(object.toString(), Bug.class);
				buglist.add(aBug);
			}
			
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.getAllBugs(). " + e);
			System.out.println("ConnectToAPIDatabase.getAllBugs error, " + e);
			throw e;
		} 
		
		return buglist;
	}
	
	public boolean authentication(String username, String password) {

		//System.out.println("Login in ConnecToAPIDAtabase is " + username + ", passsword is " + password);
		String returnedValue = "";		
		base64 = new Base64Coding();
		String encodedUserInfo = base64.encode(username + ":" + password);

		try {
			URL url = new URL(GET_SESSIONID + encodedUserInfo);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			returnedValue = (base64.decode(root.toString()));	
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.authentication(). " + e);
			System.out.println("ConnectToAPIDatabase.authentication() Exception, " + e);
			return false;
		}


		String[] data = returnedValue.split(":", 2);	
		String sid = data[0];						// Session ID returned form the REST API.
		sessionId = Long.parseLong(sid);
		String expiryDate = data[1];	
		DateTime sessionExpiryDate = new DateTime(expiryDate);
		//System.out.println("Date Returned from API is: " + sessionExpiryDate + ". SessionID returned is " + sessionId);
		
		DateTime currentDate = DateTime.now();
		DateTime currentTimePlusFive = currentDate.plusMinutes(5);
		//System.out.println("CurrentDate Time plus 5        " + currentTimePlusFive);
		
		if(sessionExpiryDate.compareTo(currentTimePlusFive) < 1) {
            //System.out.println("currentTimePlusFive is greater than the currentTime. So Yes, This is a Valid Session ID.");
		}
		
		//String formattedCurrentTime = currentDate.toString(pattern);
		// System.out.println("Current Time Formatted is " + formattedCurrentTime);

		/*ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(timeruner, 5, 5, TimeUnit.SECONDS);*/

		return true;
	}
	
	
	/*public static Runnable timeruner = new Runnable() {
		public void run() {
			System.out.println("I run every 5 Seconds.");
		}
	};*/
	

	public void timerDelay() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
