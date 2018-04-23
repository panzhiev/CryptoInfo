package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MarketPrice> mArrayList;

    public MarketsAdapter(ArrayList<MarketPrice> list) {
        mArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configureViewHolder(holder, position);
    }

    public void reloadList(ArrayList<MarketPrice> list) {
        mArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_market_name)
        TextView mTvName;

        @BindView(R.id.tv_market_price)
        TextView mTvPrice;

        @BindView(R.id.tv_market_change_percentage)
        TextView mTvPercentage;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private void configureViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        Context context = viewHolder.mTvPercentage.getContext();
        final MarketPrice marketPrice = mArrayList.get(position);

        String name = marketPrice.getName();
        String lastPrice = Utils.formatPrice(String.valueOf(marketPrice.getPrice().getLast()));
        Double percentageChange = marketPrice.getPrice().getChange().getPercentage();
        String percentageString = Utils.formatPercentChangeForMarkets(percentageChange);
        if (percentageString.contains("-")) {
            viewHolder.mTvPercentage.setBackground(context.getDrawable(R.drawable.gradient_market_item_red));
        }
        viewHolder.mTvName.setText(name);
        viewHolder.mTvPrice.setText(lastPrice);
        viewHolder.mTvPercentage.setText(percentageString);
    }
}
