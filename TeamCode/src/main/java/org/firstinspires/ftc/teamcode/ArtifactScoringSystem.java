package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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
    public static double LAUNCH_TARGET_VELOCITY = 2100;
    public static double LAUNCHER_MIN_VELOCITY = 1750;

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    private FeederSystem feederSystem;
    private DcMotorEx launchMotorLeft;
    private DcMotorEx launchMotorRight;

    private LaunchState launchState;
    private final ElapsedTime feederTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    ArtifactScoringSystem(MyRobot myRobot) {
        this.hardwareMap = myRobot.getHardwareMap();
        this.telemetry = myRobot.getTelemetry();
        this.gamepad2 = myRobot.getGamepad2();
        this.feederSystem = myRobot.getFeederSystem();
    }

    @Override
    public void init() {
        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launch_motor_left");
        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launch_motor_right");

        launchMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launchMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launchMotorLeft.setZeroPowerBehavior(BRAKE);
        launchMotorRight.setZeroPowerBehavior(BRAKE);

        updatePID();
        stopLauncher();

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
                feederSystem.open();
                if (isTargetSpeedReached()) {
                    launchState = LaunchState.LAUNCH;
                }
                break;
            case LAUNCH:
                feederSystem.feedUp();
                feederTimer.reset();
                launchState = LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if (feederTimer.seconds() > FEED_TIME_SECONDS) {
                    launchState = LaunchState.STOP;
                }
                break;
            case STOP:
                feederSystem.stopFeeder();
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
        if (abs(launchMotorLeft.getVelocity()) > LAUNCHER_MIN_VELOCITY && abs(launchMotorRight.getVelocity()) > LAUNCHER_MIN_VELOCITY) {
            targetSpeedReached = true;
        }
        return targetSpeedReached;
    }

    public void stopLauncher() {
        launchMotorLeft.setVelocity(0);
        launchMotorRight.setVelocity(0);
    }


    public void updatePID() {
        launchMotorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
        launchMotorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kp, ki, kd, kf));
    }

    @Override
    public void stop() {
        stopLauncher();
    }

    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
        STOP
    }
}