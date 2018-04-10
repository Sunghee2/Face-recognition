package server;

import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.URL;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import detection.FaceDetection;
import utils.ImShow;
import utils.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

public class EchoServerHandler extends Thread {	
	private URL url;
	private String username = "admin";
	private String password = "MJU12345";
	private int id;
	private Socket client;
	private int state = 0;
	private int sum = 0;
	
	boolean doheeFlag = false;
	boolean dicaFlag = false;

	PrintWriter out;
	
	FaceDetection faceDetection = new FaceDetection();
	ImShow im = new ImShow("Video Preview");
	Utils util = new Utils();
	Mat frame = new Mat();
	
	
	public EchoServerHandler(Socket client, int id, String dlink) {
		this.client = client;
		this.id = id;
		try {
		url = new URL(dlink);
		Authenticator.setDefault(new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username,password.toCharArray());
			}
		});
		} catch(Exception e) {
			
		}
	}
	
	public void run() {
		try {
			while(true) {
				
				String filename1 = "C:\\imgTest\\cropFace0.jpg";
				String filename2 = "C:\\imgTest\\cropFace1.jpg";
				String filename3 = "C:\\imgTest\\cropFace2.jpg";

				int ret;
				ret = compareFeature(filename1, filename3);
				int ret1;
				ret1 = compareFeature(filename2,filename3);
				
				frame = util.urlToMat(url);
				im.showImage(frame);
				if(faceDetection.getNFace(frame)>0) {
					state = 1;
					
					faceDetection.saveFaces(frame);
					sum = 0;
					if(ret >0 && dicaFlag == false) {
						System.out.println("디카프리오 입니다.");
						out.println("di1");
						out.println("do0");
						dicaFlag = true;
					} else if(ret == 0) {
						dicaFlag = false;
						if(ret1 > 0 && doheeFlag == false) {
							System.out.println("김도희입니다.");
							out.println("do1");
							out.println("di0");
							doheeFlag = true;
						} else if(ret1 == 0) {
							doheeFlag =false;
						}
					}
				} else
					sum++;
				
				if(sum>=10)
					state = 0;
				System.out.println(id + "번째의 state : " + state);
				out = new PrintWriter(client.getOutputStream(), true);
				out.print(state);
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int compareFeature(String filename1, String filename2) {
		int retVal = 0;
		long startTime = System.currentTimeMillis();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// Load images to compare
		Mat img1 = Imgcodecs.imread(filename1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		Mat img2 = Imgcodecs.imread(filename2, Imgcodecs.CV_LOAD_IMAGE_COLOR);

		// Declare key point of images
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		// Definition of ORB key point detector and descriptor extractors
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB); 
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

		// Detect key points
		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);
		
		// Extract descriptors
		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		// Definition of descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

		// Match points of two images
		MatOfDMatch matches = new MatOfDMatch();
//		System.out.println("Type of Image1= " + descriptors1.type() + ", Type of Image2= " + descriptors2.type());
//		System.out.println("Cols of Image1= " + descriptors1.cols() + ", Cols of Image2= " + descriptors2.cols());
		
		// Avoid to assertion failed
		// Assertion failed (type == src2.type() && src1.cols == src2.cols && (type == CV_32F || type == CV_8U)
		if (descriptors2.cols() == descriptors1.cols()) {
			matcher.match(descriptors1, descriptors2 ,matches);
	
			// Check matches of key points
			DMatch[] match = matches.toArray();
			double max_dist = 0; double min_dist = 100;
			
			for (int i = 0; i < descriptors1.rows(); i++) { 
				double dist = match[i].distance;
			    if( dist < min_dist ) min_dist = dist;
			    if( dist > max_dist ) max_dist = dist;
			}
			
		    // Extract good images (distances are under 10)
			for (int i = 0; i < descriptors1.rows(); i++) {
				if (match[i].distance <= 23) {
					retVal++;
				}
			}
		}
		
		return retVal;
	}
	
}