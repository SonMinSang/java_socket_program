package hw.Component;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

public class Panel extends JPanel{

	private final JPanel panel;
	
	public Panel(Layout layout) {
		
		this.panel = new JPanel();
		this.panel.setLayout(null);
		this.panel.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
	}
	
	public Panel(Layout layout, AbstractBorder border) {
	
		this.panel = new JPanel();
		this.panel.setLayout(null);
		this.panel.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
		this.panel.setBorder(border);
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	public void addAll(Component...components) {
		for(Component component:components) {
			this.panel.add(component);
		}
	}
}
