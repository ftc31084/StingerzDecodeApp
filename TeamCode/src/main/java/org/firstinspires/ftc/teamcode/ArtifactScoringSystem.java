package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactScoringSystem {
    public static double kp = 0.01;
    public static double ki = 0;
    public static double kd = 0.0003;
    public static double kf = 0;
    public static double LAUNCH_TARGET_VELOCITY = 2000;
    public static double LAUNCHER_MIN_VELOCITY = 10;

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
        scoringMotorLeft = hardwareMap.get(DcMotorEx.class, "launch_motor_left");
        scoringMotorRight = hardwareMap.get(DcMotorEx.class, "launch_motor_right");

        scoringMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        scoringMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        scoringMotorLeft.setZeroPowerBehavior(BRAKE);
        scoringMotorRight.setZeroPowerBehavior(BRAKE);
    }

    public void runOnce() {
    }

    public void loop() throws InterruptedException {
        if (gamepad2.right_bumper) {
            startLauncher();
        } else if (gamepad2.left_bumper) {
//            updatePID();
        } else if (gamepad2.left_trigger > 0.1) {
            stop();
        }
    }

    public void startLauncher() {
        scoringMotorLeft.setVelocity(LAUNCH_TARGET_VELOCITY);
        scoringMotorRight.setVelocity(-LAUNCH_TARGET_VELOCITY);
    }

    public void updatePID() {
        scoringMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
        scoringMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
    }

    public void stop() {
        scoringMotorLeft.setVelocity(0);
        scoringMotorRight.setVelocity(0);
    }
}