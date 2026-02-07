package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MecanumDriveSystem implements Subsystem {
    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    private static double SLOW_DRIVE_FORWARD = 0.3;
    private static double SLOW_DRIVE_STRAFE = 0.3;

    private static double SPEED_FACTOR = 0.75;
    private static double SLOW_TURN_SPEED_FACTOR = 0.75;
    private static final double TPI = 537.7/(Math.PI * 4.094);
    private  static final double TRACK_WIDTH_INCHES = 8;

    // This declares the four motors needed
    DcMotorEx leftFront;
    DcMotorEx rightFront;
    DcMotorEx leftBack;
    DcMotorEx rightBack;

    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;
    final MyRobot myRobot;

    MecanumDriveSystem(MyRobot myRobot) {
        this.hardwareMap = myRobot.getHardwareMap();
        this.telemetry = myRobot.getTelemetry();
        this.gamepad1 = myRobot.getGamepad1();
        this.gamepad2 = myRobot.getGamepad2();
        this.myRobot = myRobot;
    }

    @Override
    public void init() {
        leftBack = hardwareMap.get(DcMotorEx.class, "rl_drive");
        rightBack = hardwareMap.get(DcMotorEx.class, "rr_drive");
        leftFront = hardwareMap.get(DcMotorEx.class, "fl_drive");
        rightFront = hardwareMap.get(DcMotorEx.class, "fr_drive");

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

    @Override
    public void loop() {
//        telemetry.addLine("Press A to reset Yaw");
//        telemetry.addLine("Hold left bumper to drive in robot relative");
//        telemetry.addLine("The left joystick sets the robot direction");
//        telemetry.addLine("Moving the right joystick left and right turns the robot");

        // If you press the A button, then you reset the Yaw to be zero from the way
        // the robot is currently pointing
//        if (gamepad1.a) {
//            imu.resetYaw();
//        }

        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        boolean dpadLeft = gamepad1.dpad_left;
        boolean dpadRight = gamepad1.dpad_right;

        if (dpadUp || dpadDown || dpadLeft || dpadRight) {
            if (dpadUp) {
                drive(SLOW_DRIVE_FORWARD, 0, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadDown) {
                drive(-SLOW_DRIVE_FORWARD, 0, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadRight) {
                drive(0, -SLOW_DRIVE_STRAFE, gamepad1.right_stick_x * SPEED_FACTOR);
            } else if (dpadLeft) {
                drive(0, SLOW_DRIVE_STRAFE, gamepad1.right_stick_x * SPEED_FACTOR);
            }
        } else if (gamepad1.left_bumper || gamepad1.right_bumper){
            if (gamepad1.left_bumper) {
                drive(0, 0, -gamepad1.right_stick_x * SLOW_TURN_SPEED_FACTOR);
            } else if (gamepad1.right_bumper) {
                drive(0, 0, -gamepad1.right_stick_x * -SLOW_TURN_SPEED_FACTOR);
            }
        } else {
            drive(-gamepad1.left_stick_y * SPEED_FACTOR, gamepad1.left_stick_x * SPEED_FACTOR, -gamepad1.right_stick_x * SPEED_FACTOR);
        }

        // If you press the left bumper, you get a drive from the point of view of the robot
        // (much like driving an RC vehicle)
//        if (gamepad1.left_bumper) {
//            drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//        } else {
//            driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//        }
    }

    // This routine drives the robot field relative
    private void driveFieldRelative(double forward, double strafe, double rotate) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, strafe);
        double r = Math.hypot(strafe, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        drive(newForward, newRight, rotate);
    }

    public void drive(double forward, double strafe, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;
        double backLeftPower = forward - strafe + rotate;

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

    public void driveForwardInches(double inches, double power) {
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
    }
    //
    public void strafeLeft(double inches, double power) {
        int ticks = (int) (inches * TPI);

        leftFront.setTargetPosition(leftFront.getCurrentPosition() - ticks);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + ticks);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + ticks);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() - ticks);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);
    }

}