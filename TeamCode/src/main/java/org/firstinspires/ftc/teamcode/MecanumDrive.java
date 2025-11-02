package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This file contains a minimal example of an iterative (Non-Linear) "OpMode". An OpMode is a
 * 'program' that runs in either the autonomous or the TeleOp period of an FTC match. The names
 * of OpModes appear on the menu of the FTC Driver Station. When an selection is made from the
 * menu, the corresponding OpMode class is instantiated on the Robot Controller and executed.
 *
 * Remove the @Disabled annotation on the next line or two (if present) to add this OpMode to the
 * Driver Station OpMode list, or add a @Disabled annotation to prevent this OpMode from being
 * added to the Driver Station.
 */
public class MecanumDrive  {
    public DcMotor rlMotor = null;
    public DcMotor rrMotor = null;
    public DcMotor flMotor = null;
    public DcMotor frMotor = null;
    HardwareMap hardwareMap = null;
    Telemetry telemetry = null;
    Gamepad gamepad1;
    Gamepad gamepad2;

    MecanumDrive(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        telemetry.addData("Status", "Initialized");
        rlMotor  = hardwareMap.get(DcMotor.class, "rl_motor");
        rrMotor  = hardwareMap.get(DcMotor.class, "rr_motor");
        flMotor  = hardwareMap.get(DcMotor.class, "ll_motor");
        frMotor  = hardwareMap.get(DcMotor.class, "lr_motor");

        rlMotor.setDirection(DcMotor.Direction.FORWARD);
        rrMotor.setDirection(DcMotor.Direction.FORWARD);
        flMotor.setDirection(DcMotor.Direction.FORWARD);
        frMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void teleop(){
        double power = -gamepad1.left_stick_y;

        //Left stick - Drive
        telemetry.addData("Left stick y", -gamepad1.left_stick_y);
        telemetry.addData("Left stick x", gamepad1.left_stick_x);

        telemetry.addData("Left stick y", -gamepad1.right_stick_y);
        telemetry.addData("Left stick x", gamepad1.right_stick_x);

        rlMotor.setPower(power);
        rrMotor.setPower(-power);
        flMotor.setPower(power);
        frMotor.setPower(-power);

    }
}
