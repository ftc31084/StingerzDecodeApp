package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactIntakeSystem {
    public static double INTAKE_SPEED = 0.6;
    public static double OUTTAKE_SPEED = -0.4;
    private HardwareMap hardwareMap;
    private Gamepad gamepad1, gamepad2;
    private Gamepad previousGamepad2;
    private Telemetry telemetry;

    private DcMotorEx intakeMotor;

    ArtifactIntakeSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake_motor");
    }

    public void runOnce() {
    }

    public void loop() throws InterruptedException {
        if (gamepad2.a && !gamepad2.start) {
            startIntake();
        } else if (gamepad2.b && !gamepad2.start) {
            startOuttake();
        } else if (gamepad2.left_trigger > 0.1) {
            stop();
        }
    }

    public void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    public void startOuttake() {
        intakeMotor.setPower(OUTTAKE_SPEED);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }
}

