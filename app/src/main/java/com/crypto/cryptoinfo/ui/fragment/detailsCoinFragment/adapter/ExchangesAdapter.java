package com.crypto.cryptoinfo.ui.fragment.detailsCoinFragment.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.ExchangePojo;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MarketPrice> mArrayList;

    public ExchangesAdapter(ArrayList<MarketPrice> list) {
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

        @BindView(R.id.tv_market_last_update)
        TextView mTvLastUpdate;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private void configureViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        final MarketPrice marketPrice = mArrayList.get(position);

        viewHolder.mTvName.setText(marketPrice.getName());
        viewHolder.mTvPrice.setText("$" + marketPrice.getPrice().getLast());

//        try {
//            Date date = new Date(Long.parseLong(exchangePojo.getLastUpdate()) * 1000L);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
//            sdf.setTimeZone(TimeZone.getDefault());
//            String formattedDate = sdf.format(date);
//            viewHolder.mTvLastUpdate.setText(formattedDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        viewHolder.mTvLastUpdate.setText("" + marketPrice.getPrice().getChange().getPercentage());

    }
}
