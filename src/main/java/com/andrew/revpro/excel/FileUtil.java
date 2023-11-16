package com.andrew.revpro.excel;

public class FileUtil {
	public static String sanitizeFileName(String fileName) {
	    // Define a regex pattern for disallowed characters
	    String regex = "[<>:\"/\\|?*]";
	    // Replace disallowed characters with dashes
	    String sanitizedFileName = fileName.replaceAll(regex, "-");
	    return sanitizedFileName;
	}
}
