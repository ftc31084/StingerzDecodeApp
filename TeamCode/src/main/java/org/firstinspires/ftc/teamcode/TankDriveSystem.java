package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TankDriveSystem {
    public static double SPEED_MULTIPLIER = 0.5;
    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private double forward = 0;
    private double rotate = 0;
    private double max = 0;

    TankDriveSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        leftDrive = hardwareMap.get(DcMotor.class, "rl_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "rr_drive");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    public void loop() {
        forward = -gamepad1.left_stick_y;
        rotate = gamepad1.right_stick_x;

        double leftPower = (forward + rotate) * SPEED_MULTIPLIER;
        double rightPower = (forward - rotate) * SPEED_MULTIPLIER;

        if (leftPower > 1 || rightPower > 1) {
            max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            leftPower /= max;
            rightPower /= max;
        }

        // Run wheels in arcade mode
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public void autoPushSample() {

    }

    public void stop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }
}
