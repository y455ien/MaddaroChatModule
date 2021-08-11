package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.document_messages;

import com.example.maddarochatmodule.data_model.ChatMessageModel;

public interface DocumentMessageListener {
    void onMessageRequestingPreview(ChatMessageModel message);
}
