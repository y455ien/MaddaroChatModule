package com.example.maddarochatmodule.chat_module.chat.chat_rooms_list;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maddarochatmodule.base.BaseFragment;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.ChatInternalActivity;
import com.example.maddarochatmodule.data_model.ChatMessageModel;
import com.example.maddarochatmodule.data_model.ChatRoomModel;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.intcore.statefullayout.StatefulLayout;
import com.example.maddarochatmodule.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChatRoomsFragment extends BaseFragment {

    @BindView(R.id.layout_stateful)
    StatefulLayout statefulLayout;
    @BindView(R.id.rv_chat_header_list)
    RecyclerView rvChatHeaderList;
    @BindView(R.id.pb_loadMore)
    ProgressBar pbLoadMore;
    int CHAT_REQUEST_CODE = 1;

    @Inject
    UserPref userPref;

    private ChatRoomsViewModel viewModel;

    private ChatRoomsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_chat_rooms;
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatRoomsViewModel.class);
    }

    @Override
    protected void onError(ErrorModel errorModel) {
        super.onError(errorModel);
        if (errorModel.isNetworkError())
            statefulLayout.showConnectionError();
    }

    @Override
    public void showLoading() {
        if (adapter.isEmpty()) {
            statefulLayout.showLoading();
        } else {
            pbLoadMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        pbLoadMore.setVisibility(View.GONE);
    }

    @Override
    protected void onViewCreated() {
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::onError);

        viewModel.getChatRoomsLiveData().observe(getViewLifecycleOwner(), this::onReceiveChatRooms);

        viewModel.getIncomingChatRoomActivationsLiveData().observe(getViewLifecycleOwner(), this::onReceiveIncomingChatRoomActivations);

        viewModel.getIncomingChatMessagesLiveData().observe(getViewLifecycleOwner(), this::onReceiveIncomingChatMessages);

        setUpViews();
        getChatRooms();
    }


    private void setUpViews() {
        statefulLayout.setClickListener(this::getChatRooms);

        adapter = new ChatRoomsAdapter(userPref.getUserId(), requireActivity(),
                model -> {
                    startActivityForResult(ChatInternalActivity.getChatInternalActivity(requireContext(), model), CHAT_REQUEST_CODE);
                    adapter.resetMessageIndicator(model.getId());
                    viewModel.lastClickedRoom = adapter.getRoomPosition(model);
                    viewModel.lastClickedRoomId = model.getId();
                });
        rvChatHeaderList.setAdapter(adapter);
        rvChatHeaderList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void getChatRooms() {
        showLoading();
        viewModel.getChatRooms();
    }

    private void onReceiveChatRooms(List<ChatRoomModel> chatHeaderList) {
        adapter.clear(false);
        adapter.addAll(chatHeaderList);
        hideLoading();

        if (adapter.isEmpty()) {
            statefulLayout.showEmpty();
        } else
            statefulLayout.showContentAndLockConnectionErrors();
    }

    private void onReceiveIncomingChatRoomActivations(ChatRoomModel chatRoomModel) {
        ChatRoomModel room = adapter.getChatRoomById(chatRoomModel.getId());
        if (room != null)
            room.setActive(chatRoomModel.isActive());
        else
            getChatRooms();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.lastClickedRoom != -1 && adapter != null) {
            adapter.resetMessageIndicator(String.valueOf(viewModel.lastClickedRoomId));
        }
    }

    private void onReceiveIncomingChatMessages(ChatMessageModel messageModel) {
        if (adapter.isRoomFound(messageModel.getRoomId()) != -1) {
            ChatRoomModel chatRoomModel = new ChatRoomModel();
            chatRoomModel.setId(messageModel.getRoomId());
            chatRoomModel.setLastMessage(messageModel);
            chatRoomModel.setUnreadMessages(1);
            chatRoomModel.setUpdatedAt(messageModel.getCreatedAtLong());
            adapter.add(chatRoomModel);

        } else
            getChatRooms();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHAT_REQUEST_CODE) {
//            ((HostActivity) Objects.requireNonNull(getActivity())).getUnReadMessageCount();
        }
    }

}
