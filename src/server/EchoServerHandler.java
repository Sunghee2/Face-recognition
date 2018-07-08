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

import DB.DBConnection;

public class EchoServerHandler extends Thread {	
	private URL url;
	private String username = "admin";
	private String password = "MJU12345";
	private int id;
	private Socket client;
	private int state = 0;
	private int sum = 0;
	private int fileNo = 0;
	private int faceNo = 4;

	PrintWriter out;
	
	FaceDetection faceDetection = new FaceDetection();
	ImShow im = new ImShow("Video Preview");
	Utils util = new Utils();
	Mat frame = new Mat();
	DBConnection db = new DBConnection();
	
	
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

				
				frame = util.urlToMat(url);
				im.showImage(frame);
				if(faceDetection.getNFace(frame)>0) {  // 사람이 있으면 실행
					state = 1;
					
					faceDetection.saveFaces(frame, "C:\\imgTest"+id+"\\", fileNo);
					sum = 0;
					
					for(int i = 1; i == faceNo; i++) {  // db에 있는 얼굴 개수 만큼 돌림.
						if(i==faceNo) { // 얼굴이 없을 경우 추가
							db.insertImg(id, fileNo, faceNo);
							faceNo++;
							db.retrieveImg();
							break;
						}
						int ret = compareFeature("c:\\faceList\\" + i + ".jpg", "c:\\imgTest"+id+"\\cropFace"+fileNo+".jpg");
						if(ret > 0) {
							//얼굴이 맞을 경우
							System.out.println(i+"번째 사람");
							break;
						} 
					}
					
					fileNo++;
				} else
					sum++;
				
				if(sum>=10)
					state = 0;
				System.out.println(id + "��°�� state : " + state);
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

		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB); 
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);
		
		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

		MatOfDMatch matches = new MatOfDMatch();
		
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