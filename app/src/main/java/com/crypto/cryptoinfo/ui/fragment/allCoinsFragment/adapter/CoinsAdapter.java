package com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.ViewHolder> {

    private ArrayList<CoinPojo> mArrayList = new ArrayList<>();

    public CoinsAdapter(ArrayList<CoinPojo> list) {
        mArrayList = list;
    }

    @Override
    public CoinsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CoinsAdapter.ViewHolder holder, int position) {

        final CoinPojo coinPojo = mArrayList.get(position);

        holder.mTextViewSymbol.setText(coinPojo.getSymbol());
        holder.mTextViewCurrencyName.setText(coinPojo.getName());
        holder.mTextViewPrice.setText(coinPojo.getPriceUsd());
        holder.mTextViewCoinmarketCap.setText(coinPojo.getMarketCapUsd());

        String percentChange1h = coinPojo.getPercentChange1h();
        String percentChange24h = coinPojo.getPercentChange24h();
        String percentChange7d = coinPojo.getPercentChange7d();

        Context context = holder.mTextView1h.getContext();

        if (percentChange1h != null) {
            if (percentChange1h.contains("-")) {
                holder.mTextView1h.setText(percentChange1h);
                holder.mTextView1h.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.mTextView1h.setText("+" + percentChange1h);
                holder.mTextView1h.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            holder.mTextView1h.setText("-");
            holder.mTextView1h.setTextColor(context.getResources().getColor(R.color.colorTextDefault));
        }

        if (percentChange24h != null) {
            if (percentChange24h.contains("-")) {
                holder.mTextView24h.setText(percentChange24h);
                holder.mTextView24h.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.mTextView24h.setText("+" + percentChange24h);
                holder.mTextView24h.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            holder.mTextView24h.setText("-");
            holder.mTextView24h.setTextColor(context.getResources().getColor(R.color.colorTextDefault));
        }
        if (percentChange7d != null) {
            if (percentChange7d.contains("-")) {
                holder.mTextView7d.setText(percentChange7d);
                holder.mTextView7d.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.mTextView7d.setText("+" + percentChange7d);
                holder.mTextView7d.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            holder.mTextView7d.setText("-");
            holder.mTextView7d.setTextColor(context.getResources().getColor(R.color.colorTextDefault));
        }
    }

    public void reloadList(ArrayList<CoinPojo> list) {
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
