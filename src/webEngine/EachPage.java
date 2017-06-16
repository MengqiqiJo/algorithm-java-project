package webEngine;

public class EachPage implements Comparable<EachPage> {
	private final String pageName;
	private final int pageScore;
	
	public EachPage(final String name, final int score) {
		
		this.pageName = name;
		this.pageScore = score;
	}
	
	public String getPageName() {
		return pageName;
	}

	public int getPageScore() {
		return pageScore;
	}

	
	public String toString() {
		return String.format("%s, The score is =%d", pageName, pageScore);
	}

	@Override
	public int compareTo(EachPage to) {
		return to.pageScore - this.pageScore;
	}
}