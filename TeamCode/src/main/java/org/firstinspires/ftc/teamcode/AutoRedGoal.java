package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


public class AutoRedGoal extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    CRServo rampMotor;

    private DcMotorEx launchMotorLeft;
    private DcMotorEx launchMotorRight;

    private ArtifactScoringSystem scoringSystem;

    static final double TPI = 537.7/(Math.PI * 4.094);
    static final double TRACK_WIDTH_INCHES = 8;

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotor.class, "fl_drive");
        rightFront = hardwareMap.get(DcMotor.class, "fr_drive");
        leftBack = hardwareMap.get(DcMotor.class, "rl_drive");
        rightBack = hardwareMap.get(DcMotor.class, "rr_drive");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        scoringSystem = new ArtifactScoringSystem(hardwareMap, telemetry, gamepad1, gamepad2);
        scoringSystem.init();

        telemetry.addData(">", "Auto Ready.");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Put code here
            driveForwardInches(60,1);
            stopDrive();
            launch();
            stopDrive();
        }
    }

    private void driveForwardInches(double inches, double power) {
        int ticks = (int) (inches * TPI);

        leftFront.setTargetPosition(leftFront.getCurrentPosition() + ticks);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + ticks);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + ticks);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() + ticks);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);

        while (opModeIsActive()
                && leftFront.isBusy()
                && rightFront.isBusy()
                && leftBack.isBusy()
                && rightBack.isBusy()) {
            idle();
        }

        stopDrive();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    private void turnDegrees(double degrees, double power) {
        double arcLength = Math.PI * TRACK_WIDTH_INCHES * (degrees / 360.0);
        int ticks = (int) (arcLength * TPI);

        leftFront.setTargetPosition(leftFront.getCurrentPosition() - ticks);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() - ticks);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + ticks);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() + ticks);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (opModeIsActive()
                && leftFront.isBusy()
                && leftBack.isBusy()
                && rightFront.isBusy()
                && rightBack.isBusy()) {
            idle();
        }

        stopDrive();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void stopDrive() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

//    private void launch(double LAUNCH_TARGET_VELOCITY){
//        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launch_motor_left");
//        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launch_motor_right");
//
//        launchMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        launchMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        launchMotorLeft.setZeroPowerBehavior(BRAKE);
//        launchMotorRight.setZeroPowerBehavior(BRAKE);
//
//        launchMotorLeft.setVelocity(LAUNCH_TARGET_VELOCITY);
//        launchMotorRight.setVelocity(-LAUNCH_TARGET_VELOCITY);
//    }

    private void launch() throws InterruptedException{
        scoringSystem.spinUp();
        wait(500);
        scoringSystem.startFeeder();
        wait(3000);
        scoringSystem.stop();
    }

//    private void ramp(String direction){
//        if(direction == "in"){
//            rampMotor.setPower(-0.99999);
//        }else if(direction == "out"){
//            rampMotor.setPower(0.99999);
//        }
//
//    }
//    private void stopRamp(){
//        rampMotor.setPower(0);
//    }
}

