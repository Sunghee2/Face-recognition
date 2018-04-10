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
//	public static void main(String[] args) throws Exception {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		
//		String[] dlink = {"http://172.30.1.60/image.jpg", "http://172.30.1.19/image.jpg"};
//		
//		try {
//			ServerSocket server = new ServerSocket(1234);
//			int id = 0;
//			
//			while(true) {
//				Socket client = server.accept();
//				System.out.println("�겢�씪�씠�뼵�듃 �젒�냽 "+id);
//				Thread clientThread = new Thread(new EchoServerHandler(client, id, dlink[id]));
//				clientThread.start();
//				id++;
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
//		DBConnection db = new DBConnection();
//		db.insertImg(0, 1, 1);
//		db.insertImg(0, 2, 2);
//		db.insertImg(0, 3, 3);
//		db.retrieveImg();
//		
		
//		
//		int state = 0;
//		int sum =0;
//		int fileNo=0;
//		int faceNo = 4;
//		int id = 0;
//		int sum2 = 0;
//		
//		FaceDetection faceDetection = new FaceDetection();
//		ImShow im = new ImShow("Video Preview");
//		Utils util = new Utils();
//		
//		VideoCapture vcam = new VideoCapture(0);
//		Mat frame = new Mat();
//		DBConnection db = new DBConnection();
//		
//		im.Window.setResizable(true);
//		im.Window.setDefaultCloseOperation(3);
//		
//		while(vcam.isOpened()) {
//			vcam.read(frame);
//			im.showImage(frame);
//			if(faceDetection.getNFace(frame)>0) {  // �궗�엺�씠 �엳�쑝硫� �떎�뻾
//				state = 1;
//				
//				faceDetection.saveFaces(frame, "C:\\imgTest"+id+"\\", fileNo);
//				sum = 0;
//				
//				outerloop:
//					while(true) {
//						for(int i = 1; i <= faceNo; i++) {  // db�뿉 �엳�뒗 �뼹援� 媛쒖닔 留뚰겮 �룎由�.
//							if(i==faceNo) { // �뼹援댁씠 �뾾�쓣 寃쎌슦........ 異붽�
//								sum2++;
//								if (sum2 > 50 ) {
//									sum = 0;
//									db.insertImg(id, fileNo, faceNo);
//									faceNo++;
//									db.retrieveImg();
//								}
//							}
//							int ret = compareFeature("c:\\faceList\\" + i + ".jpg", "c:\\imgTest"+id+"\\cropFace"+fileNo+".jpg");
//							if(ret > 0) {
//								//�뼹援댁씠 留욎쓣 寃쎌슦
//								sum2 = 0;
//								System.out.println(i+"踰덉㎏ �궗�엺�엫");
//								break outerloop;
//							} 
//						}
//					}
//
//		}
//		fileNo++;
//	}
//		
//		
//		
//}
//	public static int compareFeature(String filename1, String filename2) {
//		int retVal = 0;
//		long startTime = System.currentTimeMillis();
//		
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		
//		// Load images to compare
//		Mat img1 = Imgcodecs.imread(filename1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//		Mat img2 = Imgcodecs.imread(filename2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//
//		// Declare key point of images
//		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//		Mat descriptors1 = new Mat();
//		Mat descriptors2 = new Mat();
//
//		// Definition of ORB key point detector and descriptor extractors
//		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB); 
//		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
//
//		// Detect key points
//		detector.detect(img1, keypoints1);
//		detector.detect(img2, keypoints2);
//		
//		// Extract descriptors
//		extractor.compute(img1, keypoints1, descriptors1);
//		extractor.compute(img2, keypoints2, descriptors2);
//
//		// Definition of descriptor matcher
//		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
//
//		// Match points of two images
//		MatOfDMatch matches = new MatOfDMatch();
////		System.out.println("Type of Image1= " + descriptors1.type() + ", Type of Image2= " + descriptors2.type());
////		System.out.println("Cols of Image1= " + descriptors1.cols() + ", Cols of Image2= " + descriptors2.cols());
//		
//		// Avoid to assertion failed
//		// Assertion failed (type == src2.type() && src1.cols == src2.cols && (type == CV_32F || type == CV_8U)
//		if (descriptors2.cols() == descriptors1.cols()) {
//			matcher.match(descriptors1, descriptors2 ,matches);
//	
//			// Check matches of key points
//			DMatch[] match = matches.toArray();
//			double max_dist = 0; double min_dist = 100;
//			
//			for (int i = 0; i < descriptors1.rows(); i++) { 
//				double dist = match[i].distance;
//			    if( dist < min_dist ) min_dist = dist;
//			    if( dist > max_dist ) max_dist = dist;
//			}
//			
//		    // Extract good images (distances are under 10)
//			for (int i = 0; i < descriptors1.rows(); i++) {
//				if (match[i].distance <= 23) {
//					retVal++;
//				}
//			}
//		}
//		
//		return retVal;
//	}

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
						//String inputLine = null;
						//inputLine = in.readLine();
						//System.out.println("占쎄깻占쎌뵬占쎌뵠占쎈섧占쎈뱜嚥∽옙 �겫占쏙옙苑� 獄쏆룇占� �눧紐꾩쁽占쎈였:" + inputLine);
						
						if(faceDetection.getNFace(frame)>0) {
							state = 1;
							faceDetection.saveFaces(frame);
							sum=0;
							if (ret > 0 && dicaFlag == false) {
								System.out.println("占쎈탵燁삳똾遊썹뵳�딆궎 占쎌뿯占쎈빍占쎈뼄.");
								writer.println("100");
								dicaFlag = true;
							} else if(ret == 0){
								dicaFlag=false;
								if (ret1 > 0 && doheeFlag == false) {
									System.out.println("繹먲옙占쎈즲占쎌뵕 占쎌뿯占쎈빍占쎈뼄.");
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
						System.out.println("獄쏆룇占� 占쎈쑓占쎌뵠占쎄숲 = "+data);
						if(data.equals("do")){
							data = reader.readLine();	
							if (0 < Integer.parseInt(data) && Integer.parseInt(data) < 4) {
								doheeVideoNum = Integer.parseInt(data);
				
								System.out.println("癰귣�沅� 占쎈쑓占쎌뵠占쎄숲 = 10");
								bMan.sendToAll(10);
								System.out.println("繹먲옙占쎈즲占쎌뵕占쎌벥 �뜮袁⑤탵占쎌궎 占쎄퐜甕곌쑬�뮉 : " + doheeVideoNum);
								writer.println(doheeVideoNum);
								bMan.sendToAll(doheeVideoNum);
								
							}
							data = reader.readLine();
							if (Integer.parseInt(data) >= 4) {
								doheeVideoPosition = Integer.parseInt(data);
								System.out.println("繹먲옙占쎈즲占쎌뵕占쎌벥 �뜮袁⑤탵占쎌궎 占쎈뻻揶쏄쑴占� : " + doheeVideoPosition);
//								writer.println(doheeVideoPosition);
								bMan.sendToAll(doheeVideoPosition);
							}
						} else if (data.equals("11")) {
							if (0 < Integer.parseInt(data) && Integer.parseInt(data) < 4) {
								dicaVideoNum = Integer.parseInt(data);
								System.out.println("占쎈탵燁삳똾遊썹뵳�딆궎占쎌벥 �뜮袁⑤탵占쎌궎 占쎄퐜甕곌쑬�뮉 : " + dicaVideoNum);
								writer.println(doheeVideoPosition);
							}
							if (Integer.parseInt(data) >= 4) {
								dicaVideoPosition = Integer.parseInt(data);
								System.out.println("占쎈탵燁삳똾遊썹뵳�딆궎占쎌벥 �뜮袁⑤탵占쎌궎 占쎈뻻揶쏄쑴占� : " + dicaVideoNum);
								writer.println(doheeVideoPosition);
							}
						}
						
						
						//if (inputLine.equals("quit")) //筌랃옙1占쎈튋 獄쏆룇占� 揶쏅�れ뵠 quit 占쎌뵬野껋럩�뒭 �넫�굝利�
						//break;
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

	
	