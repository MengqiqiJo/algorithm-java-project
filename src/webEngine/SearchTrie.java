package webEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import util.TST;

public class SearchTrie {

	public static final int INT_INITVALUE = 1;

	private final static SearchTrie searchTrie = new SearchTrie();

	private static TST<Integer> searchTST;

	private SearchTrie() {
		createTST();
	}

	public static SearchTrie getInstance() {
		return searchTrie;
	}

	private void createTST() {
		searchTST = new TST<Integer>();

		Map<String, List<PageInfo>> wordTable = CreateWordDic.getInstatnce().getWordDictionary();

		for (String key : wordTable.keySet()) {

			int wordCount = 0;

			for (PageInfo element : wordTable.get(key)) {
				wordCount += element.getFrequency();
			}

			searchTST.put(key, wordCount);

		}
	}

	public static Iterable<String> prefixSearch(String str) {
		return searchTST.prefixMatch(str);
	}

	public static Iterable<String> prefixSearchLm(String str, int count) {
		return searchTST.prefixMatchLm(str, count);
	}

	public static TST<Integer> getTST() {
		return searchTST;
	}

	public static void TRIEDEBUG() {
		for (String key : searchTST.keys()) {
			System.out.println(key);
			System.out.println(searchTST.get(key));
		}
	}

	public static void main(String[] args) throws IOException {
		TRIEDEBUG();
	}

}
