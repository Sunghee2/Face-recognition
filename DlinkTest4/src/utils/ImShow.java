package utils;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;

public class ImShow {

	public static int DEFAULT_HEIGHT = 500;
	public static int DEFAULT_WIDTH = 400;

	public JFrame Window;
	private ImageIcon image;
	private JLabel label;
	private Boolean SizeCustom;
	private int Height, Width;
	public boolean open;

	private Utils utils;

	public ImShow(String title) {
		SizeCustom = true;
		Height = DEFAULT_HEIGHT;
		Width = DEFAULT_WIDTH;
		utils = new Utils();

		Window = new JFrame();
		image = new ImageIcon();
		label = new JLabel();
		label.setIcon(image);
		Window.getContentPane().add(label);
		Window.setResizable(false);
		Window.setTitle(title);
		open = true;

		Window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				open = false;
				//System.exit(0);
			}
		});
	}

	public void showImage(Mat img) {
		if (img != null) {
			if (SizeCustom) {
				image.setImage(utils.matToBufferedImage(img, Height, Width));
			} else {
				image.setImage(utils.matToBufferedImage(img));
			}
			Window.pack();
			label.updateUI();
			Window.setVisible(true);
			Window.repaint();
		}
	}
}

	


