package com.example.maddarochatmodule.di.fragment;


import com.example.maddarochatmodule.base.BaseFragment;
import com.example.maddarochatmodule.chat_module.chat.chat_rooms_list.ChatRoomsFragment;
import com.example.maddarochatmodule.di.application.ApplicationComponent;
import com.example.maddarochatmodule.di.baseview.BaseViewModule;
import com.example.maddarochatmodule.di.viewmodel.ViewModelModule;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface FragmentComponent {
    void inject(BaseFragment fragment);
    void inject(ChatRoomsFragment fragment);
}
