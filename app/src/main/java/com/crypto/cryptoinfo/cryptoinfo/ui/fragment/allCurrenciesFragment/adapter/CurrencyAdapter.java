package com.crypto.cryptoinfo.cryptoinfo.ui.fragment.allCurrenciesFragment.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crypto.cryptoinfo.cryptoinfo.R;
import com.crypto.cryptoinfo.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private ArrayList<CoinPojo> mArrayList = new ArrayList<>();

    public CurrencyAdapter(ArrayList<CoinPojo> list) {
        mArrayList = list;
    }

    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyAdapter.ViewHolder holder, int position) {

        final CoinPojo coinPojo = mArrayList.get(position);

        holder.mTextViewSymbol.setText(coinPojo.getSymbol());
        holder.mTextViewCurrencyName.setText(coinPojo.getName());
        holder.mTextViewPrice.setText(coinPojo.getPriceUsd());
        holder.mTextViewCoinmarketCap.setText(coinPojo.getMarketCapUsd());

        String percentChange1h = coinPojo.getPercentChange1h();
        String percentChange24h = coinPojo.getPercentChange24h();
        String percentChange7d = coinPojo.getPercentChange7d();

        holder.mTextView24h.setText(coinPojo.getPercentChange24h());
        holder.mTextView7d.setText(coinPojo.getPercentChange7d());

        Context context = holder.mTextView1h.getContext();

        if (percentChange1h != null && percentChange1h.contains("-")) {
            holder.mTextView1h.setText(percentChange1h);
            holder.mTextView1h.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.mTextView1h.setText("+" + percentChange1h);
            holder.mTextView1h.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (percentChange24h != null && percentChange24h.contains("-")) {
            holder.mTextView24h.setText(percentChange24h);
            holder.mTextView24h.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.mTextView24h.setText("+" + percentChange24h);
            holder.mTextView24h.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (percentChange7d != null && percentChange7d.contains("-")) {
            holder.mTextView7d.setText(percentChange7d);
            holder.mTextView7d.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.mTextView7d.setText("+" + percentChange7d);
            holder.mTextView7d.setTextColor(context.getResources().getColor(R.color.green));
        }
    }

    public void reloadList(ArrayList<CoinPojo> list){
        mArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_symbol_currency)
        TextView mTextViewSymbol;

        @BindView(R.id.tv_name_currency)
        TextView mTextViewCurrencyName;

        @BindView(R.id.tv_price_currency)
        TextView mTextViewPrice;

        @BindView(R.id.tv_market_cap)
        TextView mTextViewCoinmarketCap;

        @BindView(R.id.tv_percent_change_1h)
        TextView mTextView1h;

        @BindView(R.id.tv_percent_change_24h)
        TextView mTextView24h;

        @BindView(R.id.tv_percent_change_7d)
        TextView mTextView7d;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
