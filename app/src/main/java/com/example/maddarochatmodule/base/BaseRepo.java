package com.example.maddarochatmodule.base;

import android.os.Looper;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.data_model.ErrorResponse;
import com.example.maddarochatmodule.network.NetworkManager;
import com.example.maddarochatmodule.util.StringUtils;
import com.google.gson.Gson;
import com.hadilq.liveevent.LiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;
import timber.log.Timber;

public class BaseRepo {

    @Inject
    protected NetworkManager networkManager;

    @Inject
    protected UserPref userPref;

    protected CompositeDisposable disposable = new CompositeDisposable();
    protected LiveEvent<ErrorModel> errorLiveData = new LiveEvent<>();

    @Inject
    protected BaseRepo() {
    }

    public void dispose() {
        disposable.dispose();
    }

    public void handleError(Throwable t) {
        Timber.e(t);

        ErrorModel error;

        if (t instanceof ErrorModel) {
            error = (ErrorModel) t;
        } else if (t instanceof HttpException) {
            error = getHttpErrorMessage((HttpException) t);

        } else if (t instanceof IOException) {
            error = new ErrorModel(StringUtils.getString(R.string.no_internet_connection));
            error.setCode(ErrorModel.ErrorCodes.NETWORK_ERROR);

        } else {
            error = new ErrorModel(StringUtils.getString(R.string.an_error_has_occurred));
        }

        if (Looper.myLooper() == Looper.getMainLooper())
            errorLiveData.setValue(error);
        else
            errorLiveData.postValue(error);

    }

    private ErrorModel getHttpErrorMessage(HttpException httpException) {
        Gson gson = new Gson();
        try {
            assert httpException.response().errorBody() != null;
            ErrorResponse errorResponse = gson.fromJson(httpException.response().errorBody().string(), ErrorResponse.class);

            ErrorModel model = errorResponse.getFirstError();
            if (model == null)
                model = new ErrorModel(StringUtils.getString(R.string.an_error_has_occurred), httpException.code());

            return model;

        } catch (Exception e) {
            return new ErrorModel(StringUtils.getString(R.string.an_error_has_occurred), httpException.code());
        }
    }

    protected void removeNulls(Map<String, Object> map) {
        if (map == null || map.isEmpty())
            return;

        List<String> toRemove = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key) == null)
                toRemove.add(key);
        }

        for (String key : toRemove) {
            map.remove(key);
        }
    }
}
