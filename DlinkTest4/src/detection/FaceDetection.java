package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {

	CascadeClassifier faceDetector = new CascadeClassifier(
			"resources/haarcascade_frontalface_alt.xml");
	MatOfRect faceDetections;
	Rect rectCrop = null;
	Mat face = null;
	String path = "C:\\imgTest\\";
	int i = 0;
	
	
	public FaceDetection() {
		faceDetections = new MatOfRect();
	}

	public MatOfRect detectFaces(Mat m) {
		faceDetector.detectMultiScale(m, faceDetections);
		return faceDetections;
	}

	public int getNFace(Mat m) {
		faceDetector.detectMultiScale(m, faceDetections);
		return faceDetections.toArray().length;
	}

	public void DrawRect(Mat frame1) {
		faceDetector.detectMultiScale(frame1, faceDetections);

		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(frame1, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
	}
	
	public void saveFaces(Mat frame) {
		faceDetector.detectMultiScale(frame, faceDetections);
		
		for(Rect rect : faceDetections.toArray()) {
			String filename = "cropFace" + i + ".jpg";
			rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
			face = new Mat(frame, rectCrop);
			Imgcodecs.imwrite(path + filename, face);
			System.out.println(filename);
			
			i++;
		}
	}
	
	public int getNSavedFaces() {
		return i;
	}
}
