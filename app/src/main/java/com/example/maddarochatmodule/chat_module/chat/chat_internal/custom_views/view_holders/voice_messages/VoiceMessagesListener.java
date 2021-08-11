package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.voice_messages;

import com.example.maddarochatmodule.data_model.ChatMessageModel;

public interface VoiceMessagesListener {
    void onMessageRequestingPlay(ChatMessageModel message);

    void onRequestingStop();
}
