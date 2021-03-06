package org.firstinspires.ftc.teamcode.java.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.java.movement.*;
import org.firstinspires.ftc.teamcode.java.movement.AutoDriving;
import org.firstinspires.ftc.teamcode.java.movement.AutoDrivingNew;
import org.firstinspires.ftc.teamcode.java.util.Angle;
import org.firstinspires.ftc.teamcode.java.util.MovementData;
import org.firstinspires.ftc.teamcode.java.util.PositionControl.PidfConstants;
import org.firstinspires.ftc.teamcode.java.util.RobotHardware;

import static org.firstinspires.ftc.teamcode.java.util.PositionControl.PidfConstants.USDrive;
import static org.firstinspires.ftc.teamcode.java.util.PositionControl.PidfConstants.USStrafe;
import static org.firstinspires.ftc.teamcode.java.util.PositionControl.PidfConstants.USTurn;


//To fix error perhaps flip the switch

@Autonomous(name = "AutoDrivingTest", group = "Backup")

public class AutoDrivingTest extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    // Declare OpMode members.
    RobotHardware robot = new RobotHardware();
    //Hardware robot = new Hardware();
//    private DcMotor frontRightMotor;
//    private DcMotor frontLeftMotor;
//    private DcMotor backLeftMotor;
//    private DcMotor backRightMotor;
    private BNO055IMU imu;
    private CRServo pl;
    private CRServo pn;
    private ActiveLocation AL;
    private Thread locationThread;
    //    private PathFinder PF;
//    private Thread pathThread;
    private AutoDrivingNew autoDriving;
    //  private PidfController PIDFDrive;
    //private PidfController PIDFStrafe;
    //private PidfController PIDFTurn;

    private boolean location;

    //private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
    	telemetry.addData("Test", "Test");
    	telemetry.update();
//    	sleep(10000);
        location = true;
        robot.init(hardwareMap);
        //  pl = robot.lShooter;
        imu = robot.imu;
//
//        frontLeftMotor = robot.frontLeftMotor;
//        frontRightMotor = robot.frontRightMotor;
//        backRightMotor = robot.backRightMotor;
//        backLeftMotor = robot.backLeftMotor;


        AL = new ActiveLocation(robot);
        locationThread = new Thread(AL);
        locationThread.start();
        //autoAdjusting= new AutoAdjusting(robot,AL,Side.RED,this);

//        PF = new PathFinder(AL);
//        pathThread = new Thread(PF);
//        pathThread.start();
//
//         */
//        //12096
//        PIDFDrive = PositionPidfConstants.USDrive;//new PIDFController(0.0011844/*96004999*/, 0.0000000000000, 0.00150719/*423*/, 0); //003,000001,003705
//        //PIDFDrive = new PIDFController(0.00, 0.000000, 0.00, 0);
//        PIDFStrafe = PositionPidfConstants.USStrafe;//new PIDFController(0.001705, 0.000000000000001, 0.005705, 0);
//        PIDFTurn = PositionPidfConstants.USTurn; //new PIDFController(0.35, 0.000000000, 0.395, 0); //38
//        //PIDFTurn = new PIDFController(0, 0, 0, 0); //38
//
//        autoDriving = new AutoDriving(PIDFDrive, PIDFStrafe, PIDFTurn, robot);


        //12096
        //PIDFDrive = PidfConstants.USDrive; //new PIDFController(0.0011844/*96004999*/, 0.0000000000000, 0.00150719/*423*/, 0); //003,000001,003705
        //PIDFDrive = new PIDFController(0.00, 0.000000, 0.00, 0);
        //    PIDFStrafe = PidfConstants.USStrafe;//new PIDFController(0.001705, 0.000000000000001, 0.005705, 0);
        //  PIDFTurn = PidfConstants.USTurn; //new PIDFController(0.35, 0.000000000, 0.395, 0); //38
        //PIDFTurn = new PIDFController(0, 0, 0, 0); //38

         autoDriving = new AutoDrivingNew(USDrive, USStrafe, USTurn, robot, telemetry);
//        autoDriving.telemetry = telemetry;
        //autoDriving = new AutoDriving(PIDFDrive, PIDFStrafe, PIDFTurn,robot);
//        telemetry.addData("Average", PidfConstants.average);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

