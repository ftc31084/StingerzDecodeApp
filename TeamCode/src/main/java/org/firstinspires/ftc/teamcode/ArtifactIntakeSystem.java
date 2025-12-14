package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactIntakeSystem implements Subsystem {
    public static double INTAKE_SPEED = 1.0;
    public static double OUTTAKE_SPEED = -0.4;
    public static double FEEDER_FULL_SPEED = 1.0;
    public static double FEEDER_STOPPER_OPEN = 0.3;
    public static double FEEDER_STOPPER_CLOSE = 0.4;

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private Telemetry telemetry;

    private DcMotorEx intakeMotor;
    private CRServo feeder;
    private Servo feederStopper;

    ArtifactIntakeSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    @Override
    public void init() {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake_motor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        feeder = hardwareMap.get(CRServo.class, "launch_feeder");
        feeder.setDirection(DcMotorSimple.Direction.REVERSE);

        feederStopper = hardwareMap.get(Servo.class, "feeder_stopper");
        feederStopper.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void loop() throws InterruptedException {
        if (gamepad2.aWasPressed() && !gamepad2.startWasPressed()) {
            closeFeeder();
            startIntake();
        } else if (gamepad2.bWasPressed() && !gamepad2.startWasPressed()) {
            startOuttake();
        } else if (gamepad2.left_trigger > 0.1) {
            stop();
            openFeeder();
        }
    }

    public void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
        feeder.setPower(FEEDER_FULL_SPEED);
    }

    public void openFeeder() {
        feederStopper.setPosition(FEEDER_STOPPER_OPEN);
    }

    public void closeFeeder() {
        feederStopper.setPosition(FEEDER_STOPPER_CLOSE);
    }

    public void startOuttake() {
        intakeMotor.setPower(OUTTAKE_SPEED);
    }

    @Override
    public void stop() {
        intakeMotor.setPower(0);
        feeder.setPower(0);
    }
}