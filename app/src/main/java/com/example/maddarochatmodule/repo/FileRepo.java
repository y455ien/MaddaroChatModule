package com.example.maddarochatmodule.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.maddarochatmodule.base.BaseRepo;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.data_model.FileListResponse;
import com.example.maddarochatmodule.data_model.FileModel;
import com.example.maddarochatmodule.data_model.SuccessResponse;
import com.example.maddarochatmodule.network.Endpoints;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileRepo extends BaseRepo {

    @Inject
    UserPref userPref;

    @Inject
    public FileRepo() {
    }

    public LiveData<FileModel> uploadFile(File file) {
        Observable<FileModel> observable = getUploadObservable(file);
        MutableLiveData<FileModel> liveData = new MutableLiveData<>();
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<List<FileModel>> uploadFiles(List<File> list) {

        Observable<FileModel> observable = getUploadObservable(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            Observable<FileModel> b = getUploadObservable(list.get(i));
            observable = observable.mergeWith(b);
        }

        List<FileModel> doneFiles = new ArrayList<>();
        MutableLiveData<List<FileModel>> liveData = new MutableLiveData<>();
        disposable.add(observable
                .doOnComplete(() -> {
                    liveData.setValue(doneFiles);
                }).subscribe(doneFiles::add, this::handleError));

        return liveData;
    }

    private Observable<FileModel> getUploadObservable(File file) {
        RequestBody fileBody = RequestBody.create(file, MultipartBody.FORM);
        MultipartBody.Part fileMultiPart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        Map<String, RequestBody> body = new HashMap<String, RequestBody>() {{
        }};
//        body.put("file", fileBody);
        return networkManager.postMultiPart(Endpoints.UPLOAD_URL, body, fileMultiPart, FileModel.class);
    }

    public LiveData<FileListResponse> getUserFiles() {
        MutableLiveData<FileListResponse> liveData = new MutableLiveData<>();
        Observable<FileListResponse> observable = networkManager.getRequest(Endpoints.FILES, null, FileListResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<SuccessResponse> addDocument(String name, String categoryID, List<String> files) {
        Map<String, Object> body = new HashMap<String, Object>() {{
            put("name_ar", name);
            put("name_en", name);
            put("document_categories_id", categoryID);
//            put("user_id", userPref.getId());
            put("files", files);
        }};

        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();
        Observable<SuccessResponse> observable = networkManager.postRequest(Endpoints.FILES, body, SuccessResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<SuccessResponse> deleteDocument(String documentID) {
        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();
        Observable<SuccessResponse> observable = networkManager.deleteRequest(Endpoints.FILES + "/" + documentID, null, SuccessResponse.class);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
