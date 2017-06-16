package customUI;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter;

public class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

	public MyHighlightPainter(Color color) {
		super(color);
	}
}
