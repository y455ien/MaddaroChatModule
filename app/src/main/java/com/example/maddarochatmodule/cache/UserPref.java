package com.example.maddarochatmodule.cache;

import com.example.maddarochatmodule.data_model.UserModel;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPref {

    private static UserModel userModel;
    private static String accessToken;

    private final static String KEY_MODEL = "user_model";
    private final static String KEY_ACCES_TOKEN = "access_token";
    private final static String KEY_GUEST_MODE = "guest_mode";
    private final static String KEY_NOTIFICATION_TOKEN = "notification_token";

    private final static String KEY_CHAT_TOKEN = "chat_token";

    @Inject
    public UserPref() {

    }

    public void setChatToken(String chatToken) {
        SharedPrefManager.getInstance().setString(KEY_CHAT_TOKEN, chatToken);
    }

    public String getChatToken() {
        return SharedPrefManager.getInstance().getString(KEY_CHAT_TOKEN);
    }

//    public  void setNotificationToken(String token){
//        SharedPrefManager.getInstance().setString(KEY_NOTIFICATION_TOKEN, token);
//    }
//
//    public String getNotificationToken(){
//        return SharedPrefManager.getInstance().getString(KEY_NOTIFICATION_TOKEN);
//    }
//
//    public void setAccessToken(String token) {
//        SharedPrefManager.getInstance().setString(KEY_ACCES_TOKEN, token);
//        accessToken = token;
//    }

//    public String getAccessToken() {
//        if (accessToken == null)
//            accessToken = SharedPrefManager.getInstance().getString(KEY_ACCES_TOKEN);
//
//        return accessToken;
//    }
//
//    public boolean isAccountActive() {
//        if (getUser() == null)
//            return false;
//
////        return getUser().getActivationStatus() == AccountStatus.ACTIVATED;
//        return true;
//    }
//
//    public void refreshSaveUser() {
//        if (userModel == null)
//            return;
//        String json = new Gson().toJson(userModel, UserModel.class);
//        SharedPrefManager.getInstance().setString(KEY_MODEL, json);
//    }

//    public void setUserModel(UserModel model) {
//        String json = new Gson().toJson(model, UserModel.class);
//        SharedPrefManager.getInstance().setString(KEY_MODEL, json);
//        userModel = null;
//        userModel = getUser();
//
//        if (model.getAccessToken() != null)
//            setAccessToken(model.getAccessToken());
//
//        setGuestMode(false);
//    }

//    public UserModel getUser() {
//        if (userModel == null) {
//            String json = SharedPrefManager.getInstance().getString(KEY_MODEL);
//            userModel = new Gson().fromJson(json, UserModel.class);
//        }
//
//        return userModel;
//    }

//    public boolean isLoggedin() {
//        return true;
////        return getAccessToken() != null && !getAccessToken().isEmpty()
////                && getUser().getActivationStatus() == AccountStatus.ACTIVATED;
//    }

//    public String getId() {
//        UserModel model = getUser();
//        if (model != null)
//            return model.getId();
//
//        return null;
//    }

//    public void logout() {
//        SharedPrefManager.getInstance().clear(KEY_ACCES_TOKEN);
//        SharedPrefManager.getInstance().clear(KEY_MODEL);
//        SharedPrefManager.getInstance().clear(KEY_NOTIFICATION_TOKEN);
//        userModel = null;
//        accessToken = null;
//    }

//    public void setGuestMode(boolean isGuestMode) {
//        SharedPrefManager.getInstance().setBoolean(KEY_GUEST_MODE, isGuestMode);
//    }

//    public boolean isGuestMode() {
//        return SharedPrefManager.getInstance().getBoolean(KEY_GUEST_MODE);
//    }
}
