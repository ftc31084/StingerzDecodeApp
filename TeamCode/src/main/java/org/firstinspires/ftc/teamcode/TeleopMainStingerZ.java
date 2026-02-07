package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "StingerZ: Teleop")
//@Disabled
public class TeleopMainStingerZ extends LinearOpMode {
    MyRobot myRobot;

    @Override
    public void runOpMode() throws InterruptedException {
        MyRobot.ROBOT = MyRobot.Robot.STINGERZ;

        myRobot = new MyRobot(hardwareMap, telemetry, gamepad1, gamepad2);
        myRobot.init();

        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            myRobot.getMecanumDriveSystem().loop();
            myRobot.getArtifactIntakeSystem().loop();
            myRobot.getArtifactScoringSystem().loop();
            telemetry.update();
        }

        myRobot.stop();
    }
}