//        sleep(1000);

        waitForStart();
        runtime.reset();
        int movement = 0;


        telemetry.addData("Test", "1");
        telemetry.update();
        //while (opModeIsActive()) {
        // run until the end of the match (driver presses STOP)
//        try {

//        movement = 1;
//        while (opModeIsActive()) {
//            if (movement == 1) {
//                telemetry.addData("Test", "qwertyuytresdcvbhjuytrdbfghtytfvcbvbmgjhfgbfng");
//                telemetry.update();
//            }
//            movement = 2;
//        }
        //AL.setStartPosition(0, 0);
        //PF.setDestination(600,600);
            /*
            telemetry.addData("FL", frontLeftMotor.getCurrentPosition());
            telemetry.addData("BR", backRightMotor.getCurrentPosition());
            telemetry.addData("FR", frontRightMotor.getCurrentPosition());
            telemetry.addData("BL", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }*/
        //telemetry.addData("X", AL.getFieldX());
        //telemetry.addData("Y", AL.getFieldY())r
        //telemetry.addData("Angle", AL.getAngle());
        //telemetry.addData("Path: ", PF.getEncoderPath());
        //telemetry.update();

        while (opModeIsActive() && !isStopRequested()) {
            //frontLeftMotor.setPower(.29);
            autoDriving.setStartLocation(new MovementData(0, 0, Angle.fromDegrees(0)));
//                autoDriving.setStartLocation(0,0,0);
            autoDriving.setDefaultErrorRanges(new MovementData(6000, 6000, Angle.fromDegrees(0, false)));
//                autoDriving.setDefaultErrorRanges(50,100,7);
            autoDriving.stopAt(new MovementData(0, 0, Angle.fromDegrees(30)), 0.9);
            telemetry.addData("Moving", "true");
            telemetry.update();
//                autoDriving.stopAt(new MovementData(0,0,Angle.fromDegrees(0)),.90);
            movement += 1; //??? Might cause issue
            //autoDriving.driveX(600);
//                autoDriving.stopAt(MovementData.withDegrees(-600, 00,0), .3);
//                sleep(1000);
//                autoDriving.stopAt(MovementData.withDegrees(600, 00,0), .3);
//                sleep(1000);
//                autoDriving.stopAt(MovementData.withDegrees(0, 00,0), .3);
//                movement = 2;
            //autoDriving.turnOff();

//                if (movement == 0) {
//                    location = true;
//                    //String report = autoDriving.stopAt(MovementData.withDegrees(00, 00, ), .3);
//                    stat = autoDriving.stopAt(MovementData.withDegrees(00, 600,0 ), .3);
//                    //String report = autoDriving.errorReport(MovementData.withDegrees(600, 0, 0));
//                    //telemetry.addData("Error Report", report);
//                    //telemetry.update();
//                }else if (movement == 1){
//                    //telemetry.speak("In");
//                    stat = autoDriving.stopAt(MovementData.withDegrees(00, 00,00 ), .3);
//                }else{
//                    autoDriving.turnOff();
//                }
//                telemetry.speak(""+movement+" "+stat);
//                if (stat){
//                    movement+=1;
//                    sleep(1000);
//                    stat = false;
//                }
//                telemetry.addData("Report ", autoDriving.errorReport(0,0,0));
//                telemetry.update();


            //String report = autoDriving.errorReport(new MovementData(600, 600, 90));
            //telemetry.addData("Error Report", report);
            //telemetry.speak("Hello" + stat);
//            telemetry.addData("Angle", activeLocation.getAngle());
            /*telemetry.addData("FL", frontLeftMotor.getPower());
            telemetry.addData("FR", frontRightMotor.getPower());
            telemetry.addData("BL", backLefftMotor.getPower());
            telemetry.addData("BR", backRightMotor.getPower());

             */
            telemetry.update();
            //sleep(200);

            if (runtime.milliseconds() >= 29000 || movement >= 1) {
                location = false;
                //                frontRightMotor.setPower(0);
                //               frontLeftMotor.setPower(0);
                //             backRightMotor.setPower(0);
                //           backLeftMotor.setPower(0);
                //String report = autoDriving.errorReport(MovementData.withDegrees(0, 600, 0));
                //telemetry.addData("Error Report", report);
                //frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                //backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.speak("Done");
                telemetry.update();
                //requestOpModeStop();
                //AL.Stop();
                //PF.stop();

            }

        }
        //AL.Stop();
        //PF.stop();

    }
}
