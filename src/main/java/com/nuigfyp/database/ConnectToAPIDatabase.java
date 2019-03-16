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
//import com.nuigfyp.view.bugReporterView;


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
	private static DateTime expiryDateTime;
	private static String username = "", password = "", user = "";

	public String changeStatusInDB(String id) throws SQLException {

		base64 = new Base64Coding();
		String encodedBugId = base64.encode(id);
		
		if (checkExpiryDate()) {
			try {

				URL url = new URL(CHANGE_STATUS_BUG_URL + "changeBugStatus/" + encodedBugId + "/" + encodedSessionId());
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
		}
		
		return returnConnectionString; 	
	}
	
	public String deleteEntry(String id) throws SQLException {
		
		int bugId = Integer.parseInt(id);
		base64 = new Base64Coding();
		String encodedBugId = base64.encode(id);
		String Delete_API_URL = DELETE_BUG_URL + "deletebug/" + encodedBugId + "/" + encodedSessionId(); 

		if (checkExpiryDate()) {
			try {

				URL url = new URL(Delete_API_URL);
				URLConnection urlCon = url.openConnection();
				HttpURLConnection http = (HttpURLConnection) urlCon;
				http.setRequestMethod("DELETE");
				http.setDoOutput(true);
				http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				http.connect();
				int responseCode = http.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					returnConnectionString = "[" + responseCode + "]" + ": Success Deleting Bug ID Number " + bugId;
				} else {
					returnConnectionString = "[" + responseCode + "]" + ": Failed to Delete Bug ID Number " + bugId
							+ ". Please try again.";
				}

			} catch (Exception e) {
				log.error("General Exception at ConnectToAPIDatabase.deleteEntry(). " + e);
				returnConnectionString = "Failed to delete Bug with ID " + id;
			} 
		}
		
		return returnConnectionString; 
	}
	
	
	public String updateEntry(Bug bug, int[] filesChanged) throws SQLException { // ==== int[] filesChanged NOT Used

		base64 = new Base64Coding();
		String encodedBugId = base64.encode(Integer.toString(bug.getId()));	
		String UPDATE_URL = UPDATE_BUG_URL + encodedBugId + "/updateBug/" + encodedSessionId();
		String bugConvertedToJson = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			bugConvertedToJson = mapper.writeValueAsString(bug);
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.updateEntry(). " + e);
			e.printStackTrace();
		}
		
		byte[] JsonConvertedToByteArray = bugConvertedToJson.getBytes(StandardCharsets.UTF_8);
		int length = JsonConvertedToByteArray.length;

		if (checkExpiryDate()) {
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

		if (checkExpiryDate()) {
			try {

				URL url = new URL(POST_BUG_URL + "/" + encodedSessionId());
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

			//int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			responseFileName = EntityUtils.toString(responseEntity, "UTF-8");

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
		}
		
		return responseFileName;
	}
		
	
	public Bug GETSpecificBugObject(String primaryKey) {
		
		base64 = new Base64Coding();
		String SpecificBugURL = GET_SPECIFIC_BUG_URL + base64.encode(primaryKey) + "/" + "getSpecificBug";
			
		Bug aBug = new Bug();
		
		if (checkExpiryDate()) {
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

				} 

			} catch (Exception e) {
				log.error("General Exception at ConnectToAPIDatabase.GETSpecificBugObject(). " + e);
			} 
		}
		return aBug;
	}

	
	public static void GETRequest(String fileName) throws Exception {

		FileOutputStream fos = null;
		base64 = new Base64Coding();
		
		try {
			URL website = new URL(BASE_URL_GETFILENAMED + base64.encode(fileName));
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());					
			fos = new FileOutputStream(DOWNLOADED_FILES_DIRECTORY + "/" + fileName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.GETSpecificBugObject(). " + e);
			throw e;
		}	
		fos.close();
	}
	
	
	public ArrayList<Bug> getAllBugs() throws Exception {	
	    
		base64 = new Base64Coding();	
		ArrayList<Bug> buglist = new ArrayList<Bug>();
		JSONArray jsonArray = null;
		
		if (checkExpiryDate()) {
			try {
				URL url = new URL(GET_ALL_BUGS_URL + "/" + encodedSessionId());
				//URL url = new URL(GET_ALL_BUGS_URL);
				URLConnection request = url.openConnection();
				request.connect();
				JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); // Convert the input stream to a json element

				try {
					jsonArray = new JSONArray(root.toString());
				} catch (JSONException e) {
				}

				for (int n = 0; n < jsonArray.length(); n++) {
					JSONObject object = jsonArray.getJSONObject(n);
					Gson gson = new GsonBuilder().setLenient().create();
					Bug aBug = gson.fromJson(object.toString(), Bug.class);
					buglist.add(aBug);
				}

			} catch (Exception e) {
				log.error("General Exception at ConnectToAPIDatabase.getAllBugs(). " + e);
				throw e;
			} 
		} 
		
		return buglist;
	}
	
	@SuppressWarnings("static-access")
	public boolean authentication(String username, String password, String user) {

		this.username = username;
		this.password = password;
		this.user = user;
		
		String returnedValue = "";		
		base64 = new Base64Coding();
		String encodedUserInfo = base64.encode(username + ":" + password + ":" + user);

		try {
			URL url = new URL(GET_SESSIONID + encodedUserInfo);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			returnedValue = (base64.decode(root.toString()));	
		} catch (Exception e) {
			log.error("General Exception at ConnectToAPIDatabase.authentication(). " + e);
			return false;
		}

		String[] data = returnedValue.split(":", 2);	
		String sid = data[0];						// Session ID returned form the REST API.
		sessionId = Long.parseLong(sid);
		String expiryDate = data[1];	
		
		//DateTime sessionExpiryDate = new DateTime(expiryDate);	
		
		// ************ Testing Purposes it will expire after 5 mins so get all should fail
		expiryDateTime = new DateTime(expiryDate).minusMinutes(4);
				
		//String formattedCurrentTime = currentDate.toString(pattern);
		// System.out.println("Current Time Formatted is " + formattedCurrentTime);

		return true;
	}
	
	// I CHANGED METHOD sessionExpiryDate as it is not been used. Could change back if anomalies
	public boolean checkExpiryDate() { 

		DateTime currentDate = DateTime.now();
		//System.out.println("CLIENT: Current DateTime Is: " + currentDate + ", Session Expiry DateTime Is: " + expiryDateTime);
		
		if (expiryDateTime.compareTo(currentDate) < 1) {
			//System.out.println("CLIENT: The CurrentDate is GREATER than the Expiry Date, So i need a NEW SessionId.");		
			authentication(username, password, user);
			//System.out.println("NEW SessionId returned is: " + sessionId);
			return true;
		} else if (currentDate.compareTo(expiryDateTime) < 1) {
			//System.out.println("CLIENT: OK: " + sessionId + ", CurrentDate is LESS than SessionExpiryDate...");		
			return true;
		} 
		
		return false;
	}

	public String encodedSessionId() {

		base64 = new Base64Coding();
		String sessionIdToString = Long.toString(sessionId);
		String encodedSessionId = base64.encode(sessionIdToString);
		return encodedSessionId;

	}

	public void timerDelay() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
