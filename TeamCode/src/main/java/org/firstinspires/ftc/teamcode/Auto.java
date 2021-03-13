/*
 * Copyright (c) 2020 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Auto")
public class Auto extends LinearOpMode
{
    OpenCvWebcam webCam;
    SkystoneDeterminationPipeline pipeline;
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight, LeftBlue, RightBlue;
    Servo ClawPivot, pinch;
    CRServo tread;

    @Override
    public void runOpMode()
    {
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");

        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        ClawPivot = hardwareMap.servo.get("ClawPivot");
        pinch = hardwareMap.servo.get("pinch");
        tread = hardwareMap.crservo.get("tread");

        ClawPivot.setDirection(Servo.Direction.REVERSE);
        pinch.setDirection(Servo.Direction.REVERSE);

        LeftBlue = hardwareMap.dcMotor.get("LeftBlue");
        RightBlue = hardwareMap.dcMotor.get("RightBlue");

        RightBlue.setDirection((DcMotor.Direction.REVERSE));

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "cam"), cameraMonitorViewId);
        pipeline = new SkystoneDeterminationPipeline();
        webCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        webCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        webCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
        });

        waitForStart();

        sleep(1000);
        if(pipeline.position == SkystoneDeterminationPipeline.RingPosition.FOUR) {
            StrafeLeft(1, 260);
            ClawPivot.setPosition(0.5);
            sleep(1500);
            DriveForward(1,200);
            sleep(1000);
            pinch.setPosition(1);
            sleep(500);
            ClawPivot.setPosition(1);
            sleep(1000);
            StrafeLeft(1,270);
            DriveForward(1,2275);
            ClawPivot.setPosition(0.5);
            sleep(1000);
            pinch.setPosition(0);
            sleep(1000);
            DriveBackwards(1,1350);
            StrafeRight(1,300);
            shoot();
            DriveForward(1, 150);
            stop();
        }
        else if(pipeline.position == SkystoneDeterminationPipeline.RingPosition.ONE) {

            StrafeLeft(1, 260);
            ClawPivot.setPosition(0.5);
            sleep(1500);
            DriveForward(1,200);
            sleep(1000);
            pinch.setPosition(1);
            sleep(500);
            ClawPivot.setPosition(1);
            sleep(1000);
            DriveForward(1,1650);
            StrafeRight(1,450);
            ClawPivot.setPosition(0.5);
            sleep(1000);
            pinch.setPosition(0);
            sleep(1000);
            DriveBackwards(1,750);
            StrafeLeft(1,500);
            shoot();
            DriveForward(1,250);
            stop();

        }

        else if(pipeline.position == SkystoneDeterminationPipeline.RingPosition.NONE) {
            StrafeLeft(1, 260);
            ClawPivot.setPosition(0.5);
            sleep(1500);
            DriveForward(1,200);
            sleep(1000);
            pinch.setPosition(1);
            sleep(500);
            ClawPivot.setPosition(1);
            StrafeLeft(1,350);
            DriveForward(1,1150);
            ClawPivot.setPosition(0.5);
            sleep(1000);
            pinch.setPosition(0);
            sleep(1000);
            DriveBackwards(1,450);
            StrafeRight(1,300);
            shoot();
            DriveForward(1, 300);
            stop();

        }


        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("Position", pipeline.position);
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);

        }
    }

    public static class SkystoneDeterminationPipeline extends OpenCvPipeline
    {
        /*
         * An enum to define the skystone position
         */
        public enum RingPosition
        {
            FOUR,
            ONE,
            NONE
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(181,98);

        static final int REGION_WIDTH = 45;
        static final int REGION_HEIGHT = 35;

        final int FOUR_RING_THRESHOLD = 145;
        final int ONE_RING_THRESHOLD = 128;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        int avg1;

        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile RingPosition position = RingPosition.FOUR;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = RingPosition.FOUR; // Record our analysis
            if(avg1 > FOUR_RING_THRESHOLD){
                position = RingPosition.FOUR;
            }else if (avg1 > ONE_RING_THRESHOLD){
                position = RingPosition.ONE;
            }else{
                position = RingPosition.NONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis()
        {
            return avg1;
        }
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
    public void shoot() {
        RightBlue.setPower(1);
        LeftBlue.setPower(1);
        sleep(2000);
        tread.setPower(1);
        sleep(3000);
        RightBlue.setPower(0);
        LeftBlue.setPower(0);
        tread.setPower(0);
        sleep(500);
    }
}