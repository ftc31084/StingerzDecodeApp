package org.firstinspires.ftc.teamcode;

public interface Subsystem {
    void init();

    void loop() throws InterruptedException;

    void stop();
}
