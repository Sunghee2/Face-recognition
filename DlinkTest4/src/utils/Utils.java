package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Utils {
	private MatOfByte matOfByte;
	BufferedImage img = null;
	private Mat mat;
	
	public Utils() {
		matOfByte = new MatOfByte();
	}

	public BufferedImage matToBufferedImage(Mat img) {
		Imgcodecs.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			if (in != null) {
				bufImage = ImageIO.read(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bufImage;
	}
	
	public BufferedImage matToBufferedImage(Mat img, int Height, int Width) {
		Imgproc.resize(img, img, new Size(Height, Width));
		Imgcodecs.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			if (in != null) {
				bufImage = ImageIO.read(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bufImage;
	}
	
	public Mat urlToMat(URL url) {
		try {
			img = ImageIO.read(url.openStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
		byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		
		return mat;
	}	
}