package com.example.maddarochatmodule.chat_module.chat.chat_rooms_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.example.maddarochatmodule.base.BaseViewModel;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.ChatRoomsResponse;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.repo.ChatRepo;


import java.util.List;

import javax.inject.Inject;

public class ChatRoomsViewModel extends BaseViewModel {

    private ChatRepo chatRepo;
    int lastClickedRoom = -1;
    String lastClickedRoomId = "";
    private MediatorLiveData<ChatRoomsResponse> chatRoomsLiveData;

    @Inject
    ChatRoomsViewModel(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;

        chatRepo.subscribeToChatRepo();

        chatRoomsLiveData = new MediatorLiveData<>();
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(chatRepo);
        return super.getErrorLiveData();
    }


    void getChatRooms() {
        chatRoomsLiveData.addSource(chatRepo.getChatRooms(),
                chatRoomsResponse -> chatRoomsLiveData.setValue(chatRoomsResponse));
    }

    void getChatUnreadMessages() {

    }

    LiveData<List<ChatRoomModel>> getChatRoomsLiveData() {
        return Transformations.map(chatRoomsLiveData, ChatRoomsResponse::getChatRooms);
    }

    LiveData<Boolean> getStatusLiveData() {
        return chatRepo.getSocketStatusLiveData();
    }

    LiveData<ChatRoomModel> getIncomingChatRoomActivationsLiveData() {
        return chatRepo.getIncomingChatRoomActivationsLiveData();
    }

    LiveData<ChatMessageModel> getIncomingChatMessagesLiveData() {
        return chatRepo.getIncomingChatMessagesLiveData();
    }

    @Override
    protected void onCleared() {
        chatRepo.dispose();
        super.onCleared();
    }
}
