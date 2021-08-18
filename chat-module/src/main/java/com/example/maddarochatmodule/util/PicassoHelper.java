package com.example.maddarochatmodule.util;

import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class PicassoHelper {

    public interface SuccessListener {
        void onSuccess();
    }

    public interface FailureListener {
        void onFailure();
    }

    public static final int JUST_FIT = 0, CENTER_CROP = 1, CENTER_INSIDE = 2;

    public static void loadImageWithCache(
            final String url, ImageView imageView, int mode, @Nullable Integer errorResID, @Nullable SuccessListener successListener, @Nullable FailureListener failureListener) {

        if (TextUtils.isEmpty(url)) {
            if (failureListener != null)
                failureListener.onFailure();
            return;
        }

        RequestCreator requestCreator = Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .noFade();

        if (mode == CENTER_CROP)
            requestCreator = requestCreator.centerCrop();
        else if (mode == CENTER_INSIDE)
            requestCreator = requestCreator.centerInside();

        requestCreator.into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.e("YASSIEN PICASSO " , "SUCCESS");
                if (successListener != null)
                    successListener.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                Log.e("YASSIEN PICASSO " , e.toString());
                loadImageSkippingCache(url, imageView, mode, errorResID, successListener, failureListener);
            }
        });
    }


    // Access can be public
    private static void loadImageSkippingCache(
            final String url, ImageView imageView, int mode, @Nullable Integer errorResID, @Nullable SuccessListener successListener, @Nullable FailureListener failureListener) {

        if (TextUtils.isEmpty(url)) {
            if (failureListener != null)
                failureListener.onFailure();
            return;
        }

        RequestCreator requestCreator = Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .noFade();

        if (mode == CENTER_CROP)
            requestCreator = requestCreator.centerCrop();
        else if (mode == CENTER_INSIDE)
            requestCreator = requestCreator.centerInside();

        if (errorResID != null)
            requestCreator = requestCreator.error(errorResID);
        else
            requestCreator = requestCreator.error(new ColorDrawable(0xFFFFFF));

        requestCreator.into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if (successListener != null)
                    successListener.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                if (failureListener != null)
                    failureListener.onFailure();
            }
        });
    }

}
