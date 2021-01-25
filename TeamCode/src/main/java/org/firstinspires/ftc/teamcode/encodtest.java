package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

    /**
     * Created by Sarthak on 6/1/2019.
     * Odometry system calibration. Run this OpMode to generate the necessary constants to calculate the robot's global position on the field.
     * The Global Positioning Algorithm will not function and will throw an error if this program is not run first
     */
    @TeleOp(name = "Odometry System Calibration", group = "Calibration")
    public class encodtest extends LinearOpMode {

        private DcMotor FrontRight, FrontLeft, BackLeft, BackRight, verticalLeft, verticalRight, horizontal;

        @Override
        public void runOpMode() throws InterruptedException {
            FrontRight = hardwareMap.dcMotor.get("FrontRight");
            FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
            BackLeft = hardwareMap.dcMotor.get("BackLeft");
            BackRight = hardwareMap.dcMotor.get("BackRight");
            verticalLeft = hardwareMap.dcMotor.get("BackRight");
            verticalRight = hardwareMap.dcMotor.get("FrontLeft");
            horizontal = hardwareMap.dcMotor.get("FrontRight");

            waitForStart();

            while(opModeIsActive()){
                FrontLeft.setPower(1);
                BackLeft.setPower(1);
                FrontRight.setPower(1);
                BackRight.setPower(1);
                telemetry.addData("Vertical Left Position", -verticalLeft.getCurrentPosition());
                telemetry.addData("Vertical Right Position", verticalRight.getCurrentPosition());
                telemetry.addData("Horizontal Position", horizontal.getCurrentPosition());

                //Update values
                telemetry.update();
            }
        }

    }


