package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;

import java.util.List;

public class MarketsViewModel extends ViewModel {

    private final LiveData<List<MarketPrice>> marketsListLiveData;
    public static String pair;

    public MarketsViewModel() {
        marketsListLiveData = App.dbInstance.getMarketPriceDao().getMarketsWithPairLiveData(pair);
    }

    public LiveData<List<MarketPrice>> getMarketsList() {
        return marketsListLiveData;
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
