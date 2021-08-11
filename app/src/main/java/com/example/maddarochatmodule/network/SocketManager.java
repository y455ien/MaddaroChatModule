package com.example.maddarochatmodule.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.maddarochatmodule.cache.UserPref;
import com.hadilq.liveevent.LiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

@Singleton
public class SocketManager {

    public static final String OBSERVE_ROOM_ACTIVATION = "observe-room-deactivate";
    public static final String OBSERVE_MESSAGE = "messages-observer";

    private static final String EMIT_AUTH = "authentication";
    public static final String EMIT_MESSAGE = "send-message";
    public static final String EMIT_ROOM_ACTIVATION = "room-deactivate";
    public static final String EMIT_REED_MESSAGE = "seen-messages";

    private Socket mSocket;
    public LiveEvent<Throwable> errorLiveData ;
    public MutableLiveData<Boolean> statusLiveData ;

    @Inject
    UserPref userPref;

    @Inject
    public SocketManager() {
        errorLiveData = new LiveEvent<>();
        statusLiveData = new MutableLiveData<>();
    }


    public void init() {
        Log.e("YASSIEN ", "INIT SOCKET");
        try {
            mSocket = IO.socket(Endpoints.SOCKET);

            mSocket.on("authenticated", onAuthenticated);
            mSocket.on("unauthorized", failAuth);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onTimeout);
            mSocket.on(Socket.EVENT_ERROR, onError);

            mSocket.connect();

        } catch (Exception e) {
            Log.e("YASSIEN ", e.getMessage());
            e.printStackTrace();
        }

    }

    public void destroy() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    public void observe(String event, Emitter.Listener fn) {
        if (mSocket == null) {
            init();

            if (mSocket != null)
                mSocket.on(event, fn);
        } else {
            mSocket.on(event, fn);
        }
    }

    public LiveEvent<Boolean> emit(String event, JSONObject data) {
        Timber.d("%s emit: %s", event, data.toString());
        LiveEvent<Boolean> successEvent = new LiveEvent<>();
        if (!mSocket.connected()) {
            successEvent.postValue(false);
            errorLiveData.postValue(new IOException());
        } else {
            mSocket.emit(event, data, new AckWithTimeOut(5000, event) {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Timber.d("%s emit ack: %s", event, args[0].toString());
                        if (args[0].toString().equalsIgnoreCase("No Ack")) {
                            successEvent.postValue(false);
                            errorLiveData.postValue(new IOException());
                        } else {
                            cancelTimer();
                            try {
                                boolean value = ((JSONObject) args[0]).getBoolean("status");
                                successEvent.postValue(value);
                            } catch (JSONException e) {
                                successEvent.postValue(false);
                                errorLiveData.postValue(e);
                            }

                        }
                    }
                }
            });
        }
        return successEvent;
    }

    private Emitter.Listener onAuthenticated = args -> {
        Timber.d("onAuthenticated");
        statusLiveData.postValue(true);
    };

    private Emitter.Listener failAuth = args -> {
        Timber.d("failAuth");
        statusLiveData.postValue(false);
    };

    private Emitter.Listener onConnect = args -> {
//        Timber.d("onConnect: user -> %s , token -> %s", userPref.getId(), userPref.getAccessToken());
        try {
//            String token = userPref.getAccessToken();
            JSONObject json = new JSONObject();
            json.put("access_token", userPref.getChatToken());
//            json.put("access_token", "Bearer ".concat("yassien1"));

            mSocket.emit(EMIT_AUTH, json);

        } catch (Exception e) {
            e.printStackTrace();
        }

    };

    private Emitter.Listener onDisconnect = args -> {
        Timber.d("onDisconnect");
        statusLiveData.postValue(false);
    };

    private Emitter.Listener onConnectError = args -> {
        Timber.d("onConnectError");
        Log.e("YASSIEN", args[0].toString());
        statusLiveData.postValue(false);
    };

    private Emitter.Listener onTimeout = args -> {
        Timber.d("onTimeout");
    };

    private Emitter.Listener onError = args -> {
        Timber.d("onError");
        statusLiveData.postValue(false);
    };

}
