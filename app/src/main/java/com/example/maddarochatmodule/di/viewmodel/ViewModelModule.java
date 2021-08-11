package com.example.maddarochatmodule.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.maddarochatmodule.chat_module.chat.chat_internal.ChatInternalViewModel;
import com.example.maddarochatmodule.chat_module.chat.chat_internal.document_view.DocumentViewViewModel;
import com.example.maddarochatmodule.chat_module.chat.chat_rooms_list.ChatRoomsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract public class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(ChatRoomsViewModel.class)
    abstract ViewModel provideChatRoomsViewModel(ChatRoomsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatInternalViewModel.class)
    abstract ViewModel provideChatInternalViewModel(ChatInternalViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DocumentViewViewModel.class)
    abstract ViewModel provideDocumentViewViewModel(DocumentViewViewModel viewModel);
}
