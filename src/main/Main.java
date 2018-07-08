package main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import DB.DBConnection;
import detection.FaceDetection;
import main.Main.BManager;
import main.Main.Connect_Thread;
import server.EchoServerHandler;
import utils.ImShow;
import utils.Utils;

public class Main {
	private int PORT = 7777;
	private ServerSocket server;
	private BManager bMan = new BManager();
	boolean doheeFlag = false;
	boolean dicaFlag = false;
	
	int doheeVideoNum;
	int doheeVideoPosition;
	int dicaVideoNum;
	int dicaVideoPosition;
	
	
	int state = 0;
	int sum=0;
	static int id = 0;
	String data;
	
	public Main() {
	
	}

	void startServer(String dlink) {
		try {
			server = new ServerSocket(PORT);
			System.out.println("start Server!");

			while (true) {
				Socket socket = server.accept();

				new Connect_Thread(socket, dlink).start();
				bMan.add(socket);
		
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String[] dlink = {"http://192.168.0.5/image.jpg","http://192.168.0.4/image.jpg"};
		Main server = new Main();
		while(id<2) {
			server.startServer(dlink[id]);

		}

	}

	public class Connect_Thread extends Thread {

		Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private String username = "admin";
		private String password = "MJU12345";
		Utils util = new Utils();
		URL url;
		
		
		
		Connect_Thread(Socket socket, String dlink) {
			this.socket = socket;
			try {
				url = new URL(dlink);
				Authenticator.setDefault(new Authenticator() {
					
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password.toCharArray());
					}
				});
			}catch(Exception e) {
				
			}
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				String msg = null;
				
				ImShow im = new ImShow("Video Preview");
				im.Window.setResizable(true);
				im.Window.setDefaultCloseOperation(3);

				Mat frame = new Mat();
				VideoCapture vcam = new VideoCapture(0);

				FaceDetection faceDetection = new FaceDetection();
				
				if (vcam.isOpened()) {
					Thread.sleep(500); 
					while(vcam.isOpened()) {
						
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
							sum=0;
							if (ret > 0 && dicaFlag == false) {
								writer.println("100");
								dicaFlag = true;
							} else if(ret == 0){
								dicaFlag=false;
								if (ret1 > 0 && doheeFlag == false) {
									writer.println("101");
									doheeFlag = true;
								}else if(ret1 == 0){
									doheeFlag = false;
								}
							}	
						}
						else
							sum++;
						
						if(sum>=10)
							state = 0;
						
						writer.println(state);
						System.out.println("state = "+state);
						
						
						data = reader.readLine();
						if(data.equals("do")){
							data = reader.readLine();	
							if (0 < Integer.parseInt(data) && Integer.parseInt(data) < 4) {
								doheeVideoNum = Integer.parseInt(data);
				
								bMan.sendToAll(10);
								writer.println(doheeVideoNum);
								bMan.sendToAll(doheeVideoNum);
								
							}
							data = reader.readLine();
							if (Integer.parseInt(data) >= 4) {
								doheeVideoPosition = Integer.parseInt(data);
//								writer.println(doheeVideoPosition);
								bMan.sendToAll(doheeVideoPosition);
							}
						} else if (data.equals("11")) {
							if (0 < Integer.parseInt(data) && Integer.parseInt(data) < 4) {
								dicaVideoNum = Integer.parseInt(data);
								writer.println(doheeVideoPosition);
							}
							if (Integer.parseInt(data) >= 4) {
								dicaVideoPosition = Integer.parseInt(data);
								writer.println(doheeVideoPosition);
							}
						}
						
						
					}
					vcam.release();
					writer.close();
					reader.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bMan.remove(socket);
					if (reader != null)
						reader.close();
					if (writer != null)
						writer.close();
					if (socket != null)
						socket.close();
					reader = null;
					writer = null;
					socket = null;

					System.out.println("Client Disconnected!");
				} catch (Exception e) {

				}		
			}
		}
	}

	class BManager extends Vector {
		BManager() {

		}

		void add(Socket sock) {
			super.add(sock);
		}

		void remove(Socket sock) {
			super.remove(sock);
		}

		synchronized void sendToAll(int msg) {
			PrintWriter writer = null;
			Socket sock;

			for (int i = 0; i < size(); i++) {
				sock = (Socket) elementAt(i);
				try {
					writer = new PrintWriter(sock.getOutputStream(), true);
				} catch (IOException ie) {
				}
				if (writer != null)
					writer.println(msg);
			}
		}
	}
	public static int compareFeature(String filename1, String filename2) {
		int retVal = 0;
		long startTime = System.currentTimeMillis();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
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
	
			DMatch[] match = matches.toArray();
			double max_dist = 0; double min_dist = 100;
			
			for (int i = 0; i < descriptors1.rows(); i++) { 
				double dist = match[i].distance;
			    if( dist < min_dist ) min_dist = dist;
			    if( dist > max_dist ) max_dist = dist;
			}
			
			for (int i = 0; i < descriptors1.rows(); i++) {
				if (match[i].distance <= 23) {
					retVal++;
				}
			}
		}
		
		return retVal;
	}

		
	}

	
	