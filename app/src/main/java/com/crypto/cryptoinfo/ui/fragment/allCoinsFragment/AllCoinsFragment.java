package com.crypto.cryptoinfo.ui.fragment.allCoinsFragment;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.activity.MainActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter.CoinsAdapter;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.viewModel.CoinsListViewModel;
import com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment.FavouritesCoinsFragment;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.crypto.cryptoinfo.utils.Constants.COIN;
import static com.crypto.cryptoinfo.utils.Constants.MAIN_SCREEN;
import static com.jakewharton.rxbinding2.widget.RxTextView.textChanges;

public class AllCoinsFragment extends Fragment implements IBaseFragment, CoinsAdapter.OnCoinItemClickListener {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.rv_all_coins)
    public RecyclerView mRvCurrencies;

    @BindView(R.id.ll_sort)
    public LinearLayout mLlSort;

    @BindView(R.id.ll_search)
    public LinearLayout mLlSearch;

    @BindView(R.id.ll_sort_rank)
    public LinearLayout mLlSortRank;

    @BindView(R.id.ll_sort_price)
    public LinearLayout mLlSortPrice;

    @BindView(R.id.et_search)
    public EditText mEtSearch;

    @BindView(R.id.ll_sort_cap)
    public LinearLayout mLlSortCap;

    @BindView(R.id.ll_sort_1h)
    public LinearLayout mLlSort1h;

    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.iv_close_sort)
    public ImageView mIvCloseSort;

    @BindView(R.id.iv_close_search)
    public ImageView mIvCloseSearch;

    private CoinsAdapter mCoinsAdapter;
    private CoinsListViewModel mCoinsListViewModel;
    private CoinsPresenter mCoinsPresenter;
    private ProgressDialog mProgressDialog;
    private List<CoinPojo> mCoinPojoList = new ArrayList<>();

    private boolean isVisibleSortLayout = false;
    private boolean isVisibleSearchLayout = false;
    private boolean isSortRankUp = false;
    private boolean isSortPriceUp = false;
    private boolean isSortCapUp = false;
    private boolean isSort1hUp = false;
    private Disposable searchDisposable;

    public AllCoinsFragment() {
        // Required empty public constructor
    }

    public static AllCoinsFragment newInstance() {
        return new AllCoinsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mCoinsPresenter == null) {
            mCoinsPresenter = new CoinsPresenter(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_all_coins, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        setListeners();
        ((MainActivity) getActivity()).setToolbarTitle(this.getResources().getString(R.string.title_all_coins));

        mCoinsListViewModel = ViewModelProviders.of(this).get(CoinsListViewModel.class);
        mCoinsListViewModel.getCoinsList().observe(this, coins -> {
                    mCoinPojoList = coins;
                    setList((ArrayList) coins);
                }
        );

        searchDisposable = textChanges(mEtSearch)
                .map(inputText -> filter(inputText.toString()))
                .subscribe(list -> setList((ArrayList) list), Throwable::printStackTrace);

        return view;
    }

    private List<CoinPojo> filter(String input) {

        List<CoinPojo> searchingCoinPogoList = new ArrayList<>();

        if (!mCoinPojoList.isEmpty()) {
            for (CoinPojo coinPojo : mCoinPojoList) {
                if (coinPojo.getSymbol().toLowerCase().contains(input.toLowerCase()) || coinPojo.getName().toLowerCase().contains(input.toLowerCase())) {
                    searchingCoinPogoList.add(coinPojo);
                }
            }
        }
        return searchingCoinPogoList;
    }

    private void setUpRecyclerView() {
        mRvCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCurrencies.setHasFixedSize(true);
    }

    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mCoinsPresenter.getCurrenciesList();
//            mCoinsPresenter.getChartsData("bitcoin", "1517915664000");
//            if (isVisibleSortLayout) {
//                selectRank();
//                setSortLayoutVisibility();
//            }

            if (isVisibleSearchLayout) {
                setSearchLayoutVisibility();
                mEtSearch.setText("");
            }
        });
        mIvCloseSort.setOnClickListener(v -> setSortLayoutVisibility());
        mIvCloseSearch.setOnClickListener(v -> setSearchLayoutVisibility());
        mLlSortRank.setOnClickListener(v -> {
            selectRank();
            mCoinsPresenter.sortListByRank((ArrayList<CoinPojo>) mCoinPojoList, isSortRankUp);
            isSortRankUp = !isSortRankUp;
        });

        mLlSortPrice.setOnClickListener(v -> {
            selectPrice();
            mCoinsPresenter.sortListByPrice((ArrayList<CoinPojo>) mCoinPojoList, isSortPriceUp);
            isSortPriceUp = !isSortPriceUp;
        });
        mLlSortCap.setOnClickListener(v -> {
            selectCap();
            mCoinsPresenter.sortListByCap((ArrayList<CoinPojo>) mCoinPojoList, isSortCapUp);
            isSortCapUp = !isSortCapUp;
        });
        mLlSort1h.setOnClickListener(v -> {
            select1h();
            mCoinsPresenter.sortListBy1h((ArrayList<CoinPojo>) mCoinPojoList, isSort1hUp);
            isSort1hUp = !isSort1hUp;
        });
    }

    private void setSortLayoutVisibility() {
        if (!isVisibleSortLayout) {
            mLlSort.setVisibility(View.VISIBLE);
        } else {
            mLlSort.setVisibility(View.GONE);
        }
        isVisibleSortLayout = !isVisibleSortLayout;
    }

    private void setSearchLayoutVisibility() {
        if (!isVisibleSearchLayout) {
            mLlSearch.setVisibility(View.VISIBLE);
        } else {
            mLlSearch.setVisibility(View.GONE);
        }
        isVisibleSearchLayout = !isVisibleSearchLayout;
    }

    private void selectRank() {
        mLlSortRank.setBackground(getResources().getDrawable(R.drawable.sort_enabled_bg));
        mLlSortPrice.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortCap.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSort1h.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
    }

    private void selectPrice() {
        mLlSortRank.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortPrice.setBackground(getResources().getDrawable(R.drawable.sort_enabled_bg));
        mLlSortCap.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSort1h.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
    }

    private void selectCap() {
        mLlSortRank.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortPrice.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortCap.setBackground(getResources().getDrawable(R.drawable.sort_enabled_bg));
        mLlSort1h.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
    }

    private void select1h() {
        mLlSortRank.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortPrice.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSortCap.setBackground(getResources().getDrawable(R.drawable.sort_disabled_bg));
        mLlSort1h.setBackground(getResources().getDrawable(R.drawable.sort_enabled_bg));
    }

    @Override
    public void setList(ArrayList list) {
        Log.d(TAG, "setList started");
        if (mCoinsAdapter == null) {
            Log.d(TAG, "mCoinsAdapter == null");
            mCoinsAdapter = new CoinsAdapter(list, Constants.COIN_DEFAULT_VIEW_TYPE, this);
            mRvCurrencies.setAdapter(mCoinsAdapter);
        } else {
            Log.d(TAG, "mCoinsAdapter != null");
            mCoinsAdapter.reloadList(list);
        }
    }

    @Override
    public void reloadList(ArrayList list) {

    }

    @Override
    public void showError() {
        Log.d(TAG, "showError");
        DialogFactory.createGenericErrorDialog(getContext(), R.string.error_message).show();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String valueMainScreen = prefs.getString(MAIN_SCREEN, "0");
        if (valueMainScreen.equals("0")) {
            getActivity().finishAffinity();
        } else {
            ((MainActivity) getActivity()).navigatorBackPressed(FavouritesCoinsFragment.newInstance());
        }
    }

    public String getCurrentTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.all_menu, menu);
        Utils.setToolbarIconsColor(getContext(), menu, R.color.colorTextDefault);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                if (isVisibleSearchLayout) {
                    setSearchLayoutVisibility();
                }
                setSortLayoutVisibility();
                return true;
            case R.id.action_search:
                if (isVisibleSortLayout) {
                    setSortLayoutVisibility();
                }
                setSearchLayoutVisibility();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgressIndicator() {
//        mProgressDialog = DialogFactory.createProgressDialog(getContext(), R.string.loading);
//        mProgressDialog.show();
//        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
//        mAVLoadingIndicatorView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCoinsPresenter != null) {
            mCoinsPresenter.unsubscribe();
        }

        if (!searchDisposable.isDisposed()) {
            searchDisposable.dispose();
        }
    }

    @Override
    public void onCoinItemClick(CoinPojo coinPojo) {
        Intent intent = new Intent(getContext(), CoinInfoActivity.class);
        intent.putExtra(COIN, coinPojo);
        startActivity(intent);
    }
}
