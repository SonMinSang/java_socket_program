package hw.Component;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class ScrollPane {

	
	private final JScrollPane scrollPane;
	public ScrollPane(JTextArea textArea, Layout layout) {
		
		this.scrollPane = new JScrollPane(textArea);
		this.scrollPane.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
		this.scrollPane.setBorder(new LineBorder(Color.black, 1, true));
	}
	
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
}
