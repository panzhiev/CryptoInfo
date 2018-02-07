package com.crypto.cryptoinfo.di.component;

import com.crypto.cryptoinfo.di.module.ApplicationModule;
import com.crypto.cryptoinfo.di.module.ModelImplModule;
import com.crypto.cryptoinfo.presenter.BasePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ModelImplModule.class})
public interface ApplicationComponent {

    //activities
    void inject(BasePresenter presenter);

    //fragments
}
