package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.text_messages;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.util.ChatUtils;
import com.stfalcon.chatkit.messages.MessageHolders;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomOutcomingTextMessageViewHolder extends MessageHolders.BaseOutcomingMessageViewHolder<ChatMessageModel> {

    @BindView(R.id.cl_bubble)
    ConstraintLayout bubble;
    @BindView(R.id.tv_message_text)
    TextView tvMessageText;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(ChatMessageModel message) {
        ChatUtils.setTimeTextView(message.getCreatedAt(), tvMessageTime);

        ChatUtils.setChatBubbleBackground(bubble, false);

        tvMessageText.setText(message.getText());
    }
}
