package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.voice_messages;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.util.ChatUtils;
import com.stfalcon.chatkit.messages.MessageHolders;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomOutcomingVoiceMessageViewHolder
        extends MessageHolders.BaseOutcomingMessageViewHolder<ChatMessageModel> {

    @BindView(R.id.cl_bubble)
    ConstraintLayout bubble;
    @BindView(R.id.iv_play_stop)
    ImageView ivPlayStop;
    @BindView(R.id.tv_message_voice_duration)
    TextView tvMessageDuration;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    private VoiceMessagesListener voiceMessagesListener;

    public CustomOutcomingVoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        ButterKnife.bind(this, itemView);
        voiceMessagesListener = (VoiceMessagesListener) payload;
    }

    @Override
    public void onBind(ChatMessageModel message) {
        ChatUtils.setTimeTextView(message.getCreatedAt(), tvMessageTime);

        ChatUtils.setChatBubbleBackground(bubble, false);

        ivPlayStop.setImageResource(message.isVoicePlaying() ? R.drawable.ic_stop : R.drawable.ic_play);
        ivPlayStop.setOnClickListener(
                v -> {
                    if (voiceMessagesListener == null)
                        return;
                    if (message.isVoicePlaying())
                        voiceMessagesListener.onRequestingStop();
                    else
                        voiceMessagesListener.onMessageRequestingPlay(message);
                });

        //Todo: voice duration not returned by the backend
        tvMessageDuration.setText(ChatUtils.getHumanDurationText(message.getVoiceDuration() == null ? 0L : message.getVoiceDuration()));
    }
}
