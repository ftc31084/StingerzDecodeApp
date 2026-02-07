package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactIntakeSystem implements Subsystem {
    public static double INTAKE_SPEED = 0.75;
    public static double OUTTAKE_SPEED = -0.4;
    public static double INTAKE_TIMEOUT = 10.0; //The intake runs this long before auto stop

    private final HardwareMap hardwareMap;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;

    private FeederSystem feederSystem;
    private DcMotorEx intakeMotor;
    private IntakeState intakeState;

    private final ElapsedTime intakeTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    ArtifactIntakeSystem(MyRobot myRobot) {
        this.hardwareMap = myRobot.getHardwareMap();
        this.telemetry = myRobot.getTelemetry();
        this.gamepad2 = myRobot.getGamepad2();
        this.feederSystem = myRobot.getFeederSystem();
    }

    @Override
    public void init() {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake_motor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeState = IntakeState.IDLE;
    }

    @Override
    public void loop() throws InterruptedException {
        if (gamepad2.aWasPressed() && !gamepad2.startWasPressed()) {
            intakeState = IntakeState.START;
        } else if (gamepad2.bWasPressed() && !gamepad2.startWasPressed()) {
            intakeState = IntakeState.SPINNING_OUT;
        } else if (gamepad2.left_trigger > 0.1) {
            intakeState = IntakeState.STOP;
        }

        switch (intakeState) {
            case START:
                feederSystem.close();
                intakeTimer.reset();
                feederSystem.feedUp();
                startIntake();
                intakeState = IntakeState.IDLE;
                break;
//            case SPINNING_IN:
//                if (intakeTimer.seconds() > INTAKE_TIMEOUT) {
//                    intakeState = IntakeState.STOP;
//                }
//                    intakeState = IntakeState.IDLE;
//                break;
            case SPINNING_OUT:
                feederSystem.feedDown();
                startOuttake();
                intakeState = IntakeState.IDLE;
                break;
            case STOP:
                feederSystem.stopFeeder();
                stop();
                intakeState = IntakeState.IDLE;
            case IDLE:
                break;
                //
        }
    }

    public void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    public void startOuttake() {
        intakeMotor.setPower(OUTTAKE_SPEED);
    }

    @Override
    public void stop() {
        intakeMotor.setPower(0);
    }

    private enum IntakeState {
        IDLE,
        START,
        SPINNING_IN,
        SPINNING_OUT,
        STOP
    }
}