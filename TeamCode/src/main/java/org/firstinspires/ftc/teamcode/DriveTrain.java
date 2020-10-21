package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "DriveTrain")
public class DriveTrain extends LinearOpMode
{
    //Drive Motors
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    //Collection Servos
    private Servo servo;
    private CRServo pull;

    //Wobble
    private DcMotor ClawPivot;
    private Servo pinch;
    @Override
    public void runOpMode() throws InterruptedException
    {
        motorFrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("FrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("BackLeft");
        motorBackRight = hardwareMap.dcMotor.get("BackRight");


        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);

        servo = hardwareMap.servo.get("pivot");
        pull = hardwareMap.crservo.get("pull");

        ClawPivot = hardwareMap.dcMotor.get("ClawPivot");
        pinch = hardwareMap.servo.get("pinch");
        waitForStart();

        while(opModeIsActive())
        {
            motorFrontLeft.setPower(-gamepad1.left_stick_y);
            motorBackLeft.setPower(-gamepad1.left_stick_y);
            motorFrontRight.setPower(-gamepad1.right_stick_y);
            motorBackRight.setPower(-gamepad1.right_stick_y);

            servo.setPosition(0.8);

            if(gamepad1.a) {
                pull.setPower(1);
            } else {
                pull.setPower(0);
            }

            ClawPivot.setPower(gamepad1.left_trigger);
            if(gamepad1.x) {
                pinch.setPosition(0.5);
            }
            idle();
        }
    }

}
