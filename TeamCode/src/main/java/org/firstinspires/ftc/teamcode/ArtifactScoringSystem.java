package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.LaunchActivityConstantsList;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactScoringSystem {
    public static double LAUNCH_SPEED = 1.0;
    private HardwareMap hardwareMap;
    private Gamepad gamepad1, gamepad2;
    private Gamepad previousGamepad2;
    private Telemetry telemetry;

    private DcMotorEx scoringMotor;

    ArtifactScoringSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        scoringMotor = hardwareMap.get(DcMotorEx.class, "scoring_motor");
    }

    public void runOnce() {
    }

    public void loop() throws InterruptedException {
        if (gamepad2.left_bumper && !gamepad2.start) {
            startIntake();
        }else if (gamepad2.right_bumper) {
            stop();
        }
    }

    public void startIntake() {
        scoringMotor.setPower(LAUNCH_SPEED);
    }

    public void stop() {
        scoringMotor.setPower(0);
    }
}

