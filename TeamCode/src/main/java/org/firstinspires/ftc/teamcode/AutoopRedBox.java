package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class AutoopRedBox extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.get(DcMotor.class, "fl_drive");
        rightFront = hardwareMap.get(DcMotor.class, "fr_drive");
        leftBack = hardwareMap.get(DcMotor.class, "rl_drive");
        rightBack = hardwareMap.get(DcMotor.class, "rr_drive");

        waitForStart();

        if (opModeIsActive()) {
            // Put code here

            stopMotors();
        }
    }

    private void driveForward(double leftPower, double rightPower, long duration) {
        leftFront.setPower(leftPower);
        rightFront.setPower(rightPower);
        leftBack.setPower(leftPower);
        rightBack.setPower(rightPower);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void driveForward(double power, long duration) {
        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void driveBackward(double leftPower, double rightPower, long duration) {
        leftFront.setPower(-leftPower);
        rightFront.setPower(-rightPower);
        leftBack.setPower(-leftPower);
        rightBack.setPower(-rightPower);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void driveBackward(double power, long duration) {
        leftFront.setPower(-power);
        rightFront.setPower(-power);
        leftBack.setPower(-power);
        rightBack.setPower(-power);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void turnLeft(double leftPower, double rightPower, long duration) {
        leftFront.setPower(-leftPower);
        rightFront.setPower(rightPower);
        leftBack.setPower(-leftPower);
        rightBack.setPower(rightPower);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void turnLeft(double power, long duration) {
        leftFront.setPower(-power);
        rightFront.setPower(power);
        leftBack.setPower(-power);
        rightBack.setPower(power);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void turnRight(double leftPower, double rightPower, long duration) {
        leftFront.setPower(leftPower);
        rightFront.setPower(-rightPower);
        leftBack.setPower(leftPower);
        rightBack.setPower(-rightPower);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void turnRight(double power, long duration) {
        leftFront.setPower(power);
        rightFront.setPower(-power);
        leftBack.setPower(power);
        rightBack.setPower(-power);
        sleep(duration);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    private void stopMotors() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        sleep(500);
    }

    private void stopMotors(long duration) {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        sleep(duration);
    }
}
