package com.crypto.cryptoinfo.ui.fragment.allCoinsFragment;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.presenter.CurrenciesPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.ui.activity.MainActivity;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter.CoinsAdapter;
import com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.viewModel.CurrenciesListViewModel;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCoinsFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.rv_my_nvs)
    public RecyclerView mRvCurrencies;

    @BindView(R.id.ll_sort)
    public LinearLayout mLlSort;

    @BindView(R.id.ll_search)
    public LinearLayout mLlSearch;

    @BindView(R.id.ll_sort_rank)
    public LinearLayout mLlSortRank;

    @BindView(R.id.ll_sort_price)
    public LinearLayout mLlSortPrice;

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
    private CurrenciesListViewModel mCurrenciesListViewModel;
    private CurrenciesPresenter mCurrenciesPresenter;
    private ProgressDialog mProgressDialog;
    private List<CoinPojo> mCoinPojoList;

    private boolean isVisibleSortLayout = false;
    private boolean isVisibleSearchLayout = false;
    private boolean isSortRankUp = false;
    private boolean isSortPriceUp = false;

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
        if (mCurrenciesPresenter == null) {
            mCurrenciesPresenter = new CurrenciesPresenter(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_all_currencies, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        setListeners();
        ((MainActivity) getActivity()).setToolbarTitle(this.getResources().getString(R.string.list));

        mCurrenciesListViewModel = ViewModelProviders.of(this).get(CurrenciesListViewModel.class);
        mCurrenciesListViewModel.getCoinsList().observe(this, coins -> {
                    mCoinPojoList = coins;
                    setList((ArrayList) coins);
                    Log.d(TAG, mCoinPojoList.toString());
                }
        );

        return view;
    }

    private void setUpRecyclerView() {
        mRvCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCurrencies.setHasFixedSize(true);
    }

    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mCurrenciesPresenter.getCurrenciesList());
        mIvCloseSort.setOnClickListener(v -> setSortLayoutVisibility());
        mIvCloseSearch.setOnClickListener(v -> setSearchLayoutVisibility());
        mLlSortRank.setOnClickListener(v -> {
            selectRank();
            mCurrenciesPresenter.sortListByRank((ArrayList<CoinPojo>) mCoinPojoList, isSortRankUp);
            isSortRankUp = !isSortRankUp;
        });

        mLlSortPrice.setOnClickListener(v -> {
            selectPrice();
            mCurrenciesPresenter.sortListByPrice((ArrayList<CoinPojo>) mCoinPojoList, isSortPriceUp);
            isSortPriceUp = !isSortPriceUp;
        });
        mLlSortCap.setOnClickListener(v -> selectCap());
        mLlSort1h.setOnClickListener(v -> select1h());
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
            mCoinsAdapter = new CoinsAdapter(list);
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
        DialogFactory.createGenericErrorDialog(getContext(), R.string.error_message);
    }

    @Override
    public void onBackPressed() {

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
        if (mCurrenciesPresenter != null) {
            mCurrenciesPresenter.unsubscribe();
        }
    }
}
