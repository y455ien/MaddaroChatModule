package com.example.maddarochatmodule.chat_module.chat.chat_internal.document_view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProvider;

import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.base.BaseActivity;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.util.StringUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.intcore.statefullayout.StatefulLayout;

import java.io.InputStream;

import butterknife.BindView;

public class DocumentViewActivity extends BaseActivity {

    private static final String DATA = "data";

    public static Intent getIntent(Context context, String pdfUrl) {
        Intent intent = new Intent(context, DocumentViewActivity.class);
        intent.putExtra(DATA, pdfUrl);
        return intent;
    }

    @BindView(R.id.stateful_layout)
    StatefulLayout statefulLayout;
    @BindView(R.id.pdfView)
    PDFView pdfView;

    private DocumentViewViewModel viewModel;

    private String pdfUrl;

    @Override
    protected int getLayout() {
        return R.layout.activity_document_view;
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DocumentViewViewModel.class);
    }

    @Override
    public void showLoading() {
        statefulLayout.showLoading();
    }

    @Override
    protected void onError(ErrorModel errorModel) {
        if(errorModel.isNetworkError())
            statefulLayout.showConnectionError();
        else
            statefulLayout.showUnKnownError();
        super.onError(errorModel);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.document_view));

        if (!getDataFromIntent()) {
            statefulLayout.showUnKnownError();
            return;
        }

        viewModel.getErrorLiveData().observe(this, this::onError);

        statefulLayout.setClickListener(this::getDocumentInputStream);

        getDocumentInputStream();
    }

    private boolean getDataFromIntent() {
        if (TextUtils.isEmpty(getIntent().getStringExtra(DATA)))
            return false;
        pdfUrl = getIntent().getStringExtra(DATA);
        return true;
    }

    private void getDocumentInputStream(){
        showLoading();
        viewModel.getDocumentDownloadStream(pdfUrl).observe(this, this::onReceiveDocumentInputStream);
    }

    private void onReceiveDocumentInputStream(InputStream inputStream){
        hideLoading();
        statefulLayout.showContentAndLockConnectionErrors();
        pdfView.fromStream(inputStream).load();
    }
}
