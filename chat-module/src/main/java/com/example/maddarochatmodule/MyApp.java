package com.example.maddarochatmodule;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.example.maddarochatmodule.di.application.ApplicationComponent;
import com.example.maddarochatmodule.di.application.ApplicationModule;
import com.example.maddarochatmodule.di.application.DaggerApplicationComponent;
import com.example.maddarochatmodule.util.LocaleHelper;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class MyApp extends Application {
    protected ApplicationComponent applicationComponent;

    private static Context context;
    private static MyApp app;

    public static MyApp get(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    public static MyApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = app.getApplicationContext();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //Intcore1
        // Bearer intcore1
        // 6113c7774edd90bb2296856f

        //Intcore2
        // Bearer intcore2
        // 6113c8174edd90bb22968570

        applicationComponent.getUserPref().setUserId("Bearer intcore2");
//        applicationComponent.getUserPref().setUserId("6113c7774edd90bb2296856f");

//        Lingver.init(this, LocaleHelper.getLanguage(this));

        RxPaparazzo.register(this);

        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void notifyLanguageChanged() {
        context = LocaleHelper.onAttach(app);
    }

    public static Context getContext() {
        return context;
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        context = LocaleHelper.onAttach(this);
    }
}
