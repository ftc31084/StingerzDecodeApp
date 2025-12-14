package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDriveSystem implements Subsystem {
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;

    private static double SLOW_DRIVE_FORWARD = 0.3;
    private static double SLOW_DRIVE_STRAFE = 0.3;

    private static double SPEED_FACTOR = 0.75;

    // This declares the four motors needed
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;

    MecanumDriveSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    @Override
    public void init() {
        leftBack = hardwareMap.get(DcMotor.class, "rl_drive");
        rightBack = hardwareMap.get(DcMotor.class, "rr_drive");
        leftFront = hardwareMap.get(DcMotor.class, "fl_drive");
        rightFront = hardwareMap.get(DcMotor.class, "fr_drive");

        // We set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);

        // This uses RUN_USING_ENCODER to be more accurate.   If you don't have the encoder
        // wires, you should remove these
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public void autoInit() {
        //TODO: initialize the drive system for auto
    }

    @Override
    public void loop() {
        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        boolean dpadLeft = gamepad1.dpad_left;
        boolean dpadRight = gamepad1.dpad_right;

        if (dpadUp || dpadDown || dpadLeft || dpadRight) {
            if (dpadUp) {
                drive(SLOW_DRIVE_FORWARD, 0, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadDown) {
                drive(-SLOW_DRIVE_FORWARD, 0, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadLeft) {
                drive(0, -SLOW_DRIVE_STRAFE, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadRight) {
                drive(0, SLOW_DRIVE_STRAFE, gamepad1.right_stick_x * SPEED_FACTOR);
            }
        } else {
            drive(-gamepad1.left_stick_y * SPEED_FACTOR, gamepad1.left_stick_x * SPEED_FACTOR, gamepad1.right_stick_x * SPEED_FACTOR);
        }
    }

    public void drive(double forward, double right, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.
        leftFront.setPower(frontLeftPower / maxPower);
        rightFront.setPower(frontRightPower / maxPower);
        leftBack.setPower(backLeftPower / maxPower);
        rightBack.setPower(backRightPower / maxPower);
    }

    @Override
    public void stop() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
}