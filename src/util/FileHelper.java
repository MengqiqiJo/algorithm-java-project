package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

	public static List<String> readDirectory(String dir) {

		List<String> strList = new ArrayList<String>();
		List<File> fileList = getFileList(dir);

		StringBuilder strBuilder = new StringBuilder();

		for (File file : fileList) {
			try {
				strList.add(readToBuffer(strBuilder, file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strList;
	}

	public static void writeFile(String fileContent, String outFilename) throws IOException {
		OutputStream os = new FileOutputStream(outFilename);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		writer.write(fileContent);
		writer.flush();
		os.close();
	}

	public static void appendFile(StringBuffer buffer, String outFilename) throws IOException {
		OutputStream os = new FileOutputStream(outFilename, true);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		writer.write(buffer.toString());
		writer.flush();
		os.close();
	}

	public static String readToBuffer(StringBuilder strBuilder,File file) throws IOException {
		strBuilder = new StringBuilder();
		String line; // Content of each line
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		line = reader.readLine(); // read the first line
		while (line != null) { // false if reach the end of file
			if (line.length() != 0) {
				strBuilder.append(line); // store the content to buffer
				strBuilder.append('\n'); // add end of line symbol
			}
			line = reader.readLine(); // read next line
		}

		reader.close();
		fis.close();

		return strBuilder.toString();
	}
	
	/**
	 * Read file to buffer
	 */
	public static String readToBuffer(File file) throws IOException {
		StringBuilder strBuilder = new StringBuilder();
		String line; // Content of each line
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		line = reader.readLine(); // read the first line
		while (line != null) { // false if reach the end of file
			if (line.length() != 0) {
				strBuilder.append(line); // store the content to buffer
				strBuilder.append('\n'); // add end of line symbol
			}
			line = reader.readLine(); // read next line
		}

		reader.close();
		fis.close();

		return strBuilder.toString();
	}

	/**
	 * Read file to buffer
	 */
	public static void readToBuffer(StringBuffer buffer, InputStream is) throws IOException {
		String line; // Content of each line
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		line = reader.readLine(); // read the first line
		while (line != null) { // false if reach the end of file
			buffer.append(line); // store the content to buffer
			buffer.append('\n'); // add end of line symbol
			line = reader.readLine(); // read next line
		}
	}

	/***
	 * Obtain all files of the directory
	 * 
	 * @param obj
	 * @return
	 */
	public static ArrayList<File> getFileList(Object obj) {

		File directory = null;

		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}

		ArrayList<File> files = new ArrayList<File>();
		if (directory.isFile()) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getFileList(fileOne));
			}
		}
		return files;
	}

	/**
	 * Use PrintStream to convey data to StringBuffer
	 */
	public static void writeFromBuffer(StringBuffer buffer, OutputStream os) {

		PrintStream ps = new PrintStream(os);
		ps.print(buffer.toString());
	}

	/**
	 * Copy InputStream to OutputStream
	 * 
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os) throws IOException {

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
		line = reader.readLine();
		while (line != null) {
			// copy stream
			writer.println(line);
			line = reader.readLine();
		}

		// flush all buffer into the stream.
		writer.flush();
	}

	/**
	 * Use copyStream(InputStream, OutputStream) to copy file
	 */
	public static void copyTextFile(String inFilename, String outFilename) throws IOException {
		// Create file
		InputStream is = new FileInputStream(inFilename);
		OutputStream os = new FileOutputStream(outFilename);
		copyStream(is, os); // copy stream
		is.close(); // close InputStream
		os.close(); // close OutputStream
	}

	/**
	 * delete all html tags(UNTESTED)
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public static String splitAndFilterString(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// remove all html tags
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "Empty file";
		}
		return str;
	}

}
