package com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.List;

public class CurrenciesListViewModel extends ViewModel {

    private final LiveData<List<CoinPojo>> coinsListLiveData;

    public CurrenciesListViewModel() {
        coinsListLiveData = App.dbInstance.getCoinDao().getAll();
    }

    public LiveData<List<CoinPojo>> getCoinsList() {
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

//    public void insertCoinsAsync(List<CoinPojo> coinPojoList) {
//        new InsertAsyncTask().execute(coinPojoList);
//    }

//    private static class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground() {
//            App.dbInstance.getCoinDao().deleteAll();
//            return null;
//        }
//    }
}
