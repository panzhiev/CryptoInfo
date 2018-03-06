package com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.FavActivity;
import com.crypto.cryptoinfo.ui.activity.MainActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.AllCoinsFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter.CoinsAdapter;
import com.crypto.cryptoinfo.ui.fragment.favouritesCoinsFragment.viewModel.CoinsFavListViewModel;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crypto.cryptoinfo.utils.Constants.MAIN_SCREEN;

public class FavouritesCoinsFragment extends Fragment implements IBaseFragment, CoinsAdapter.OnCoinItemClickListener {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.rv_fav_coins)
    public RecyclerView mRvCurrencies;

    @BindView(R.id.ll_sort_fav)
    public LinearLayout mLlSort;

    @BindView(R.id.ll_sort_rank_fav)
    public LinearLayout mLlSortRank;

    @BindView(R.id.ll_sort_price_fav)
    public LinearLayout mLlSortPrice;

    @BindView(R.id.ll_sort_cap_fav)
    public LinearLayout mLlSortCap;

    @BindView(R.id.ll_sort_1h_fav)
    public LinearLayout mLlSort1h;

    @BindView(R.id.swipe_refresh_fav)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.iv_close_sort_fav)
    public ImageView mIvCloseSort;

    private CoinsAdapter mCoinsAdapter;
    private CoinsFavListViewModel mCoinsFavListViewModel;
    private CoinsPresenter mCoinsPresenter;
    private ProgressDialog mProgressDialog;
    private List<CoinPojo> mCoinPojoList = new ArrayList<>();

    private boolean isVisibleSortLayout = false;
    private boolean isVisibleSearchLayout = false;
    private boolean isSortRankUp = false;
    private boolean isSortPriceUp = false;
    private boolean isSortCapUp = false;
    private boolean isSort1hUp = false;

    public FavouritesCoinsFragment() {
        // Required empty public constructor
    }

    public static FavouritesCoinsFragment newInstance() {
        return new FavouritesCoinsFragment();
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
        View view = inflater.inflate(R.layout.fragment_fav_coins, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        setListeners();
        ((MainActivity) getActivity()).setToolbarTitle(this.getResources().getString(R.string.title_fav_coins));

        mCoinsFavListViewModel = ViewModelProviders.of(this).get(CoinsFavListViewModel.class);
        mCoinsFavListViewModel.getCoinsFavList().observe(this, coins -> {
                    mCoinPojoList = coins;
                    setList((ArrayList) coins);
                }
        );

//        searchDisposable = textChanges(mEtSearch)
//                .map(inputText -> filter(inputText.toString()))
//                .subscribe(list -> setList((ArrayList) list), Throwable::printStackTrace);

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
            if (isVisibleSortLayout) {
                selectRank();
                setSortLayoutVisibility();
            }
        });
        mIvCloseSort.setOnClickListener(v -> setSortLayoutVisibility());
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
        assert mLlSort != null;
        if (!isVisibleSortLayout) {
            mLlSort.setVisibility(View.VISIBLE);
        } else {
            mLlSort.setVisibility(View.GONE);
        }
        isVisibleSortLayout = !isVisibleSortLayout;
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
        DialogFactory.createGenericErrorDialog(getContext(), R.string.error_message).show();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String valueMainScreen = prefs.getString(MAIN_SCREEN, "0");
        if (valueMainScreen.equals("0")) {
            ((MainActivity) getActivity()).navigatorBackPressed(AllCoinsFragment.newInstance());
        } else {
            getActivity().finishAffinity();
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
        inflater.inflate(R.menu.favourites_menu, menu);
        Utils.setToolbarIconsColor(getContext(), menu, R.color.colorTextDefault);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                setSortLayoutVisibility();
                return true;
            case R.id.action_edit_favourites:
                startActivity(new Intent(getContext(), FavActivity.class));
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
    }

    @Override
    public void onCoinItemClick(CoinPojo coinPojo) {
        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
    }
}
