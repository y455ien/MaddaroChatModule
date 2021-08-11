package com.example.maddarochatmodule.repo;

import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.maddarochatmodule.base.BaseRepo;
import com.example.maddarochatmodule.data_model.ChatMessageCountResponse;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.data_model.ChatRoomMessagesResponse;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.ChatRoomResponse;
import com.example.maddarochatmodule.data_model.ChatRoomsResponse;
import com.example.maddarochatmodule.data_model.annotation.MessageType;
import com.example.maddarochatmodule.network.Endpoints;
import com.example.maddarochatmodule.network.SocketManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hadilq.liveevent.LiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;
import timber.log.Timber;

@Singleton
public class ChatRepo extends BaseRepo {

    private SocketManager socketManager;

    private LiveEvent<ChatRoomModel> incomingChatRoomActivationsLiveData;
    private LiveEvent<ChatMessageModel> incomingChatMessagesLiveData;
    private MutableLiveData<Integer> incomingChatMessagesCountLiveData;
    private MediatorLiveData<Boolean> socketStatusLiveData;

    private int noOfSubscribedViewModels = 0;

    @Inject
    public ChatRepo(SocketManager socketManager) {
        Timber.d("Constructor called");
        this.socketManager = socketManager;
        incomingChatMessagesLiveData = new LiveEvent<>();
        incomingChatRoomActivationsLiveData = new LiveEvent<>();
        socketStatusLiveData = new MediatorLiveData<>();
        incomingChatMessagesCountLiveData = new MutableLiveData<>();
        observeSocketErrorsAndStatus();
    }

    /*
     * Must be called from any ViewModel using ChatRepo at constructor injection
     * */
    public void subscribeToChatRepo() {
        // Initiate socket if this is the first subscriber.
        if (noOfSubscribedViewModels == 0)
            initSocket();
        noOfSubscribedViewModels++;
        Timber.d("subscribeToChatRepo called, current noOfSubscribedViewModels: %d", noOfSubscribedViewModels);
    }

    /*
     * Must be called from any ViewModel using ChatRepo at ViewModel::onCleared
     * */
    @Override
    public void dispose() {
        noOfSubscribedViewModels--;
        // Destroy socket and dispose if this is the last subscriber.
        if (noOfSubscribedViewModels == 0) {
            destroySocket();
            super.dispose();
        }
        Timber.d("dispose called, current noOfSubscribedViewModels: %d", noOfSubscribedViewModels);
    }

    // Public Getters

    public LiveData<Boolean> getSocketStatusLiveData() {
        return socketStatusLiveData;
    }

    public LiveEvent<ChatRoomModel> getIncomingChatRoomActivationsLiveData() {
        return incomingChatRoomActivationsLiveData;
    }

    public MutableLiveData<Integer> getIncomingChatMessagesCountLiveData() {
        return incomingChatMessagesCountLiveData;
    }

    public LiveData<ChatMessageModel> getIncomingChatMessagesLiveData() {
        return incomingChatMessagesLiveData;
    }

    // Socket

    public LiveData<Boolean> sendChatRoomActivationMessage(String reservationId, boolean active) {
        JSONObject object = new JSONObject();
        try {
            object.put("ref_id", reservationId);
            object.put("active", active);
        } catch (JSONException e) {
            handleError(e);
        }
        return socketManager.emit(SocketManager.EMIT_ROOM_ACTIVATION, object);
    }

    public LiveData<Boolean> sendChatMessage(String roomId, String body, @MessageType int messageType, @Nullable Long voiceDuration, @Nullable String documentName) {
        JSONObject object = new JSONObject();
        try {
            object.put("room_id", roomId);
            object.put("message", body);
            object.put("type", messageType);
            object.put("duration", voiceDuration);
            object.put("fileName", documentName);
        } catch (JSONException e) {
            handleError(e);
        }
        return socketManager.emit(SocketManager.EMIT_MESSAGE, object);
    }

    public LiveData<Boolean> seenMessage(String roomId) {
        JSONObject object = new JSONObject();
        try {
            object.put("room_id", roomId);

        } catch (JSONException e) {
            handleError(e);
        }
        return socketManager.emit(SocketManager.EMIT_REED_MESSAGE, object);

    }

    private void initSocket() {
        Timber.d("initSocket called");
        disposable = new CompositeDisposable();
//        if (!userPref.isLoggedin())
//            return;
        socketManager.init();
        observeIncomingChatRoomActivations();
        observeIncomingChatMessages();
    }

