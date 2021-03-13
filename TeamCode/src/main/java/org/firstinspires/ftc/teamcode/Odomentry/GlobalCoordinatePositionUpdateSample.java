package org.firstinspires.ftc.teamcode.Odomentry;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Sarthak on 6/1/2019.
 * Example OpMode that runs the GlobalCoordinatePosition thread and accesses the (x, y, theta) coordinate values
 */

@TeleOp(name = "Global Coordinate Position Test", group = "Calibration")
public class GlobalCoordinatePositionUpdateSample extends LinearOpMode {

    //Odometry encoder wheels
    DcMotor verticalRight, verticalLeft, horizontal;

    //The amount of encoder ticks for each inch the robot moves. This will change for each robot and needs to be changed here
    final double COUNTS_PER_INCH = 307.699557;

    //Hardware map names for the encoder wheels. Again, these will change for each robot and need to be updated below
    String verticalLeftEncoderName = "FrontRight", verticalRightEncoderName = "FrontLeft", horizontalEncoderName = "BackLeft";

    @Override
    public void runOpMode() throws InterruptedException {

        //Assign the hardware map to the odometry wheels
        verticalLeft = hardwareMap.dcMotor.get(verticalLeftEncoderName);
        verticalRight = hardwareMap.dcMotor.get(verticalRightEncoderName);
        horizontal = hardwareMap.dcMotor.get(horizontalEncoderName);

        //Reset the encoders
        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        /*
        Reverse the direction of the odometry wheels. THIS WILL CHANGE FOR EACH ROBOT. Adjust the direction (as needed) of each encoder wheel
        such that when the verticalLeft and verticalRight encoders spin forward, they return positive values, and when the
        horizontal encoder travels to the right, it returns positive value
        */
        verticalLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        horizontal.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set the mode of the odometry encoders to RUN_WITHOUT_ENCODER
        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Init complete
        telemetry.addData("Status", "Init Complete");
        telemetry.update();
        waitForStart();

        /*
          *****************
          OpMode Begins Here
          *****************
         */

        //Create and start GlobalCoordinatePosition thread to constantly update the global coordinate positions\
        OdometryGlobalCoordinatePosition globalPositionUpdate = new OdometryGlobalCoordinatePosition(verticalLeft, verticalRight, horizontal, COUNTS_PER_INCH, 75);
        Thread positionThread = new Thread(globalPositionUpdate);
        positionThread.start();

        while(opModeIsActive()){
            //Display Global (x, y, theta) coordinates
            telemetry.addData("X Position", globalPositionUpdate.returnXCoordinate() / COUNTS_PER_INCH);
            telemetry.addData("Y Position", globalPositionUpdate.returnYCoordinate() / COUNTS_PER_INCH);
            telemetry.addData("Orientation (Degrees)", globalPositionUpdate.returnOrientation());

            telemetry.addData("Vertical left encoder position", verticalLeft.getCurrentPosition());
            telemetry.addData("Vertical right encoder position", verticalRight.getCurrentPosition());
            telemetry.addData("horizontal encoder position", horizontal.getCurrentPosition());

            telemetry.addData("Thread Active", positionThread.isAlive());
            telemetry.update();
        }

        //Stop the thread
        globalPositionUpdate.stop();
    }

    /**
     * Created by Sarthak on 10/4/2019.
     */
    @TeleOp(name = "ODum")
    public static class Odum extends LinearOpMode {
        //Drive motors
        DcMotor right_front, right_back, left_front, left_back;
        //Odometry Wheels
        DcMotor verticalLeft, verticalRight, horizontal;

        final double COUNTS_PER_INCH = 307.699557;

        //Hardware Map Names for drive motors and odometry wheels. THIS WILL CHANGE ON EACH ROBOT, YOU NEED TO UPDATE THESE VALUES ACCORDINGLY
        String rfName = "FrontRight", rbName = "BackRight", lfName = "FrontLeft", lbName = "BackLeft";
        String verticalLeftEncoderName = rbName, verticalRightEncoderName = lfName, horizontalEncoderName = rfName;

        OdometryGlobalCoordinatePosition globalPositionUpdate;

        @Override
        public void runOpMode() throws InterruptedException {
            //Initialize hardware map values. PLEASE UPDATE THESE VALUES TO MATCH YOUR CONFIGURATION
            initDriveHardwareMap(rfName, rbName, lfName, lbName, verticalLeftEncoderName, verticalRightEncoderName, horizontalEncoderName);

            telemetry.addData("Status", "Init Complete");
            telemetry.update();
            waitForStart();

            //Create and start GlobalCoordinatePosition thread to constantly update the global coordinate positions
            globalPositionUpdate = new OdometryGlobalCoordinatePosition(verticalLeft, verticalRight, horizontal, COUNTS_PER_INCH, 75);
            Thread positionThread = new Thread(globalPositionUpdate);
            positionThread.start();

            globalPositionUpdate.reverseRightEncoder();
            globalPositionUpdate.reverseNormalEncoder();

            while(opModeIsActive()){
                //Display Global (x, y, theta) coordinates
                telemetry.addData("X Position", globalPositionUpdate.returnXCoordinate() / COUNTS_PER_INCH);
                telemetry.addData("Y Position", globalPositionUpdate.returnYCoordinate() / COUNTS_PER_INCH);
                telemetry.addData("Orientation (Degrees)", globalPositionUpdate.returnOrientation());

                telemetry.addData("Vertical left encoder position", verticalLeft.getCurrentPosition());
                telemetry.addData("Vertical right encoder position", verticalRight.getCurrentPosition());
                telemetry.addData("horizontal encoder position", horizontal.getCurrentPosition());

                telemetry.addData("Thread Active", positionThread.isAlive());
                telemetry.update();
            }

            //Stop the thread
            globalPositionUpdate.stop();

        }

        private void initDriveHardwareMap(String rfName, String rbName, String lfName, String lbName, String vlEncoderName, String vrEncoderName, String hEncoderName){
            right_front = hardwareMap.dcMotor.get(rfName);
            right_back = hardwareMap.dcMotor.get(rbName);
            left_front = hardwareMap.dcMotor.get(lfName);
            left_back = hardwareMap.dcMotor.get(lbName);

            verticalLeft = hardwareMap.dcMotor.get(vlEncoderName);
            verticalRight = hardwareMap.dcMotor.get(vrEncoderName);
            horizontal = hardwareMap.dcMotor.get(hEncoderName);

            right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


            right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            left_front.setDirection(DcMotorSimple.Direction.REVERSE);
            right_front.setDirection(DcMotorSimple.Direction.REVERSE);
            right_back.setDirection(DcMotorSimple.Direction.REVERSE);

            telemetry.addData("Status", "Hardware Map Init Complete");
            telemetry.update();
        }

        /**
         * Calculate the power in the x direction
         * @param desiredAngle angle on the x axis
         * @param speed robot's speed
         * @return the x vector
         */
        private double calculateX(double desiredAngle, double speed) {
            return Math.sin(Math.toRadians(desiredAngle)) * speed;
        }

        /**
         * Calculate the power in the y direction
         * @param desiredAngle angle on the y axis
         * @param speed robot's speed
         * @return the y vector
         */
        private double calculateY(double desiredAngle, double speed) {
            return Math.cos(Math.toRadians(desiredAngle)) * speed;
        }
    }
}