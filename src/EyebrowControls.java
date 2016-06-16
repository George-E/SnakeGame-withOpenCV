import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;  
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.util.Arrays;

import javax.imageio.ImageIO;  
import javax.swing.*;  

import org.opencv.core.Core;  
import org.opencv.core.Mat;  
import org.opencv.core.MatOfByte;  
import org.opencv.core.MatOfRect;  
import org.opencv.core.Point;  
import org.opencv.core.Rect;  
import org.opencv.core.Scalar;  
import org.opencv.core.Size;  
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;  
import org.opencv.objdetect.CascadeClassifier;  
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

class FacePanel extends JPanel{  
	private BufferedImage image;  

	public FacePanel(){  
		super();   
	}  

	public boolean matToBufferedImage(Mat matrix) {  
		MatOfByte mb =new MatOfByte();  
		Imgcodecs.imencode(".jpg", matrix, mb);  
		try {  
			image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
		} catch (IOException e) {  
			e.printStackTrace();  
			return false; // Error  
		}  
		return true; // Successful  
	}  

	public void paintComponent(Graphics g){  
		super.paintComponent(g);   
		if (image!=null)       
			g.drawImage(image,10,10,image.getWidth(),image.getHeight(), null);
	}
}  

class FaceDetector {  
	static int LEFT_EYEBROW_THRESH = 50;//change these
	static int RIGHT_EYEBROW_THRESH = 30;
	
	private CascadeClassifier face_cascade;  
	Rect[] oldFacesArray;
	public FaceDetector(){  
		face_cascade = new CascadeClassifier("classifiers/haarcascade_eye.xml"); 
	}  

	public Mat detectAndVisualize(Mat inputframe){  
		Mat mRgba=new Mat();  
		Mat mGrey=new Mat();  
		MatOfRect faces = new MatOfRect();  
		inputframe.copyTo(mRgba);  
		inputframe.copyTo(mGrey);  
		Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);  
		Imgproc.equalizeHist( mGrey, mGrey );  
		face_cascade.detectMultiScale(mGrey, faces, 1.1, 6, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(
				40,40), new Size(70,70)); 
		//System.out.println(String.format("Detected %s faces", faces.toArray().length));  
		Rect[] facesArray = faces.toArray();
		if (facesArray.length ==2 && Math.abs(facesArray[0].y-facesArray[1].y) <20)  
			oldFacesArray = facesArray.clone();
		else if (oldFacesArray != null)
			facesArray = oldFacesArray.clone();
		else
			return mRgba;
		int leftEye = (facesArray[0].x > facesArray[1].x)?0:1;
		int rightEye = (leftEye==1)?0:1;
		Imgproc .rectangle(mRgba, facesArray[leftEye].tl(), facesArray[leftEye].br(), new Scalar(0, 255, 0, 255), 3);
		Imgproc .rectangle(mRgba, facesArray[rightEye].tl(), facesArray[rightEye].br(), new Scalar(0, 0, 255, 255), 3);
		/////
		Point leftTopLeft = new Point(facesArray[leftEye].tl().x, facesArray[leftEye].tl().y-facesArray[leftEye].height/2);
		Point leftBottomRight = new Point(facesArray[leftEye].br().x, facesArray[leftEye].tl().y-10);
		Imgproc .rectangle(mRgba, leftTopLeft, leftBottomRight, new Scalar(255, 0, 0, 255), 3);
		Point rightTopLeft = new Point(facesArray[rightEye].tl().x, facesArray[rightEye].tl().y-facesArray[rightEye].height/2);
		Point rightBottomRight = new Point(facesArray[rightEye].br().x, facesArray[rightEye].tl().y-10);
		Imgproc .rectangle(mRgba, rightTopLeft, rightBottomRight, new Scalar(255, 0, 0, 255), 3);

		Rect left = new Rect(leftTopLeft, leftBottomRight);
		Rect right = new Rect(rightTopLeft, rightBottomRight);
		Mat leftCrop = new Mat(mGrey, left);
		Mat rightCrop = new Mat(mGrey, right);

		System.out.print(Arrays.toString(leftCrop.get(leftCrop.rows()/2, leftCrop.rows())) + "\t");
		System.out.println(Arrays.toString(rightCrop.get(rightCrop.rows()/2, rightCrop.rows())));
		if (leftCrop.get(leftCrop.rows()/2, leftCrop.rows())[0] <LEFT_EYEBROW_THRESH) {
			System.out.println("left eye brow up");
		} 
		if (rightCrop.get(rightCrop.rows()/2, rightCrop.rows())[0] <RIGHT_EYEBROW_THRESH) {
			System.out.println("right eye brow up");
		}

