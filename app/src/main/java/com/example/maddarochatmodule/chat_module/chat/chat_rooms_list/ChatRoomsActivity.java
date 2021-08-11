package com.example.maddarochatmodule.chat_module.chat.chat_rooms_list;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.base.BaseActivity;

public class ChatRoomsActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_chat_rooms;
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
    }

    @Override
    protected void onViewCreated() {

    }
}