    private void destroySocket() {
        Timber.d("destroySocket called");
        socketManager.destroy();
    }

    private void observeSocketErrorsAndStatus() {
        errorLiveData.addSource(socketManager.errorLiveData, this::handleError);
        socketStatusLiveData.addSource(socketManager.statusLiveData, status -> socketStatusLiveData.setValue(status));
    }

    private void observeIncomingChatRoomActivations() {
        socketManager.observe(SocketManager.OBSERVE_ROOM_ACTIVATION, args -> {
            if (args == null || incomingChatRoomActivationsLiveData == null)
                return;
            try {
                ChatRoomModel chatRoomModel = new Gson().fromJson(args[0].toString(), ChatRoomModel.class);

                if (Looper.myLooper() == Looper.getMainLooper())
                    incomingChatRoomActivationsLiveData.setValue(chatRoomModel);
                else
                    incomingChatRoomActivationsLiveData.postValue(chatRoomModel);
            } catch (JsonSyntaxException e) {
                handleError(e);
            }
        });
    }

    private void observeIncomingChatMessages() {
        socketManager.observe(SocketManager.OBSERVE_MESSAGE, args -> {
            if (args == null || incomingChatMessagesLiveData == null)
                return;
            try {
                ChatMessageModel message = new Gson().fromJson(args[0].toString(), ChatMessageModel.class);
//                try {
//                    String msgEncode = URLEncoder.encode(message.getBody(), "UTF-8");
//                    message.setBody(msgEncode);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.println();

                if (Looper.myLooper() == Looper.getMainLooper())
                    incomingChatMessagesLiveData.setValue(message);
                else
                    incomingChatMessagesLiveData.postValue(message);

                incomingChatMessagesCountLiveData.postValue(1);
            } catch (JsonSyntaxException e) {
                handleError(e);
            }
        });
    }

    // Apis

    public LiveData<ChatRoomsResponse> getChatRooms() {
        MutableLiveData<ChatRoomsResponse> liveData = new MutableLiveData<>();
        Observable<ChatRoomsResponse> observable = networkManager.getRequest(Endpoints.CHAT_ROOMS, null, ChatRoomsResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }


    public MutableLiveData<ChatMessageCountResponse> getUnreadMessages() {
        MutableLiveData<ChatMessageCountResponse> liveData = new MutableLiveData<>();
        Observable<ChatMessageCountResponse> observable = networkManager.getRequest(Endpoints.CHAT_UNREAD_MESSAGE, null, ChatMessageCountResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }


    public LiveData<ChatRoomModel> getChatRoomInternal(String chatRoomId) {
        MediatorLiveData<ChatRoomModel> liveData = new MediatorLiveData<>();
        Observable<ChatRoomResponse> observable = networkManager.getRequest(Endpoints.INTERNAL_CHAT_ROOM + "/" + chatRoomId, null, ChatRoomResponse.class);
        disposable.add(observable.subscribe(value -> liveData.setValue(value.getRoom()), this::handleError));
        return liveData;
    }

    public LiveData<ChatRoomModel> getChatRoomInternalByReservationId(String reservationId) {
        MediatorLiveData<ChatRoomModel> liveData = new MediatorLiveData<>();
        Observable<ChatRoomResponse> observable = networkManager.getRequest(Endpoints.INTERNAL_CHAT_ROOM_RESERVATION_REFERENCE + "/" + reservationId, null, ChatRoomResponse.class);
        disposable.add(observable.subscribe(value -> liveData.setValue(value.getRoom()), this::handleError));
        return liveData;
    }

    public LiveData<ChatRoomMessagesResponse> getChatRoomMessages(String chatRoomId, Long topMessageTimeStamp) {
        MutableLiveData<ChatRoomMessagesResponse> liveData = new MutableLiveData<>();
        Map<String, Object> data = null;
        if (topMessageTimeStamp != null)
            data = new HashMap<String, Object>() {{
                put("timestamp", topMessageTimeStamp);
            }};
        Observable<ChatRoomMessagesResponse> observable = networkManager.getRequest(Endpoints.CHAT_ROOM_MESSAGES + "/" + chatRoomId, data, ChatRoomMessagesResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<InputStream> getDocumentDownloadStream(String documentUrl) {
        MutableLiveData<InputStream> liveData = new MutableLiveData<>();
        Observable<ResponseBody> observable = networkManager.getDownloadRequest(documentUrl);
        disposable.add(observable.subscribe(responseBody -> liveData.setValue(responseBody.byteStream()), this::handleError));
        return liveData;
    }


}