		return mRgba;  
	}  

	public void  detectAndMoveSnake(Mat inputframe){  
		MatOfRect faces = new MatOfRect();  
		Imgproc.cvtColor( inputframe, inputframe, Imgproc.COLOR_BGR2GRAY);  
		Imgproc.equalizeHist( inputframe, inputframe );  
		face_cascade.detectMultiScale(inputframe, faces, 1.1, 6, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(
				40,40), new Size(70,70)); 

		Rect[] facesArray = faces.toArray();
		if (facesArray.length ==2 && Math.abs(facesArray[0].y-facesArray[1].y) <20)  
			oldFacesArray = facesArray.clone();
		else if (oldFacesArray != null)
			facesArray = oldFacesArray.clone();
		else
			return;
		int leftEye = (facesArray[0].x > facesArray[1].x)?0:1;
		int rightEye = (leftEye==1)?0:1;
		Point leftTopLeft = new Point(facesArray[leftEye].tl().x, facesArray[leftEye].tl().y-facesArray[leftEye].height/2);
		Point leftBottomRight = new Point(facesArray[leftEye].br().x, facesArray[leftEye].tl().y-10);

		Point rightTopLeft = new Point(facesArray[rightEye].tl().x, facesArray[rightEye].tl().y-facesArray[rightEye].height/2);
		Point rightBottomRight = new Point(facesArray[rightEye].br().x, facesArray[rightEye].tl().y-10);

		Rect left = new Rect(leftTopLeft, leftBottomRight);
		Rect right = new Rect(rightTopLeft, rightBottomRight);
		Mat leftCrop = new Mat(inputframe, left);
		Mat rightCrop = new Mat(inputframe, right);

		//System.out.print(Arrays.toString(leftCrop.get(leftCrop.rows()/2, leftCrop.rows())) + "\t");
		//System.out.println(Arrays.toString(rightCrop.get(rightCrop.rows()/2, rightCrop.rows())));
		if (leftCrop.get(leftCrop.rows()/2, leftCrop.rows())[0] <LEFT_EYEBROW_THRESH) {
			//System.out.println("left eye brow up");
			Game.eyebrowReadings[2]++;
		} else if (rightCrop.get(rightCrop.rows()/2, rightCrop.rows())[0] <RIGHT_EYEBROW_THRESH) {
			//System.out.println("right eye brow up");
			Game.eyebrowReadings[0]++;
		}  else {
			//System.out.println("straight brows");
			Game.eyebrowReadings[1]++;
		}
	} 
}
public class EyebrowControls {  

	static JFrame frame = null; 
	static FacePanel facePanel = null; 
	static Mat webcam_image = null;

	//uncomment to test lighting
public static void main(String arg[]) {  
		activate(true, 100);
	}  

	public static void activate (boolean gui, int refreshSpeed)  {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		FaceDetector faceDetector=new FaceDetector(); 

		if (gui) {
			frame = new JFrame("WebCam Capture - Face detection");  
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
			frame.setSize(400,400); //give the frame some arbitrary size (changes later)
			frame.setBackground(Color.BLUE);
			facePanel = new FacePanel();  
			frame.add(facePanel,BorderLayout.CENTER);       
			frame.setVisible(true);       
		}

		//Open and Read from the video stream  
		webcam_image=new Mat();  
		VideoCapture webCam =new VideoCapture(0);   

		if( webCam.isOpened())  
		{  
			Timer t = new Timer(refreshSpeed, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					webCam.read(webcam_image);  
					if( !webcam_image.empty() )  
					{   
						if (Game.dead) {
							webCam.release(); 
							((Timer)e.getSource()).stop();
						}
						if (gui) {
							//Apply the classifier to the captured image  
							webcam_image=faceDetector.detectAndVisualize(webcam_image); 
							//Display the image  
							frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
							facePanel.matToBufferedImage(webcam_image);  
							facePanel.repaint(); 
						} else {
							//check for controls
							faceDetector.detectAndMoveSnake(webcam_image); 
						}
					} 
				}
			});
			t.start();
		} else
			webCam.release(); //release the webcam
	}  
}
