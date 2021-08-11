package com.example.maddarochatmodule.di.baseview;


import com.example.maddarochatmodule.base.BaseView;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseViewModule {

    public BaseView baseView;

    public BaseViewModule(BaseView baseView){
        this.baseView = baseView;
    }

    @Provides
    public BaseView provideBaseView(){
        return baseView;
    }
}
