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
    private DcMotor pull;
    private CRServo tread;

    //Wobble
    private Servo ClawPivot;
    private CRServo pinch;

    //Distribution
    private DcMotor LeftBlue;
    private DcMotor RightBlue;
    private DcMotor LinearAct;
    @Override
    public void runOpMode() throws InterruptedException
    {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");


        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        pivot = hardwareMap.servo.get("pivot");
        pull = hardwareMap.dcMotor.get("pull");
        tread = hardwareMap.crservo.get("tread");

        ClawPivot = hardwareMap.servo.get("ClawPivot");
        pinch = hardwareMap.crservo.get("pinch");


        LeftBlue = hardwareMap.dcMotor.get("LeftBlue");
        RightBlue = hardwareMap.dcMotor.get("RightBlue");
        LinearAct = hardwareMap.dcMotor.get("LinearAct");

        RightBlue.setDirection((DcMotor.Direction.REVERSE));
        waitForStart();

        while(opModeIsActive())
        {
            FrontLeft.setPower(gamepad1.left_stick_y);
            BackLeft.setPower(gamepad1.left_stick_y);
            FrontRight.setPower(gamepad1.right_stick_y);
            BackRight.setPower(gamepad1.right_stick_y);

            if(gamepad1.left_trigger < 0 || gamepad1.left_trigger > 0)  {
                FrontLeft.setPower(gamepad1.left_trigger);
                FrontRight.setPower(-gamepad1.left_trigger);
                BackLeft.setPower(-gamepad1.left_trigger);
                BackRight.setPower(gamepad1.left_trigger);
            }

            if(gamepad1.right_trigger < 0 || gamepad1.right_trigger > 0)  {
                FrontLeft.setPower(-gamepad1.right_trigger);
                FrontRight.setPower(gamepad1.right_trigger);
                BackLeft.setPower(gamepad1.right_trigger);
                BackRight.setPower(-gamepad1.right_trigger);
            }

            pivot.setPosition(1);

            if(gamepad1.a) {
                pull.setPower(1);

            } else if(gamepad1.b){
                pull.setPower(0);

            }

            if(gamepad2.dpad_down) {
                ClawPivot.setPosition(1);
            }
            else if(gamepad2.dpad_up){
                ClawPivot.setPosition(0);
            }
            if(gamepad2.x) {
                pinch.setPower(-1);
            }
            else if(gamepad2.y) {
                pinch.setPower(1);
            }
            else {
                pinch.setPower(0);
            }

            if(gamepad2.left_trigger == 1){
                LeftBlue.setPower(1);
                RightBlue.setPower(1);
            }
            else if(gamepad2.right_trigger == 1) {
                LeftBlue.setPower(-1);
                RightBlue.setPower(-1);
            }
            else if(gamepad2.left_stick_y == 1) {
                LeftBlue.setPower(0.1);
                RightBlue.setPower(0.1);
            }
            else if(gamepad2.left_stick_y == -1) {
                LeftBlue.setPower(-0.1);
                RightBlue.setPower(-0.1);
            }
            else {
                LeftBlue.setPower(0);
                RightBlue.setPower(0);
            }
            if(gamepad2.a) {
                LinearAct.setPower(1);
            }
            else if(gamepad2.b){
                LinearAct.setPower(-1);
            }
            else {
                LinearAct.setPower(0);
            }

            if(gamepad2.right_bumper) {
                tread.setPower(1);
            }
            else if(gamepad2.left_bumper) {
                tread.setPower(-1);
            }
            else {
                tread.setPower(0);
            }
            idle();
        }
    }

}
