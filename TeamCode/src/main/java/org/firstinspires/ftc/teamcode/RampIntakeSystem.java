package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Deprecated
public class RampIntakeSystem {
    public static double RAMP_SPEED = 0.99999;

    private HardwareMap hardwareMap;
    private Gamepad gamepad1, gamepad2;
    private Gamepad previousGamepad2;
    private Telemetry telemetry;

    private CRServo rampMotor;

    RampIntakeSystem(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        rampMotor = hardwareMap.get(CRServo.class, "ramp_servo");
    }

    public void runOnce() {}

    public void loop() throws InterruptedException {
        if (gamepad1.dpad_up && !gamepad1.start) {
            upRamp();
        } else if (gamepad1.dpad_down && !gamepad1.start) {
            downRamp();
        } else if (gamepad1.right_bumper && !gamepad1.start) {
            stopRamp();
        }
    }

    public void upRamp() { rampMotor.setPower(-RAMP_SPEED);}

    public void downRamp() { rampMotor.setPower(RAMP_SPEED); }

    public void stopRamp() { rampMotor.setPower(0.0); }
}