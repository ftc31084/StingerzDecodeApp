package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//
@Autonomous(name = "StingerZ: Auto Small Zone (Red)")
public class AutoRedSmallZone extends LinearOpMode {
    /*
     * Motors
     */
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    private DcMotorEx launchMotorLeft;
    private DcMotorEx launchMotorRight;
    private CRServo launchFeeder;
    private DcMotorEx intakeMotor;
    private ArtifactScoringSystem scoringSystem;
    private FeederSystem feederSystem;
    private MecanumDriveSystem driveSystem;
    private ArtifactIntakeSystem intakeSystem;

    static final double TPI = 537.7 / (Math.PI * 5.51181);
    static final double TRACK_WIDTH_INCHES = 8;

    /*
     * Variables
     */
    public static double LAUNCH_TARGET_VELOCITY = 2000;
    public static double FEEDER_FULL_SPEED = 1.0;
    public static double INTAKE_SPEED = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {
        /*
         * Sets up all mapping
         * Should NOT be touched unless adding more motors or fixing something
         */

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

        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launch_motor_left");
        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launch_motor_right");

        launchMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launchMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launchMotorLeft.setZeroPowerBehavior(BRAKE);
        launchMotorRight.setZeroPowerBehavior(BRAKE);

        launchFeeder = hardwareMap.get(CRServo.class, "launch_feeder");
        launchFeeder.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake_motor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Waits for the start button to be pressed
        waitForStart();

        /*
         * Actual AutoOp code
         */
        if (opModeIsActive()) {

            driveForwardInches(20, 0.6);
            waitForDriveComplete();
            stopMotors();
            //
            /*
            // Step 1
            driveSystem.driveForwardInches(60, 0.6);
            waitForDriveComplete();
            turnDegrees(300, 0.4);
            waitForDriveComplete();
            launch();

            //Step 2
            turnDegrees(-300, 0.4);
            waitForDriveComplete();
            driveSystem.driveForwardInches(-50,0.6);
            waitForDriveComplete();
            turnDegrees(400, 0.4);
            waitForDriveComplete();
            driveSystem.driveForwardInches(-20,0.6);
            waitForDriveComplete();
            driveSystem.driveForwardInches(-20,0.6);
            waitForDriveComplete();

            //Step 3
            turnDegrees(-400,0.4);
            waitForDriveComplete();
            driveSystem.driveForwardInches(50,0.6);
            waitForDriveComplete();
            turnDegrees(300,0.4);
            waitForDriveComplete();
            launch();
            waitForDriveComplete();
            turnDegrees();

            stopMotors(); // End of AutoOp */
        }
    }

    /*
     * Helper Methods
     */

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

        stopMotors();

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

        stopMotors();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
    private void lobBall() throws InterruptedException {
        scoringSystem.spinUp();
        wait(500);
        feederSystem.feedUp();
        wait(3000);
        scoringSystem.stop();
    }

    private void lobBall(long time1, long time2) throws InterruptedException {
        scoringSystem.spinUp();
        wait(time1);
        feederSystem.feedUp();
        wait(time2);
        scoringSystem.stop();
    }

    private void launch() throws InterruptedException {
        scoringSystem.spinUp();
        feederSystem.open();
        sleep(607);
        for(int i = 0; i < 3; i++) {
            feederSystem.feedUp();
            sleep(1200);
            feederSystem.stopFeeder();
            sleep(400);
            feederSystem.feedUp();
        }
        feederSystem.close();
        scoringSystem.stop();
    }
    private void intake() {
        intakeMotor.setPower(INTAKE_SPEED);
        launchFeeder.setPower(FEEDER_FULL_SPEED);

        sleep(2000);

        intakeMotor.setPower(0);
        launchFeeder.setPower(0);
    }

    private void stopMotors() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        intakeMotor.setPower(0);
        launchMotorLeft.setPower(0);
        launchMotorRight.setPower(0);
        launchFeeder.setPower(0);
    }
}
