package com.example.maddarochatmodule.network;


import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;

import static com.example.maddarochatmodule.network.SocketManager.EMIT_REED_MESSAGE;

public class AckWithTimeOut implements Ack {

    private Timer timer;
    private long timeOut = 0;
    private boolean called = false;
    private String emissionEvent;

    public AckWithTimeOut() {
    }

    public AckWithTimeOut(long timeout_after, String event) {
        if (timeout_after <= 0)
            return;
        this.timeOut = timeout_after;
        this.emissionEvent = event;
        startTimer();
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!emissionEvent.equalsIgnoreCase(EMIT_REED_MESSAGE)) callback("No Ack");
            }
        }, timeOut);
    }

    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
            startTimer();
        }
    }

    public void cancelTimer() {
        if (timer != null)
            timer.cancel();
    }

    void callback(Object... args) {
        if (called) return;
        called = true;
        cancelTimer();
        call(args);
    }

    @Override
    public void call(Object... args) {

    }
}