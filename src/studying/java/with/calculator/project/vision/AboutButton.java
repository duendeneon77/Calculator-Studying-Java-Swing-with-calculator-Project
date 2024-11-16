package studying.java.with.calculator.project.vision;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class AboutButton extends JButton{
	public AboutButton(String ourText, Color ourColor) {
		setText(ourText);
		setFont(new Font("courier", Font.PLAIN,25));
		setOpaque(true);
		setBackground(ourColor);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
	}

}
