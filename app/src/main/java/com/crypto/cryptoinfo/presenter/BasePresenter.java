package com.crypto.cryptoinfo.presenter;


import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.network.ModelImpl;

import javax.inject.Inject;

public class BasePresenter {

    @Inject
    public ModelImpl mModel;

    public BasePresenter() {
        App.getApplicationComponent().inject(this);
    }
}
