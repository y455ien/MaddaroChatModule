package com.example.maddarochatmodule.di.application;

import android.app.Application;
import android.content.Context;

import com.example.maddarochatmodule.MyApp;
import com.example.maddarochatmodule.cache.UserPref;
import com.example.maddarochatmodule.network.NetworkManager;
import com.example.maddarochatmodule.network.SocketManager;
import com.example.maddarochatmodule.repo.ChatRepo;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApp meddaroApp);

    Application getApplication();

    @ApplicationContext
    Context getContext();

    NetworkManager getNetworkManager();

    UserPref getUserPref();

    SocketManager getSocketManager();

    ChatRepo getChatRepo();
}
