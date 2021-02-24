package org.firstinspires.ftc.teamcode.java.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RingHeightPipeline extends OpenCvPipeline {

	Telemetry telemetry;

	public enum Height {
		A, // No Rings
		B, // 1 Ring
		C; // 4 Rings

		@Override
		public String toString() {
			switch (this) {
				case A:
					return "A: No Rings";
				case B:
					return "B: 1 Ring";
				case C:
					return "C: 3 Rings";
				default:
					return "No Height Selected";
			}
		}
	}

	public RingHeightPipeline(Telemetry telemetry) {
		this.telemetry = telemetry;
	}

	public RingHeightPipeline() {
		this.telemetry = null;
	}

	/**
	 * This is the minimum threshold for Yellow/Orange which we will detect
	 */
//	static final Scalar YELLOW_MINIMUM = new Scalar(50, 44.5, 30); //TODO: Fine Tune
	//Scalar YELLOW_MINIMUM = new Scalar(140, 130, 83); //TODO: Fine Tune
	//Scalar YELLOW_MINIMUM = new Scalar(130, 160, 30);
    //rgb(156, 89, 60)
	//165 150 95
	int rstart = 15;
	int gstart = 15;
	int bstart = 30;
	Scalar YELLOW_MINIMUM = new Scalar(rstart, gstart, bstart);
	//145 165 165
	// 161 155 155

	//165 190 180
	//180

	//165 150 95
	//145 165 165
	// 161 155 155

	//165 190 180
	//180
	/**
	 * This is the maximum threshold for Yellow/Orange which we will detect
	 */
//	static final Scalar YELLOW_MAXIMUM = new Scalar(230, 172, 157.5); //TODO: Fine Tune
	//Scalar YELLOW_MAXIMUM = new Scalar(160, 150, 103); //TODO: Fine Tune
	Scalar YELLOW_MAXIMUM = new Scalar(rstart+20, gstart+20, rstart+20);
	//Scalar YELLOW_MAXIMUM = new Scalar(220, 200, 100);

	final static int inc = 15;

	public void updateMin() {
		double[] thing = YELLOW_MINIMUM.val;
		double[] newMax = YELLOW_MAXIMUM.val;

		if (thing[0] < 255) {
			thing[0]+=inc;
			newMax[0]+=inc;
		} else if (thing[1] < 255) {
			thing[1]+=inc;
			newMax[1]+=inc;
			thing[0] = rstart;
			newMax[0] = rstart+20;
		} else if (newMax[2] < 255) {
			thing[2]+=inc/3;
			newMax[2]+=inc/3;
			thing[1] = gstart;
			newMax[1] = gstart+20;
		}

		YELLOW_MINIMUM = new Scalar(thing);
		YELLOW_MAXIMUM = new Scalar(newMax);

		if (telemetry != null) {
			telemetry.addData("Current Minimum", Arrays.toString(thing));
			telemetry.update();
		}
	}

	/**
	 * The Width of the Camera, defaulted to 320 pixels
	 */
	static final int CAMERA_WIDTH = 320;

	/**
	 * The Divider is used to divide the portion of the area considered and not considered
	 */
	static final int DIVIDER = (int) (100. / 320. * CAMERA_WIDTH);

	/**
	 * A Calibration Factor to ensure that a full ring is detected
	 */
	static final int MINIMUM_WIDTH = (int) (50. / 320. * CAMERA_WIDTH);

	/**
	 * A Factor Used to determine the height of a stack of rings
	 */
	static final double HEIGHT_FACTOR = 0.7;

	/**
	 * This will store the value of the height of the stack to allow us to know where to move the robot.
	 */
	private volatile Height height = Height.A;

	Mat yCrCb = new Mat();
	


	@Override
	public void init(Mat mat) {
		super.init(mat);
//		telemetry.speak("Never gonna give you up\n" +
//				"Never gonna let you down\n" +
//				"Never gonna run around and desert you\n" +
//				"Never gonna make you cry\n" +
//				"Never gonna say goodbye\n" +
//				"Never gonna tell a lie and hurt you");
	}

	/**
	 * ProcessFrame takes each frame in the video to find the height of the stack
	 * <p>
	 * It first converts to YCrCb and processes the image to find all colors in a certain threshold.
	 * After that is done, it calculates the area of the yellow in the frame to find out which
	 * range it fits it, determining the height of the stack of rings and the position the robot
	 * needs to move to.
	 *
	 * @param input The frame to make calculations off of.
	 * @return A processed frame to display on the screen.
	 */
	@Override
	public Mat processFrame(Mat input) {
		// The first thing that this does is that it converts the input to YCrCb. The YCrCb colorspace
		// works much better in this situation because it will help significantly with
		// calculating thresholds
		Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

		// Creates a mask mat, which finds all the images within the range of colors. The mat
		// divides the data into black and white, with white being the pixels in the threshold
		Mat mask = new Mat(yCrCb.rows(), yCrCb.cols(), CvType.CV_8UC1);
		Core.inRange(yCrCb, YELLOW_MINIMUM, YELLOW_MAXIMUM, mask);

		// Use a Blur to remove noise in the frame, such as slight gaps in the rings, shadows,
		// lighting, and just make calculations easier.
		Imgproc.GaussianBlur(mask, mask, new Size(5., 15.), 0);

		// Now, after blurring, we can proceed to find the contours in the image
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(
				mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE
		);

		// Since there might be multiple rectangles within the camera scan, we find the
		// widest rectangle, the one most probably equated to the rings. We also factor in the
		// Minimum Ring Width to factor out objects not relevant in the scan, including other
		// robots and rings not on the primary stack.
		int maximumWidth = 0;
		Rect maximumRectangle = new Rect();
		for (MatOfPoint contour : contours) {
			MatOfPoint2f copy = new MatOfPoint2f(contour.toArray());
			Rect rectangle = Imgproc.boundingRect(copy);

			int width = rectangle.width;
			// Makes sure that the Rectangle is above the Horizontal Divider (a.k.a. Horizon)
			if (width > maximumWidth && rectangle.y + rectangle.height > DIVIDER) {
				maximumWidth = width;
				maximumRectangle = rectangle;
			}

			// Release buffers of the contour and the copy MatOfPoint to optimize memory usage,
			// since after the preceding comparisons and checks, we will not use this instance
			// of the variables
			contour.release();
			copy.release();
		}

		// NOTE: the reason that a DIVIDER exists is because YCrCb is unreliable when differentiating
		// between RED and ORANGE. Thus, the DIVIDER prevents the Pipeline from detecting the RED
		// Goal as a ring.

		// Now that we have a working rectangle, we can perform an aspect ratio test to determine
		// the height of the stack relative to the width, giving us a good measure of how many
		// rings there are.
		double aspectRatio = (double) maximumRectangle.height / maximumRectangle.width;
		height = (maximumWidth >= MINIMUM_WIDTH ? (aspectRatio > HEIGHT_FACTOR ? Height.C : Height.B) : Height.A);

		// The Above Ternary Expression Might be a little confusing to those with less experience
		// reading code, so the following logic tree represents the same situation:
		//                     Maximum Rectangle Width > Minimum Width?
		//                                /                       \
		//                               /                         \
		//                             NO                          YES
		//                         There is No              Is the Aspect Ratio
		//                            Ring              Greater than the Height Factor
		//                                                       /        \
		//                                                      /          \
		//                                                    NO           YES
		//                                              There is only     There is a full
		//                                                one ring         stack of ring

		updateMin();
//		telemetry.addData("Min",YELLOW_MINIMUM);
//		telemetry.addData("max", YELLOW_MAXIMUM);
//		if (height == Height.C){
//			//telemetry.speak("Gottem");
////			telemetry.speak(""+YELLOW_MAXIMUM);
//			telemetry.addData("Values",YELLOW_MINIMUM);
//
//		}
//		telemetry.update();
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			// Do Nothing
//		}
		return mask;
//		return input;
	}

	public Height getHeight() {
		return height;
	}
}
