package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import customUI.AutoComboBox;
import customUI.BoundsPopupMenuListener;
import customUI.MyHighlightPainter;
import util.FragmentPicker;
import webEngine.EachPage;
import webEngine.PageRanking;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int LAYOUT_MAINFRAME_HEGITH = 796;

	private static final int LAYOUT_MAINFRAME_WIDTH = 1280;

	private static MyHighlightPainter myHighlightPainter = new MyHighlightPainter(Color.YELLOW);

	private JComboBox<String> cbSearch;
	private JButton btnSearch;
	private JTextArea textArea;

	public MainFrame() {
		initGUI();
	}

	private void initGUI() {
		initFrameProperties();
		initComponents();
	}

	private void initFrameProperties() {

		// Main Frame Settings.
		setVisible(true);
		setSize(LAYOUT_MAINFRAME_WIDTH, LAYOUT_MAINFRAME_HEGITH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	}

	private void initComponents() {
		getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		cbSearch = new JComboBox<String>();

		cbSearch.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() {
				return new JButton() {
					public int getWidth() {
						return 0;
					}
				};
			}
		});
		cbSearch.setEditable(true);

		BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true, false);
		cbSearch.addPopupMenuListener(listener);
		cbSearch.setPrototypeDisplayValue(
				"1234567890123456789-123456789-123456789-1234567890123456789-123456789-123456789-");
		cbSearch.setMaximumRowCount(200);
		cbSearch.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		new AutoComboBox(cbSearch);
		getContentPane().add(cbSearch);

		btnSearch = new JButton("Search");
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				getSearchResult(cbSearch.getEditor().getItem().toString());
			}
		});
		getContentPane().add(btnSearch);

		JPanel panel = new JPanel();
		panel.setSize(1000, 800);
		getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 1000 };
		gbl_panel.rowHeights = new int[] { 0 };
		gbl_panel.columnWeights = new double[] { 0.0 };
		gbl_panel.rowWeights = new double[] { 0.0 };
		panel.setLayout(gbl_panel);

		textArea = new JTextArea();
		textArea.setRows(36);
		textArea.setEditable(false);
		textArea.setBackground(SystemColor.control);
		textArea.setColumns(94);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.anchor = GridBagConstraints.NORTHWEST;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		panel.add(textArea, gbc_textArea);

		JScrollPane scroll = new JScrollPane(textArea);

		// Add Textarea in to middle panel
		panel.add(scroll);

	}

	private void getSearchResult(String searchText) {
		textArea.setText("");
		searchText = searchText.trim();
		searchText.replaceFirst(" ", "");
		String[] words = searchText.split(" ");
		List<EachPage> searchResult = PageRanking.listTop10Pages(words);
		for (EachPage page : searchResult) {
			textArea.append(page.getPageName() + " - " + page.getPageScore());
			textArea.append("\n");
			try {
				textArea.append(FragmentPicker.pickFragment(page.getPageName(), words));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textArea.append("\n");
		}

		highlight(textArea, words);
	}

	// Creates highlights around all occurrences of pattern in textComp
	public static void highlight(JTextComponent textComp, String[] patterns) {
		// First remove all old highlights
		removeHighlights(textComp);

		try {
			Highlighter hilite = textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			for (String pattern : patterns) {
				int pos = 0;
				// Search for pattern
				while ((pos = text.indexOf(pattern, pos)) >= 0) {
					// Create highlighter using private painter and apply around
					// pattern
					hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
					pos += pattern.length();
				}
			}
		} catch (BadLocationException e) {
		}
	}

	// Removes only our private highlights
	public static void removeHighlights(JTextComponent textComp) {
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();

		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof MyHighlightPainter) {
				hilite.removeHighlight(hilites[i]);
			}
		}
	}

}
