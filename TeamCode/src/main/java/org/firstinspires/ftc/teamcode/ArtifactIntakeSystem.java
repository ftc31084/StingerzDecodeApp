package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactIntakeSystem implements Subsystem {
    public static double INTAKE_SPEED = 1.0;
    public static double OUTTAKE_SPEED = -0.4;
    public static double FEEDER_FULL_SPEED = 1.0;
    public static double FEEDER_STOP = 0.0;

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private Telemetry telemetry;

    private DcMotorEx intakeMotor;
    private CRServo launchFeeder;

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

        launchFeeder = hardwareMap.get(CRServo.class, "launch_feeder");
        launchFeeder.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() throws InterruptedException {
        if (gamepad2.aWasPressed() && !gamepad2.startWasPressed()) {
            startIntake();
        } else if (gamepad2.bWasPressed() && !gamepad2.startWasPressed()) {
            startOuttake();
        } else if (gamepad2.left_trigger > 0.1) {
            stop();
        }
    }

    public void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
        launchFeeder.setPower(FEEDER_FULL_SPEED);
    }

    public void startOuttake() {
        intakeMotor.setPower(OUTTAKE_SPEED);
    }

    @Override
    public void stop() {
        intakeMotor.setPower(0);
        launchFeeder.setPower(FEEDER_STOP);
    }
}