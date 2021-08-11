package com.example.maddarochatmodule.chat_module.chat.chat_internal.document_view;

import androidx.lifecycle.LiveData;

import com.example.maddarochatmodule.base.BaseViewModel;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.repo.ChatRepo;

import java.io.InputStream;

import javax.inject.Inject;

public class DocumentViewViewModel extends BaseViewModel {

    private ChatRepo chatRepo;

    @Inject
    DocumentViewViewModel(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;

        chatRepo.subscribeToChatRepo();
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(chatRepo);
        return super.getErrorLiveData();
    }

    LiveData<InputStream> getDocumentDownloadStream(String documentUrl){
        return chatRepo.getDocumentDownloadStream(documentUrl);
    }

    @Override
    protected void onCleared() {
        chatRepo.dispose();
        super.onCleared();
    }
}
