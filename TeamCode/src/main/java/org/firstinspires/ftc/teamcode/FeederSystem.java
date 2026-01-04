package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FeederSystem implements Subsystem {
    public static double RAMP_UP_SPEED = 0.9999;
    public static double RAMP_DOWN_SPEED = -0.3;

    public static double FEEDER_STOPPER_OPEN = 0.8;
    public static double FEEDER_STOPPER_CLOSE = 0.55;

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    private CRServo feeder;
    private Servo feederStopper;

    FeederSystem(MyRobot myRobot) {
        this.hardwareMap = myRobot.getHardwareMap();
        this.telemetry = myRobot.getTelemetry();
        this.gamepad2 = myRobot.getGamepad2();
    }

    @Override
    public void init() {
        feeder = hardwareMap.get(CRServo.class, "launch_feeder");
        feeder.setDirection(DcMotorSimple.Direction.REVERSE);

        feederStopper = hardwareMap.get(Servo.class, "feeder_stopper");
        feederStopper.setDirection(Servo.Direction.FORWARD);

        stopFeeder();
        close();
    }

    @Override
    public void loop() throws InterruptedException {
        if (gamepad2.left_trigger > 0.1) {
            stopFeeder();
        }
    }

    public void feedUp() {
        feeder.setPower(RAMP_UP_SPEED);
    }

    public void feedDown() {
        feeder.setPower(RAMP_DOWN_SPEED);
    }

    public void stopFeeder() {
        feeder.setPower(0.0);
    }

    public void open() {
        feederStopper.setPosition(FEEDER_STOPPER_OPEN);
    }

    public void close() {
        feederStopper.setPosition(FEEDER_STOPPER_CLOSE);
    }

    @Override
    public void stop() {
        stopFeeder();
    }
}