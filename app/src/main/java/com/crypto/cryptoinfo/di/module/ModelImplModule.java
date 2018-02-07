package com.crypto.cryptoinfo.di.module;

import com.crypto.cryptoinfo.repository.network.ModelImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelImplModule {

    @Singleton
    @Provides
    ModelImpl provideModelImpl(){
        return new ModelImpl();
    }
}
