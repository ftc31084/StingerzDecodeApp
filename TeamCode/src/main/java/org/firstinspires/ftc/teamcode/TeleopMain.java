package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "StingerZ: Teleop Main")
public class TeleopMain extends LinearOpMode {
    //    TankDrive tankDrive;
    MecanumDriveSystem mecanumDriveSystem;
    ArtifactIntakeSystem artifactIntakeSystem;

    ArtifactScoringSystem scoringSystem;

    RampIntakeSystem rampSystem;

    @Override
    public void runOpMode() throws InterruptedException {
//        tankDrive = new TankDrive(hardwareMap, telemetry, gamepad1, gamepad2);
//        tankDrive.init();

        mecanumDriveSystem = new MecanumDriveSystem(hardwareMap, telemetry, gamepad1, gamepad2);
        mecanumDriveSystem.init();

        artifactIntakeSystem = new ArtifactIntakeSystem(hardwareMap, telemetry, gamepad1, gamepad2);
        artifactIntakeSystem.init();

        scoringSystem = new ArtifactScoringSystem(hardwareMap, telemetry, gamepad1, gamepad2);
        scoringSystem.init();

        rampSystem = new RampIntakeSystem(hardwareMap, telemetry, gamepad1, gamepad2);
        rampSystem.init();

        telemetry.addData(">", "Robot Ready.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            mecanumDriveSystem.loop();
            artifactIntakeSystem.loop();
            scoringSystem.loop();
            rampSystem.loop();
            telemetry.update();
        }

        mecanumDriveSystem.stop();
        artifactIntakeSystem.stop();
        scoringSystem.stop();
    }
}