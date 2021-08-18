package com.example.maddarochatmodule.di.activity;

import com.example.maddarochatmodule.base.BaseActivity;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.ChatInternalActivity;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.document_view.DocumentViewActivity;
import com.example.maddarochatmodule.chat_module.chat.chat_rooms_list.ChatRoomsActivity;
import com.example.maddarochatmodule.di.application.ApplicationComponent;
import com.example.maddarochatmodule.di.baseview.BaseViewModule;
import com.example.maddarochatmodule.di.viewmodel.ViewModelModule;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface ActivityComponent {
    void inject(BaseActivity activity);
//    void inject(ChatRoomsActivity activity);
    void inject(ChatInternalActivity activity);
    void inject(DocumentViewActivity activity);
}
