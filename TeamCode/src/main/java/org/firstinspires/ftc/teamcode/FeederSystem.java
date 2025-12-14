package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Deprecated
public class FeederSystem implements Subsystem {
    public static double RAMP_SPEED = 0.99999;

    private HardwareMap hardwareMap;
    private Gamepad gamepad1, gamepad2;
    private Gamepad previousGamepad2;
    private Telemetry telemetry;

    private CRServo launchFeeder;

    FeederSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    @Override
    public void init() {
        launchFeeder = hardwareMap.get(CRServo.class, "launch_feeder");
        launchFeeder.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() throws InterruptedException {
        if (gamepad2.aWasPressed() && !gamepad2.startWasPressed()) {
            feedUp();
        } else if (gamepad2.yWasPressed()) {
            feedUp();
        } else if (gamepad2.left_trigger > 0.1) {
            stopFeeder();
        }
    }

    public void feedUp() {
        launchFeeder.setPower(-RAMP_SPEED);
    }

    public void feedDown() {
        launchFeeder.setPower(RAMP_SPEED);
    }

    public void stopFeeder() {
        launchFeeder.setPower(0.0);
    }

    @Override
    public void stop() {
        stopFeeder();
    }
}