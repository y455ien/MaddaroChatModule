package com.example.maddarochatmodule.chat_module.chat.chat_internal;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.example.maddarochatmodule.base.BaseViewModel;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.data_model.ChatRoomMessagesResponse;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.ChatRoomResponse;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.data_model.FileModel;
import com.example.maddarochatmodule.data_model.annotation.MessageType;
import com.example.maddarochatmodule.repo.ChatRepo;
import com.example.maddarochatmodule.repo.FileRepo;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ChatInternalViewModel extends BaseViewModel {

    @Inject
    FileRepo fileRepo;

    private ChatRepo chatRepo;

    private MediatorLiveData<ChatRoomResponse> chatRoomLiveData;
    private MediatorLiveData<ChatRoomMessagesResponse> historyChatMessagesLiveData;
    private MutableLiveData<Boolean> hasPendingMessagesToSendLD;

    private Long topMessageTimeStamp;
    private boolean noMoreMessages;

    @Inject
    ChatInternalViewModel(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;

        chatRepo.subscribeToChatRepo();

        chatRoomLiveData = new MediatorLiveData<>();
        historyChatMessagesLiveData = new MediatorLiveData<>();
        hasPendingMessagesToSendLD = new MutableLiveData<>();
        hasPendingMessagesToSendLD.setValue(false);
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(chatRepo, fileRepo);
        return super.getErrorLiveData();
    }

    public LiveData<Boolean> getHasPendingMessagesToSendLD() {
        return hasPendingMessagesToSendLD;
    }

    // Socket related

    LiveData<Boolean> getSocketStatusLiveData() {
        return chatRepo.getSocketStatusLiveData();
    }

    LiveData<ChatRoomModel> getIncomingChatRoomActivationsLiveData() {
        return chatRepo.getIncomingChatRoomActivationsLiveData();
    }

    LiveData<ChatMessageModel> getIncomingChatMessagesLiveData() {
        return chatRepo.getIncomingChatMessagesLiveData();
    }

    LiveData<Boolean> sendChatMessage(String roomId, String body, @MessageType int messageType, @Nullable Long voiceDuration, @Nullable String documentName) {
        hasPendingMessagesToSendLD.setValue(true);
        MediatorLiveData<Boolean> liveData = new MediatorLiveData<>();
        liveData.addSource(chatRepo.sendChatMessage(roomId, body, messageType, voiceDuration, documentName), aBoolean -> {
            liveData.setValue(aBoolean);
            hasPendingMessagesToSendLD.setValue(false);
        });
        return liveData;
    }

    LiveData<Boolean> sendSeenMessage(String roomId) {
         MediatorLiveData<Boolean> liveData = new MediatorLiveData<>();
        liveData.addSource(chatRepo.seenMessage(roomId), liveData::setValue);
        return liveData;
    }


    // APIs

    void getHistoryChatMessages(String chatRoomId) {
        if (noMoreMessages)
            return;
        view.showLoading();
        historyChatMessagesLiveData.addSource(chatRepo.getChatRoomMessages(chatRoomId, topMessageTimeStamp),
                chatRoomMessagesResponse -> {
                    view.hideLoading();
                    if (chatRoomMessagesResponse.getMessages() == null || chatRoomMessagesResponse.getMessages().isEmpty())
                        noMoreMessages = true;
                    else
                        topMessageTimeStamp = chatRoomMessagesResponse.getMessages().get(0).getCreatedAtLong();
                    historyChatMessagesLiveData.setValue(chatRoomMessagesResponse);
                });
    }

    LiveData<ChatRoomModel> getChatRoomLiveData() {
        return Transformations.map(chatRoomLiveData, ChatRoomResponse::getRoom);
    }

    LiveData<List<ChatMessageModel>> getHistoryChatMessagesLiveData() {
        return Transformations.map(historyChatMessagesLiveData, ChatRoomMessagesResponse::getMessages);
    }

    LiveData<FileModel> uploadMultimediaFile(File file) {
        return fileRepo.uploadFile(file);
    }


    @Override
    protected void onCleared() {
        fileRepo.dispose();
        chatRepo.dispose();
        super.onCleared();
    }
}
