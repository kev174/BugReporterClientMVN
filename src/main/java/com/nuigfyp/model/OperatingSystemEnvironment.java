package com.nuigfyp.model;


public class OperatingSystemEnvironment {

	public static String getOperatingSystem() {

		String localOS = System.getProperty("os.name").toLowerCase();	
		
		if (localOS.indexOf("win") >= 0) {
			return "Windows";
		} else if (localOS.indexOf("lin") >= 0) {
			return "Linux";
		} else {
			return "N/A";
		}
	}
	
	public static String pdfReaderCMD() {

		String localOS = System.getProperty("os.name").toLowerCase();	
		
		if (localOS.indexOf("win") >= 0) {
			return ("rundll32 url.dll, FileProtocolHandler ");
		} else if (localOS.indexOf("lin") >= 0) {
			return "xdg-open ";
		} else {
			return null;
		}
	}
}
