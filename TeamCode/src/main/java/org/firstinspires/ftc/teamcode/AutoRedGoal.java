package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.HelperClasses.ArtifactIntakeSystem;
import org.firstinspires.ftc.teamcode.HelperClasses.ArtifactScoringSystem;
import org.firstinspires.ftc.teamcode.HelperClasses.FeederSystem;
import org.firstinspires.ftc.teamcode.HelperClasses.MecanumDriveSystem;


@Autonomous(name = "StingerZ: Auto Red Goal")
public class AutoRedGoal extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    MyRobot myRobot;
    private MecanumDriveSystem driveSystem;
    private ArtifactScoringSystem scoringSystem;
    private ArtifactIntakeSystem intakeSystem;
    private FeederSystem feederSystem;

    static final double TPI = 537.7 / (Math.PI * 4.094);
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

        telemetry.addData(">", "Auto Ready.");
        telemetry.update();

        MyRobot.ROBOT = MyRobot.Robot.STINGERZ;

        myRobot = new MyRobot(hardwareMap, telemetry, gamepad1, gamepad2);
        myRobot.init();

        driveSystem = myRobot.getMecanumDriveSystem();
        scoringSystem = myRobot.getArtifactScoringSystem();
        intakeSystem = myRobot.getArtifactIntakeSystem();
        feederSystem = myRobot.getFeederSystem();

        waitForStart();

        if (opModeIsActive()) {
            // Put code here

            //backup and launch starting artifacts
            driveSystem.driveForwardInches(48, 1);
            waitForDriveComplete();
            launch();

            //pick up and launch first group
            turnDegrees(-135, 0.8);
            intakeSystem.startIntake();
            driveSystem.driveForwardInches(30, 0.5);
            waitForDriveComplete();
            intakeSystem.stop();
            driveSystem.driveForwardInches(-30, 0.8);
            waitForDriveComplete();
            turnDegrees(135, 0.8);
            launch();

            //pick up and launch second group
            turnDegrees(-135, 0.8);
            driveLeft(-24, 0.8);
            intakeSystem.startIntake();
            driveSystem.driveForwardInches(30, 0.5);
            waitForDriveComplete();
            intakeSystem.stop();
            driveSystem.driveForwardInches(-30, 0.5);
            waitForDriveComplete();
            driveLeft(24, 0.8);
            turnDegrees(135, 0.8);
            launch();

            //end off the launch line
            turnDegrees(-135, 0.8);
            driveLeft(-24, 0.8);
            driveSystem.stop();
        }
    }

    private void waitForDriveComplete() {
        while (opModeIsActive()
                && leftFront.isBusy()
                && rightFront.isBusy()
                && leftBack.isBusy()
                && rightBack.isBusy()) {
            idle();
        }

        driveSystem.stop();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void driveLeft(double inches, double power) {
        driveSystem.driveForwardInches(inches, power);

        while (opModeIsActive()
                && leftFront.isBusy()
                && rightFront.isBusy()
                && leftBack.isBusy()
                && rightBack.isBusy()) {
            idle();
        }

        driveSystem.stop();

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

        driveSystem.stop();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    private void launch() throws InterruptedException {
        scoringSystem.spinUp();
        wait(500);
        feederSystem.feedUp();
        wait(3000);
        scoringSystem.stop();
    }
}

