package webEngine;

public class PageInfo {
	private final String pageName;
	private int wordFrequency;

	public PageInfo(final int frequency, final String pageName) {

		this.pageName = pageName;
		this.wordFrequency = frequency;
	}

	public void setFrequency(int frequency) {
		this.wordFrequency = frequency;
	}
	
	public String getPageName() {
		return pageName;
	}

	public int getFrequency() {
		return wordFrequency;
	}
}
