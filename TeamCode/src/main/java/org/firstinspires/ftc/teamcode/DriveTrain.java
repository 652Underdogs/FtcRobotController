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
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor BackLeft;
    private DcMotor BackRight;
    //Collection Servos
    private Servo pivot;
    private CRServo pull;

    //Wobble
    private DcMotor ClawPivot;
    private Servo pinch;
    @Override
    public void runOpMode() throws InterruptedException
    {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");


        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);

        pivot = hardwareMap.servo.get("pivot");
        pull = hardwareMap.crservo.get("pull");
        int pullPower = 0;

        ClawPivot = hardwareMap.dcMotor.get("ClawPivot");
        pinch = hardwareMap.servo.get("pinch");
        waitForStart();

        while(opModeIsActive())
        {
            FrontLeft.setPower(-gamepad1.left_stick_y);
            BackLeft.setPower(-gamepad1.left_stick_y);
            FrontRight.setPower(-gamepad1.right_stick_y);
            BackRight.setPower(-gamepad1.right_stick_y);

            pivot.setPosition(0.8);
            pull.setPower(pullPower);

            if(gamepad1.a) {
                pullPower = 1;
            } else if (gamepad1.b){
                pullPower = 0;
            }

            ClawPivot.setPower(gamepad1.left_trigger);
            if(gamepad1.x) {
                pinch.setPosition(0.5);
            }
            idle();
        }
    }

}
