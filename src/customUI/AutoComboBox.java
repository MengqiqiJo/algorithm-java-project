package customUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import webEngine.SearchTrie;

public class AutoComboBox extends PlainDocument {

	private static final long serialVersionUID = 3762770794732039681L;

	JComboBox<String> comboBox;
	ComboBoxModel<String> model;
	JTextComponent editor;

	String prefix = "";

	// flag to indicate if setSelectedItem has been called
	// subsequent calls to remove/insertString should be ignored
	boolean isDynamic = true;
	boolean selecting = false;
	boolean hidePopupOnFocusLoss;
	boolean hitBackspace = false;
	boolean hitBackspaceOnSelection;

	public AutoComboBox(final JComboBox<String> comboBox) {
		this.comboBox = comboBox;
		model = comboBox.getModel();
		editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.setDocument(this);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!selecting)
					highlightCompletedText(0);
			}
		});
		editor.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (comboBox.isDisplayable())
					comboBox.setPopupVisible(true);
				hitBackspace = false;
				switch (e.getKeyCode()) {
				// determine if the pressed key is backspace (needed by the
				// remove method)
				case KeyEvent.VK_BACK_SPACE:
					// hitBackspace = true;
					// hitBackspaceOnSelection = editor.getSelectionStart() !=
					// editor.getSelectionEnd();
					break;
				}
			}
		});
		// Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when
		// tabbing out
		hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
		// Highlight whole text when gaining focus
		editor.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				highlightCompletedText(0);
			}

			public void focusLost(FocusEvent e) {
				// Workaround for Bug 5100422 - Hide Popup on focus loss
				if (true)
					comboBox.setPopupVisible(false);
			}
		});
		// Handle initially selected object
		Object selected = comboBox.getSelectedItem();
		if (selected != null)
			setText(selected.toString());
		highlightCompletedText(0);
	}

	public void remove(int offs, int len) throws BadLocationException {
		// return immediately when selecting an item
		if (selecting)
			return;
		if (hitBackspace) {
			// user hit backspace => move the selection backwards
			// old item keeps being selected
			if (offs > 0) {
				if (hitBackspaceOnSelection)
					offs--;
			} else {
				// User hit backspace with the cursor positioned on the start =>
				// beep
				comboBox.getToolkit().beep(); // when available use:
												// UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
			}
			highlightCompletedText(offs);

		} else {
			super.remove(offs, len);
			insertString(offs, "", null);
		}
	}

	public void updataComboItem(String str, Iterable<String> newItems) {

		comboBox.removeAllItems();
		comboBox.addItem(str);
		for (String item : newItems) {
			if (!item.equals(str)) {
				if (prefix.split(" ").length > 1) {
					comboBox.addItem(str.substring(0,str.lastIndexOf(" ") + 1) + item);
				} else {
					comboBox.addItem(item);
				}
			}
		}
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

		super.insertString(offs, str, a);

		String result = editor.getText();

		if (result.length() == 0 || " ".equals(str)) {
			return;
		}

		// return immediately when selecting an item
		if (selecting) {
			return;
		}
		if (isDynamic) {
			isDynamic = false;
			String[] searchTexts = (result).split(" ");
			updataComboItem(result, SearchTrie.prefixSearchLm(searchTexts[searchTexts.length - 1], 5));
		}
		prefix = result;
		
		// insert the string into the document

		// lookup and select a matching item
		Object item = lookupItem(result);

		// boolean listContainsSelectedItem = true;
		if (item == null) {
			// no item matches => use the current input as selected item
			item = getText(0, getLength());
			// listContainsSelectedItem = false;
		}

		setSelectedItem(item);
		setText(item.toString());
		// select the completed part
		// if (listContainsSelectedItem)
		// highlightCompletedText(str.length());

		isDynamic = true;
	}

	private void setText(String text) {
		try {
			// remove all text and insert the completed string
			super.remove(0, getLength());
			super.insertString(0, text, null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private void highlightCompletedText(int start) {
		editor.setCaretPosition(getLength());
		editor.moveCaretPosition(start);
	}

	private void setSelectedItem(Object item) {
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private Object lookupItem(String pattern) {
		Object selectedItem = model.getSelectedItem();
		// only search for a different item if the currently selected does not
		// match
		if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
			return selectedItem;
		} else {
			// iterate over all items
			for (int i = 0, n = model.getSize(); i < n; i++) {
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if (startsWithIgnoreCase(currentItem.toString(), pattern)) {
					return currentItem;
				}
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	// checks if str1 starts with str2 - ignores case
	private boolean startsWithIgnoreCase(String str1, String str2) {
		return str1.toUpperCase().startsWith(str2.toUpperCase());
	}

	private static void createAndShowGUI() {
		// the combo box (add/modify items if you like to)
		JComboBox<String> comboBox = new JComboBox<String>(
				new String[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" });
		comboBox.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() {
				return new JButton() {

					private static final long serialVersionUID = 1L;

					public int getWidth() {
						return 0;
					}
				};
			}
		});
		// has to be editable
		comboBox.setEditable(true);
		// change the editor's document
		new AutoComboBox(comboBox);

		// create and show a window containing the combo box
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().add(comboBox);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}