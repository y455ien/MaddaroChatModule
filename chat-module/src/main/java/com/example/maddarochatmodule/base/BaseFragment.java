package com.example.maddarochatmodule.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.maddarochatmodule.MyApp;
import com.example.maddarochatmodule.R;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.data_model.ErrorModel;
import com.example.maddarochatmodule.di.baseview.BaseViewModule;
import com.example.maddarochatmodule.di.fragment.DaggerFragmentComponent;
import com.example.maddarochatmodule.di.fragment.FragmentComponent;
import com.example.maddarochatmodule.di.viewmodel.DaggerViewModelFactory;
import com.example.maddarochatmodule.util.StringUtils;
import com.example.maddarochatmodule.view.LoadingDialog;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public abstract class BaseFragment extends Fragment implements BaseView {

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void inject();

    protected abstract void onViewCreated();

    @Inject
    protected DaggerViewModelFactory viewModelFactory;
    @Inject
    protected UserPref userPref;

    protected FragmentComponent daggerComponent;
    protected View view;
    protected LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(), container, false);

        initDagger();
        inject();

        initButterKnife();
        initBasicViews();

        onViewCreated();
        return view;
    }

    private void initBasicViews() {
        loadingDialog = new LoadingDialog(getContext());
    }

    private void initDagger() {
        daggerComponent = DaggerFragmentComponent
                .builder()
                .applicationComponent(MyApp.get(requireContext()).getComponent())
                .baseViewModule(new BaseViewModule(this))
                .build();
        daggerComponent.inject(this);
    }

    private void initButterKnife() {
        ButterKnife.bind(this, view);
    }

    protected void onError(ErrorModel errorModel) {
        hideLoading();
        if (errorModel.isAuthError() && isAdded()) {
            showErrorMsg("Session Expired");
        } else {
            showErrorMsg(errorModel.getMessage());
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        try {
            Toasty.error(requireContext(), msg, Toasty.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSuccessMsg(String msg) {
        try {
            Toasty.success(requireContext(), msg, Toasty.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading(String msg) {
        if (loadingDialog != null && isAdded())
            loadingDialog.show(msg);
    }

    @Override
    public void showLoading() {
        showLoading(StringUtils.getString(R.string.loading_dialog_loading_msg));
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && isAdded())
            loadingDialog.dismiss();
    }

    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view == null) {
                view = new View(getContext());
            }
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
