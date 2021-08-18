package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.document_messages;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.util.ChatUtils;
import com.example.maddarochatmodule.util.StringUtils;
import com.stfalcon.chatkit.messages.MessageHolders;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomOutcomingDocumentMessageViewHolder
        extends MessageHolders.BaseOutcomingMessageViewHolder<ChatMessageModel> {

    @BindView(R.id.cl_bubble)
    ConstraintLayout bubble;
    @BindView(R.id.ll_document)
    LinearLayout llDocument;
    @BindView(R.id.tv_document_name)
    TextView tvDocumentName;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    private DocumentMessageListener documentMessageListener;

    public CustomOutcomingDocumentMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        ButterKnife.bind(this, itemView);
        documentMessageListener = (DocumentMessageListener) payload;
    }

    @Override
    public void onBind(ChatMessageModel message) {
        ChatUtils.setTimeTextView(message.getCreatedAt(), tvMessageTime);

        ChatUtils.setChatBubbleBackground(bubble, false);

        llDocument.setOnClickListener(
                v -> {
                    if (documentMessageListener != null)
                        documentMessageListener.onMessageRequestingPreview(message);
                });

        tvDocumentName.setText(TextUtils.isEmpty(message.getDocumentName()) ? StringUtils.getString(R.string.multimedia_message) : message.getDocumentName());
    }
}
