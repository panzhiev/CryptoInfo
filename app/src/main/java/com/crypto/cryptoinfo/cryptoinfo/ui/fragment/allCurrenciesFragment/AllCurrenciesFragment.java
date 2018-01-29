package com.crypto.cryptoinfo.cryptoinfo.ui.fragment.allCurrenciesFragment;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crypto.cryptoinfo.cryptoinfo.R;
import com.crypto.cryptoinfo.cryptoinfo.presenter.CurrenciesPresenter;
import com.crypto.cryptoinfo.cryptoinfo.ui.activity.MainActivity;
import com.crypto.cryptoinfo.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.cryptoinfo.ui.fragment.allCurrenciesFragment.adapter.CurrencyAdapter;
import com.crypto.cryptoinfo.cryptoinfo.ui.fragment.allCurrenciesFragment.viewModel.CurrenciesListViewModel;
import com.crypto.cryptoinfo.cryptoinfo.utils.DialogFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCurrenciesFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.rv_my_nvs)
    public RecyclerView mRvCurrencies;

    @BindView(R.id.delete_all)
    public Button mButton;

    @BindView(R.id.get_all)
    public Button mButton2;

    private CurrencyAdapter mCurrencyAdapter;
    private CurrenciesListViewModel mCurrenciesListViewModel;
    private CurrenciesPresenter mCurrenciesPresenter;
    private ProgressDialog mProgressDialog;

    public AllCurrenciesFragment() {
        // Required empty public constructor
    }

    public static AllCurrenciesFragment newInstance() {
        return new AllCurrenciesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        init(view);

        ((MainActivity) getActivity()).setToolbarTitle(this.getResources().getString(R.string.list));

//        mCurrenciesPresenter.getCurrenciesList();

        mCurrenciesListViewModel = ViewModelProviders.of(this).get(CurrenciesListViewModel.class);
        mCurrenciesListViewModel.getCoinsList().observe(this, coins -> {

//                    if (coins == null || coins.isEmpty()) {
//                        if (NetworkUtils.internetConnectionChecking(getContext())) {
//                            Log.d(TAG, "coins = null");
//
//                        } else {
//                            showError();
//                        }
//                    } else {
//                        Log.d(TAG, "coins != null");
//                        setList((ArrayList) coins);
//                    }

                    setList((ArrayList) coins);
                }
        );


        setListeners();

        return view;
    }

    private void init(View view) {
        mRvCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCurrencies.setHasFixedSize(true);
    }

    private void setListeners() {
        mButton.setOnClickListener(view -> {
            mCurrenciesListViewModel.deleteCurrenciesAsync();
        });

        mButton2.setOnClickListener(view -> {
            mCurrenciesPresenter.getCurrenciesList();
        });
    }

    @Override
    public void setList(ArrayList list) {
        Log.d(TAG, "setList started");
        if (mCurrencyAdapter == null) {
            Log.d(TAG, "mCurrencyAdapter == null");
            mCurrencyAdapter = new CurrencyAdapter(list);
            mRvCurrencies.setAdapter(mCurrencyAdapter);
        } else {
            Log.d(TAG, "mCurrencyAdapter != null");
            mCurrencyAdapter.reloadList(list);
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
    public void showProgressIndicator() {
        mProgressDialog = DialogFactory.createProgressDialog(getContext(), R.string.loading);
        mProgressDialog.show();
    }

    @Override
    public void hideProgressIndicator() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCurrenciesPresenter != null) {
            mCurrenciesPresenter.unsubscribe();
        }
    }
}
