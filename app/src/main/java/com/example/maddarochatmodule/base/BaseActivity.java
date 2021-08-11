package com.example.maddarochatmodule.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maddarochatmodule.MyApp;
import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.di.activity.ActivityComponent;
import com.example.maddarochatmodule.di.activity.DaggerActivityComponent;
import com.example.maddarochatmodule.di.baseview.BaseViewModule;
import com.example.maddarochatmodule.di.viewmodel.DaggerViewModelFactory;
import com.example.maddarochatmodule.util.LocaleHelper;
import com.example.maddarochatmodule.util.StringUtils;
import com.example.maddarochatmodule.view.LoadingDialog;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void inject();

    protected abstract void onViewCreated();

    @Inject
    protected DaggerViewModelFactory viewModelFactory;
    @Inject
    protected UserPref userPref;

    protected ActivityComponent daggerComponent;
    protected CompositeDisposable disposable = new CompositeDisposable();

    protected LoadingDialog loadingDialog;
    private boolean doubleBackToExitPressedOnce = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Force app in portrait
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (StringUtils.getLanguage().equals("ar"))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        setContentView(getLayout());

        initDagger();
        inject();

        initButterKnife();
        initBasicViews();

        onViewCreated();
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    protected void onError(ErrorModel errorModel) {
        hideLoading();
        if (errorModel.isAuthError()) {
            showErrorMsg("Session Expired");
        } else {
            showErrorMsg(errorModel.getMessage());
        }
    }

    private void initBasicViews() {
        loadingDialog = new LoadingDialog(this);
    }

    private void initDagger() {
        daggerComponent = DaggerActivityComponent.builder()
                .applicationComponent(MyApp.get(this).getComponent())
                .baseViewModule(new BaseViewModule(this))
                .build();
        daggerComponent.inject(this);
    }

    private void initButterKnife() {
        ButterKnife.bind(this);
    }

    protected void enableDoubleBackExit() {
        doubleBackToExitPressedOnce = false;
    }

    @Override
    public void showErrorMsg(String msg) {
        Toasty.error(this, msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMsg(String msg) {
        Toasty.success(this, msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(String msg) {
        hideKeyboard();
        if (loadingDialog != null)
            loadingDialog.show(msg);
    }

    @Override
    public void showLoading() {
        showLoading(StringUtils.getString(R.string.loading_dialog_loading_msg));
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    protected void setTitleWithBack(String title) {
        setTitle(title);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void showKeyboard(@NonNull View focusedView) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).showSoftInput(focusedView, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toasty.normal(this, StringUtils.getString(R.string.click_twice)).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
