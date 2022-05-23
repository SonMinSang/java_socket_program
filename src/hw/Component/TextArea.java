package hw.Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;

public class TextArea extends JTextArea {
	private final JTextArea textArea;
	
	public TextArea(boolean edit) {
		this.textArea = new JTextArea();
		this.textArea.setEditable(edit);
	}
	public TextArea(String message) {
		
		this.textArea = new JTextArea(message);
		this.textArea.setEditable(false);
	}
	
	public TextArea(String message, Layout layout) {
	
		this.textArea = new JTextArea(message);
		this.textArea.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
		this.textArea.setBorder(new LineBorder(Color.black, 1, true));
		this.textArea.setEditable(false);
	}
	
	
	public JTextArea getTextArea() {
		return this.textArea;
	}
}
