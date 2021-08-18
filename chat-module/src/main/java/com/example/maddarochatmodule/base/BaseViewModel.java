package com.example.maddarochatmodule.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.maddarochatmodule.data_model.ErrorModel;
import com.hadilq.liveevent.LiveEvent;

import javax.inject.Inject;

public class BaseViewModel extends ViewModel {

    @Inject
    protected BaseView view;
    public MediatorLiveData<ErrorModel> mediatorErrors;

    @Inject
    public BaseViewModel() {
        mediatorErrors = new MediatorLiveData<>();
    }

    protected void addErrorObservers(BaseRepo... repos) {
        if (repos == null || repos.length == 0)
            return;
        for (BaseRepo repo : repos) {
            LiveEvent<ErrorModel> live = repo.errorLiveData;
            if (live != null){
                mediatorErrors.removeSource(live);
                mediatorErrors.addSource(live,mediatorErrors::setValue);
            }
        }
    }

    protected LiveData<ErrorModel> getErrorLiveData() {
        return mediatorErrors;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
