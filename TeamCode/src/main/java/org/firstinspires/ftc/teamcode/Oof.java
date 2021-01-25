package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "oof")
public class Oof extends LinearOpMode {
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    Servo ClawPivot, pinch;

    @Override
    public void runOpMode() throws InterruptedException {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");

        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        ClawPivot = hardwareMap.servo.get("ClawPivot");
        pinch = hardwareMap.servo.get("pinch");

        ClawPivot.setDirection(Servo.Direction.REVERSE);
        pinch.setDirection(Servo.Direction.REVERSE);

        waitForStart();
        StrafeLeft(1, 300);
        ClawPivot.setPosition(1);
        sleep(1500);
        DriveForward(1,200);
        pinch.setPosition(1);
        sleep(500);
        DriveForward(1,3000);
        pinch.setPosition(0);
        sleep(500);
        DriveBackwards(1,1000);



    }
    public void DriveForward(double power,long sleep) {
        FrontLeft.setPower(power);
        FrontRight.setPower(power);
        BackLeft.setPower(power);
        BackRight.setPower(power);
        sleep(sleep);
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
    public void DriveBackwards(double power, long sleep) {
        FrontLeft.setPower(-power);
        FrontRight.setPower(-power);
        BackLeft.setPower(-power);
        BackRight.setPower(-power);
        sleep(sleep);
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
    public void StrafeLeft(double power, long sleep) {
        FrontLeft.setPower(power);
        FrontRight.setPower(-power);
        BackLeft.setPower(-power);
        BackRight.setPower(power);
        sleep(sleep);
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
    public void StrafeRight(double power, long sleep) {
        FrontLeft.setPower(-power);
        FrontRight.setPower(power);
        BackLeft.setPower(power);
        BackRight.setPower(-power);
        sleep(sleep);
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
}
