package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views;

import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomMessagesListAdapter extends MessagesListAdapter<ChatMessageModel> {

    public CustomMessagesListAdapter(String senderId, ImageLoader imageLoader) {
        super(senderId, imageLoader);
    }

    public CustomMessagesListAdapter(String senderId, MessageHolders holders, ImageLoader imageLoader) {
        super(senderId, holders, imageLoader);
    }

    @SuppressWarnings("unchecked")
    public List<Wrapper> getAllItemsList() {
        return new ArrayList<>(items);
    }
}
