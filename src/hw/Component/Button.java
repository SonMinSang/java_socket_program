package hw.Component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import hw.Client;


public class Button {

	private JButton button;
	
	
	public Button(String message, Layout layout, boolean isEnabled) {
		this.button = new JButton(message);
		this.button.setBounds(layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight()); 
		if (isEnabled == false) {
			this.button.setEnabled(false);
		}
	}
	

	
	
	public AbstractButton getButton() {
		return this.button;
	}
	
}
