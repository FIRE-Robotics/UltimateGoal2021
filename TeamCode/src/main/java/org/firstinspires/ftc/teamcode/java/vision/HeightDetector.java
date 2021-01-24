package org.firstinspires.ftc.teamcode.java.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class HeightDetector {
    /**
     * This connects to the phone's camera to capture an image of the rings.
     * <p>
     * Currently, this uses the {@link OpenCvInternalCamera} class to temporally get
     * access to advanced features. However, when we switch to a web camera, this will have to be
     * refactored to use the webcam.
     */
    private OpenCvInternalCamera camera;
    private RingHeightPipeline pipeline;

    /**
     * A Simple constructor which creates a Ring Height Detector Instance
     * @param hardwareMap The {@link HardwareMap} Generated by OpMode to connect to the phone
     */
    public HeightDetector(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    /**
     * Initializes the Object and starts streaming (will be split up later)
     * <p>
     * This connects the camera to the pipeline and initializes development.
     * @param hardwareMap A HardwareMap which allows the creation of a phone instance
     */
    private void init(HardwareMap hardwareMap) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId",
                "id",
                hardwareMap.appContext.getPackageName()
        );
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(
                OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId
        );

        pipeline = new RingHeightPipeline();

        // Sets up the Camera to use the pipeline which detects the height of the rings
        camera.setPipeline(pipeline);

        camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
    }

    public void startStreaming() {
        // Starts Streaming the Camera Contents to the phone
        camera.openCameraDeviceAsync(() ->
                camera.startStreaming(320 /*320*/, 240 /*240*/, OpenCvCameraRotation.UPRIGHT)
        );
    }

    public void stopStreaming() {
        camera.stopStreaming();
    }

    public RingHeightPipeline.Height getHeight() {
        return pipeline.getHeight();
    }

    static class RingHeightPipeline extends OpenCvPipeline {

        public enum Height {
            A, // No Rings
            B, // 1 Ring
            C  // 3 Rings
        }

        // TODO: NEED TO CALIBRATE
        /**
         * This is the minimum threshold for Yellow/Orange which we will detect
         */
        static final Scalar YELLOW_MINIMUM = new Scalar(0, 141, 0); //TODO: Fine Tune

        /**
         * This is the maximum threshold for Yellow/Orange which we will detect
         */
        static final Scalar YELLOW_MAXIMUM = new Scalar(255, 230, 95); //TODO: Fine Tune

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

        Mat hsv = new Mat(), threshold = new Mat();

        // TODO: NEED TO CALIBRATE
        static double bMin = 100;
        static double cMin = 200;

        /**
         * Converts an RGB image to HSV and Calculates the Threshold Image to allow for yellow detection
         *
         * @param input The Input Frame to be Calculated From
         */
        void inputToHSV(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
            Core.inRange(input, YELLOW_MINIMUM, YELLOW_MAXIMUM, threshold);
        }

        /**
         * ProcessFrame takes each frame in the video to find the height of the stack
         * <p>
         * It first converts to HSV and processes the image to find all colors in a certain threshold.
         * After that is done, it calculates the area of the yellow in the frame to find out which
         * range it fits it, determining the height of the stack of rings and the position the robot
         * needs to move to.
         *
         * @param input The frame to make calculations off of.
         * @return A processed frame to display on the screen.
         */
        @Override
        public Mat processFrame(Mat input) {
            // The first thing that this does is that it converts the input to HSV. The HSV colorspace
            // Works much better for color detection as we can isolate all effect of light and solely
            // Look at the hue, making color easy to calculate.
            inputToHSV(input);

            // TODO Test if new implementation works
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(threshold, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            MatOfPoint cnt = (contours.size() == 2 ? contours.get(0) : contours.get(1));
            double area = 0;
            if (cnt.toArray().length != 0) {
                area = Imgproc.contourArea(cnt);
            }
            // Compare Size with min and max
            if (area > cMin) {
                height = Height.C;
            } else if (area > bMin) {
                height = Height.B;
            } else {
                height = Height.A;
            }

            return input;
        }

        public Height getHeight() {
            return height;
        }
    }
}
