package com.crypto.cryptoinfo.ui.fragment.coinChartsFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.ui.fragment.IBaseFragment;
import com.crypto.cryptoinfo.utils.DialogFactory;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class CoinChartsFragment extends Fragment implements IBaseFragment {

    private final String TAG = getClass().getSimpleName();

    public CoinChartsFragment() {
        // Required empty public constructor
    }

    public static CoinChartsFragment newInstance() {
        return new CoinChartsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_coin_charts, container, false);
        ButterKnife.bind(this, view);
        setListeners();

        return view;
    }

    private void setListeners() {
    }

    @Override
    public void setList(ArrayList list) {

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

                return true;
            case R.id.action_search:

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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mCoinsPresenter != null) {
//            mCoinsPresenter.unsubscribe();
//        }
//
//        if (!searchDisposable.isDisposed()) {
//            searchDisposable.dispose();
//        }
    }
}
