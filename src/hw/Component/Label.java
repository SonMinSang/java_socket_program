package hw.Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;

public class Label extends JLabel{
	private final JLabel label;
	
	
	public Label(String message, Layout layout) {
	
		this.label = new JLabel(message);
		this.label.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
	}
	
	public Label(String message, Layout layout, boolean border) {
		
		this.label = new JLabel(message);
		this.label.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
		this.label.setBorder(new LineBorder(Color.black, 1, true));
	}
	
	public JLabel getLabel() {
		return this.label;
	}
}
