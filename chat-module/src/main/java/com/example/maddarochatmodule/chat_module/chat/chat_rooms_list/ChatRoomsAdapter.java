package com.example.maddarochatmodule.chat_module.chat.chat_rooms_list;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.base.BaseRecyclerAdapter;
import com.example.maddarochatmodule.base.ItemClickListener;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.annotation.MessageType;
import com.example.maddarochatmodule.util.ChatUtils;
import com.example.maddarochatmodule.util.PicassoHelper;
import com.example.maddarochatmodule.util.StringUtils;
import com.example.maddarochatmodule.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatListViewHolder>
        implements BaseRecyclerAdapter<ChatRoomModel> {

    private String userId;
    private List<ChatRoomModel> list;
    private ItemClickListener<ChatRoomModel> listener;

    private int width, height, spacing, halfSpacing;

    ChatRoomsAdapter(String userId, FragmentActivity activity, ItemClickListener<ChatRoomModel> listener) {
        this.userId = userId;

        this.listener = listener;

        this.list = new ArrayList<>();

        spacing = (int) activity.getResources().getDimension(R.dimen.padding);
        halfSpacing = (int) activity.getResources().getDimension(R.dimen.half_padding);
        width = Utils.getScreenDisplayMetrics(activity).widthPixels - 2 * spacing;
        height = (int) (width * 0.27);
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.bindData(position);
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent)
        ConstraintLayout parent;
        @BindView(R.id.iv_chat_buddy_image)
        ImageView ivChatBuddyImage;
        @BindView(R.id.tv_chat_buddy_name)
        TextView tvChatBuddyName;
        @BindView(R.id.tv_chat_last_message_body)
        TextView tvChatLastMessageBody;
        @BindView(R.id.tv_chat_last_message_date)
        TextView tvChatLastMessageDate;
        @BindView(R.id.view_indicator)
        View viewIndicator;

        ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(int position) {
            ChatRoomModel model = list.get(position);
            // Setting parent layoutParams
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);
            layoutParams.setMarginStart(spacing);
            layoutParams.setMarginEnd(spacing);
            layoutParams.topMargin = position == 0 ? spacing : halfSpacing;
            layoutParams.bottomMargin = position == list.size() - 1 ? spacing : 0;
            parent.setLayoutParams(layoutParams);

            // Setting parent click listener
            parent.setOnClickListener(view -> {
                if (listener != null)
                    listener.onItemClicked(model);
            });

            // Binding Data
            if (model.getOtherBuddy(userId) != null) {
                tvChatBuddyName.setText(Objects.requireNonNull(model.getOtherBuddy(userId)).getName());
                PicassoHelper.loadImageWithCache(Objects.requireNonNull(model.getOtherBuddy(userId)).getAvatar(), ivChatBuddyImage, PicassoHelper.CENTER_INSIDE, null, null, null);
            } else {
                tvChatBuddyName.setText("");
                ivChatBuddyImage.setImageResource(R.drawable.ic_user_placeholder);
            }

            if (model.getLastMessage() != null) {
                tvChatLastMessageBody.setText(model.getLastMessage().getType() == MessageType.TEXT
                        ? model.getLastMessage().getText()
                        : StringUtils.getString(R.string.multimedia_message));
            } else {
                tvChatLastMessageBody.setText("");
            }
            tvChatLastMessageBody.setTypeface(model.hasUnreadMessages() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

            tvChatLastMessageDate.setText(ChatUtils.formatChatRoomTime(model.getUpdatedAt()));

//            if (model.getUnreadMessages() > 0) {
//                viewIndicator.setVisibility(View.VISIBLE);
//            } else {
//                viewIndicator.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    public void clear(boolean notifyDataSetChanged) {
        list.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public void add(ChatRoomModel item) {
        int roomPosition = isRoomFound(item.getId());
        if (roomPosition != -1) {
            list.get(roomPosition).setLastMessage(item.getLastMessage());
            list.get(roomPosition).setUnreadMessages(1);
            if (item.getLastMessage() != null)
                list.get(roomPosition).setUpdatedAt(item.getLastMessage().getCreatedAtLong());
            sortRoomsList();
            notifyItemMoved(roomPosition, 0);
            notifyItemChanged(0);
        }
    }

    @Override
    public void addAll(List<ChatRoomModel> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    int isRoomFound(String roomId) {
        int found = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(roomId)) {
                found = i;
                break;
            }
        }
        return found;
    }

    public void resetMessageIndicator(String id) {
        try {
            list.forEach(i -> {
                if (i.getId().equals(id)) {
                    i.setUnreadMessages(0);
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    ChatRoomModel getChatRoomById(String roomId) {
        ChatRoomModel room = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(roomId)) {
                room = list.get(i);
                break;
            }
        }
        return room;
    }

    int getRoomPosition(ChatRoomModel roomId) {
        int position = 0;
        position = list.indexOf(roomId);
        return position;
    }

    private void sortRoomsList() {
        Collections.sort(list, (o1, o2) -> (int) (o2.getUpdatedAt() - o1.getUpdatedAt()));
    }

}
