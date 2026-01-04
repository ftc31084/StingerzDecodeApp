package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MyRobot {
    private final HardwareMap hardwareMap;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    public static Robot ROBOT = Robot.STINGERZ;

    public enum Robot {
        STINGERZ,
        EAGLES
    }


    MecanumDriveSystem mecanumDriveSystem;
    ArtifactIntakeSystem artifactIntakeSystem;
    ArtifactScoringSystem artifactScoringSystem;
    FeederSystem feederSystem;

    MyRobot(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        getMecanumDriveSystem().init();
        getArtifactIntakeSystem().init();
        getArtifactScoringSystem().init();
        getFeederSystem().init();

        telemetry.addData(">", "Robot Ready.");
    }

    public void stop() {
        if (mecanumDriveSystem != null) {
            mecanumDriveSystem.stop();
        }
        if (artifactIntakeSystem != null) {
            artifactIntakeSystem.stop();
        }
        if (artifactScoringSystem != null) {
            artifactScoringSystem.stop();
        }
        if (feederSystem != null) {
            feederSystem.stop();
        }
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public Gamepad getGamepad1() {
        return gamepad1;
    }

    public Gamepad getGamepad2() {
        return gamepad2;
    }

    public MecanumDriveSystem getMecanumDriveSystem() {
        if (mecanumDriveSystem == null) {
            mecanumDriveSystem = new MecanumDriveSystem(this);
        }
        return mecanumDriveSystem;
    }

    public ArtifactIntakeSystem getArtifactIntakeSystem() {
        if (artifactIntakeSystem == null) {
            artifactIntakeSystem = new ArtifactIntakeSystem(this);
        }
        return artifactIntakeSystem;
    }

    public ArtifactScoringSystem getArtifactScoringSystem() {
        if (artifactScoringSystem == null) {
            artifactScoringSystem = new ArtifactScoringSystem(this);
        }
        return artifactScoringSystem;
    }

    public FeederSystem getFeederSystem() {
        if (feederSystem == null) {
            feederSystem = new FeederSystem(this);
        }
        return feederSystem;
    }
}
