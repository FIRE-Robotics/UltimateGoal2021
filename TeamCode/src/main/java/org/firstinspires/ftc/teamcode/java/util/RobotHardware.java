package org.firstinspires.ftc.teamcode.java.util;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class RobotHardware {

    public BNO055IMU imu;

    public DcMotorEx frontLeftMotor = null;
    public DcMotorEx frontRightMotor = null;
    public DcMotorEx backLeftMotor = null;
    public DcMotorEx backRightMotor = null;

    public DcMotorEx rightShooter = null;
    public DcMotorEx leftShooter = null;
    public DcMotor intakeAndDelivery = null;



    public CRServo wobbleLift1 = null;
    public CRServo wobbleLift2 = null;
    public Servo wobbleGrip    =null;

    HardwareMap hardwareMap = null;
    private final ElapsedTime period = new ElapsedTime();

    /**
     * Sets up the HardwareMap
     *
     * @param hardwareMap is the hardware map
     */
    public void init(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        // imu set up parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Parts in hardware map
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);



        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        intakeAndDelivery = hardwareMap.get(DcMotor.class, "intakeAndDelivery");
        rightShooter = hardwareMap.get(DcMotorEx.class, "shooter2");
        leftShooter = hardwareMap.get(DcMotorEx.class, "shooter1");

        wobbleLift1 = hardwareMap.get(CRServo.class, "wobbleLift1");
        wobbleLift2 = hardwareMap.get(CRServo.class, "wobbleLift2");
        wobbleGrip = hardwareMap.get(Servo.class, "wobble");

        // Motor Directions
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        wobbleLift1.setDirection(DcMotor.Direction.FORWARD);
        wobbleLift2.setDirection(DcMotor.Direction.REVERSE);

        intakeAndDelivery.setDirection(DcMotor.Direction.FORWARD);

        rightShooter.setDirection(DcMotor.Direction.FORWARD);
        leftShooter.setDirection(DcMotor.Direction.FORWARD);

        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //Shooter
        rightShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeAndDelivery.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        // Turn off all motors
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        intakeAndDelivery.setPower(0);

        rightShooter.setPower(0);
        leftShooter.setPower(0);

        wobbleLift1.setPower(0);
        wobbleLift2.setPower(0);
    }
}
