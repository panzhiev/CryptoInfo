package com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.List;

public class CoinsFavListViewModel extends ViewModel {

    private final LiveData<List<CoinPojo>> coinsListLiveData;

    public CoinsFavListViewModel() {
        coinsListLiveData = App.dbInstance.getCoinDao().getFavourites();
    }

    public LiveData<List<CoinPojo>> getCoinsFavList() {
        return coinsListLiveData;
    }

    public void deleteCurrenciesAsync() {
        new DeleteAsyncTask().execute();
    }

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            App.dbInstance.getCoinDao().deleteAll();
            return null;
        }
    }
}
