package webEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateWordDic {

	private final static String spliter = " ";
	private final Integer COUNTER = 1;
	private final static Path DIR = Paths.get("./data/Text");
	private final static Map<String, List<PageInfo>> wordDictionary = new HashMap<>();
	private final static CreateWordDic createWordDic = new CreateWordDic();

	public static CreateWordDic getInstatnce() {
		return createWordDic;
	}

	private CreateWordDic() {
		try {
			initializeWordDictionary(createFileList(DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<File> createFileList(Path dir) {
		List<File> fileList = new ArrayList<File>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
			for (Path file : stream) {
				fileList.add(file.toFile());
			}

		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		}
		return fileList;
	}

	/**
	 * Store each string in string array into hashtable
	 * 
	 * @param str1
	 * @return
	 */
	private Map<String, Integer> storeWordInHashtable(String[] str1) {
		Map<String, Integer> table1 = new HashMap<String, Integer>();
		for (String str : str1) {
			if (!table1.containsKey(str)) {
				table1.put(str, COUNTER);
			} else {
				Integer newValue = table1.get(str) + 1;
				table1.put(str, newValue);
			}
		}
		return table1;
	}

	/**
	 * Save the word which meet the requirement to a string array. only the
	 * letters and numbers are allowed
	 * 
	 * @param br
	 * @param sb
	 * @return
	 * @throws IOException
	 */
	private String[] fetchWord(File filename) throws IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
		BufferedReader br = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		int tmpchar;
		while ((tmpchar = br.read()) != -1) {
			if ((char) tmpchar != '\n' && (char) tmpchar != ' ') {
				if (isDigit((char) tmpchar) || isAlphabit((char) tmpchar)) {
					sb.append((char) tmpchar);
				}
			} else {
				sb.append(spliter);
			}
		}
		String[] str1 = sb.toString().split("\\s+");
		br.close();
		return str1;
	}

	public static boolean isDigit(char tmpchar) {
		return tmpchar >= '0' && tmpchar <= '9';
	}

	public static boolean isAlphabit(char tmpchar) {
		return (tmpchar >= 'A' && tmpchar <= 'Z') || (tmpchar >= 'a' && tmpchar <= 'z');
	}

	public static void main(String[] args) throws IOException {
		final CreateWordDic wordDic = CreateWordDic.getInstatnce();

		Map<String, List<PageInfo>> aaa = wordDic.getWordDictionary();

		Set<String> keys = aaa.keySet();
		for (String key : keys) {
			System.out.println(key + "---" + aaa.get(key).size());
			for (int i = 0; i < aaa.get(key).size(); i++) {
				System.out.println(aaa.get(key).get(i).getPageName() + "--"
						+ aaa.get(key).get(i).getFrequency());
			}

		}

	}

	public Map<String, List<PageInfo>> getWordDictionary() {
		Map<String, List<PageInfo>> map = new HashMap<>();
		map.putAll(wordDictionary);
		return map;
	}

	private void initializeWordDictionary(List<File> fileList) throws IOException {
		for (File file : fileList) {
			String[] str1 = fetchWord(file);
			Map<String, Integer> table1 = storeWordInHashtable(str1);
			Set<String> keys = table1.keySet();
			//System.out.println(file);
			for (String key : keys) {
				//System.out.println(key + "----" + table1.get(key));

				PageInfo pi = new PageInfo(table1.get(key), file.getName());
				if (!wordDictionary.containsKey(key)) {
					List<PageInfo> pageList = new ArrayList<PageInfo>();
					pageList.add(pi);
					wordDictionary.put(key, pageList);
				} else {
					List<PageInfo> pageList = wordDictionary.get(key);
					if (!pageList.contains(pi)) {
						pageList.add(pi);
					}
				}
			}
		}
	}

}
