package webEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.FileHelper;

public class ConvertHTMLToTxt {

	//The source folder where web pages files located
	private final static Path SDIR = Paths.get("./data/W3C Web Pages");
	//The destination folder where the text files will located
	private final static String TARGET = "./data/Text/";

	public static void main(String[] args) throws IOException {
		
		convertHTMLToTxt();
	}

	/**
	 * Convert all web page files to text files.
	 * @param fileList a list that contains all HTML files
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void convertHTMLToTxt() 
			throws IOException, FileNotFoundException {
		List<File> fileList = FileHelper.getFileList(SDIR);
		
		StringBuilder sb = new StringBuilder();
		for (File file : fileList) {
			org.jsoup.nodes.Document doc = Jsoup.parse(FileHelper.readToBuffer(sb, file));
			FileHelper.writeFile(doc.text().replaceAll("<", "").replaceAll(">", ""), TARGET + file.getName().replace(".htm", ".txt"));
		}
		
	}


}
