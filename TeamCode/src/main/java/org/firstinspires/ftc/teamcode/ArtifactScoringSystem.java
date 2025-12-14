package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactScoringSystem implements Subsystem {
    public static double kp = 100;
    public static double ki = 0;
    public static double kd = 2;
    public static double kf = 0;

    public static double FEED_TIME_SECONDS = 10.0; //The feeder servo run this long when a shot is requested.
    public static double LAUNCH_TARGET_VELOCITY = 2000;
    public static double LAUNCHER_MIN_VELOCITY = 1700;
    public static double FEEDER_FULL_SPEED = 1.0;

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private Telemetry telemetry;

    private DcMotorEx launchMotorLeft;
    private DcMotorEx launchMotorRight;
    private CRServo launchFeeder;
    private LaunchState launchState;

    private final ElapsedTime feederTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    ArtifactScoringSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    @Override
    public void init() {
        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launch_motor_left");
        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launch_motor_right");

        launchMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launchMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launchMotorLeft.setZeroPowerBehavior(BRAKE);
        launchMotorRight.setZeroPowerBehavior(BRAKE);

        launchFeeder = hardwareMap.get(CRServo.class, "launch_feeder");
        launchFeeder.setDirection(DcMotorSimple.Direction.REVERSE);

        updatePID();
        stopLauncher();
        stopFeeder();

        launchState = LaunchState.IDLE;
        telemetry.addData("ArtifactScoringSystem", "Initialized");
    }

    @Override
    public void loop() throws InterruptedException {
        boolean shotRequested = false;

        if (gamepad2.yWasPressed()) {
            shotRequested = true;
        } else if (gamepad2.leftBumperWasPressed()) {
            updatePID();
        } else if (gamepad2.left_trigger > 0.1) {
            launchState = LaunchState.STOP;
        }

        switch (launchState) {
            case IDLE:
                if (shotRequested) {
                    launchState = LaunchState.SPIN_UP;
                }
                break;
            case SPIN_UP:
                spinUp();
                if (isTargetSpeedReached()) {
                    launchState = LaunchState.LAUNCH;
                }
                break;
            case LAUNCH:
                startFeeder();
                feederTimer.reset();
                launchState = LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if (feederTimer.seconds() > FEED_TIME_SECONDS) {
                    launchState = LaunchState.STOP;
                }
                break;
            case STOP:
                stopFeeder();
                stopLauncher();
                launchState = LaunchState.IDLE;
                break;
        }

        telemetry.addData("State", launchState);
        telemetry.addData("launchSpeedLeft", launchMotorLeft.getVelocity());
        telemetry.addData("launchSpeedRight", launchMotorRight.getVelocity());
    }

    public void spinUp() {
        launchMotorLeft.setVelocity(LAUNCH_TARGET_VELOCITY);
        launchMotorRight.setVelocity(-LAUNCH_TARGET_VELOCITY);
    }

    public boolean isTargetSpeedReached() {
        boolean targetSpeedReached = false;
        if (launchMotorLeft.getVelocity() > LAUNCHER_MIN_VELOCITY && launchMotorRight.getVelocity() > LAUNCHER_MIN_VELOCITY) {
            targetSpeedReached = true;
        }
        return targetSpeedReached;
    }

    public void stopLauncher() {
        launchMotorLeft.setVelocity(0);
        launchMotorRight.setVelocity(0);
    }

    public void startFeeder() {
        launchFeeder.setPower(FEEDER_FULL_SPEED);
    }

    public void stopFeeder() {
        launchFeeder.setPower(0);
    }

    public void updatePID() {
        launchMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
        launchMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
    }

    @Override
    public void stop() {
        stopLauncher();
        stopFeeder();
    }

    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
        STOP
    }
}