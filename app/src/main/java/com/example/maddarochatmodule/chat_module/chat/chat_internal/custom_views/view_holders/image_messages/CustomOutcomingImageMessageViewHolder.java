package com.example.maddarochatmodule.chat_module.chat.chat_internal.custom_views.view_holders.image_messages;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.util.ChatUtils;
import com.example.maddarochatmodule.util.PicassoHelper;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.imageviewer.StfalconImageViewer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomOutcomingImageMessageViewHolder extends MessageHolders.BaseOutcomingMessageViewHolder<ChatMessageModel> {

    @BindView(R.id.cl_bubble)
    ConstraintLayout bubble;
    @BindView(R.id.iv_message_image)
    ImageView ivMessageImage;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    private Context context;

    public CustomOutcomingImageMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        ButterKnife.bind(this, itemView);
        this.context = (Context) payload;
    }

    @Override
    public void onBind(ChatMessageModel message) {
        ChatUtils.setTimeTextView(message.getCreatedAt(), tvMessageTime);

        bubble.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        PicassoHelper.loadImageWithCache("http://3.126.221.243:8080" + message.getPhotoUrl(), ivMessageImage, PicassoHelper.CENTER_CROP,
                null, null, null);

        if (context != null)
            ivMessageImage.setOnClickListener(v -> new StfalconImageViewer.Builder<>(context,
                    new String[]{message.getPhotoUrl()},
                    (imageView, image) -> PicassoHelper.loadImageWithCache("http://3.126.221.243:8080" + image, imageView, PicassoHelper.CENTER_INSIDE,
                            null, null, null)).withBackgroundColorResource(R.color.black).withBackgroundColor(Color.parseColor("#000000"))
                    .show());
    }
}
