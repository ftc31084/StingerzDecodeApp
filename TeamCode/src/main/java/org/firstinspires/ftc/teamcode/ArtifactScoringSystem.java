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

    private DcMotorEx scoringMotorLeft;
    private DcMotorEx scoringMotorRight;

    ArtifactScoringSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        scoringMotorLeft = hardwareMap.get(DcMotorEx.class, "scoring_motor_left");
        scoringMotorRight = hardwareMap.get(DcMotorEx.class, "scoring_motor_right");
    }

    public void runOnce() {
    }

    public void loop() throws InterruptedException {
        if (gamepad2.right_bumper) {
            startLauncher();
        } else if (gamepad2.left_trigger > 0.1) {
            stop();
        }
    }

    public void startLauncher() {
        scoringMotorLeft.setPower(LAUNCH_SPEED);
        scoringMotorRight.setPower(-LAUNCH_SPEED);
    }

    public void stop() {
        scoringMotorLeft.setPower(0);
        scoringMotorRight.setPower(0);
    }
}

