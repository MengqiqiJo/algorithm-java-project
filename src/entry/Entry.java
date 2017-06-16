package entry;

import javax.swing.SwingUtilities;

import view.MainFrame;

public class Entry {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainFrame();
			}
		});
	}
}
