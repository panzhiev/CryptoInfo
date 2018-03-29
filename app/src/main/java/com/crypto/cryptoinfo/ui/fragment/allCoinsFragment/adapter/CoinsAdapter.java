package com.crypto.cryptoinfo.ui.fragment.allCoinsFragment.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinFavPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CoinPojo> mArrayList = new ArrayList<>();
    private ArrayList<CoinFavPojo> mCoinFavPojos = (ArrayList<CoinFavPojo>) App.dbInstance.getCoinFavDao().getAll();
    private ArrayList<String> mCoinFavString = new ArrayList<>();
    private OnCoinItemClickListener mOnCoinItemClickListener;

    private int viewType;

    public interface OnCoinItemClickListener {
        void onCoinItemClick(CoinPojo coinPojo);
    }

    public CoinsAdapter(ArrayList<CoinPojo> list, int viewType, OnCoinItemClickListener listener) {
        mArrayList = list;
        this.viewType = viewType;
        for (CoinFavPojo coinFavPojo : mCoinFavPojos) {
            mCoinFavString.add(coinFavPojo.getId());
        }
        mOnCoinItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case Constants.COIN_DEFAULT_VIEW_TYPE:
                View v1 = layoutInflater.inflate(R.layout.item_coin, parent, false);
                return new ViewHolderDefaultCoinItem(v1);
            case Constants.COIN_FAV_SETTINGS_VIEW_TYPE:
                View v2 = layoutInflater.inflate(R.layout.item_coin_fav_settings, parent, false);
                return new ViewHolderFavCoinSettingsItem(v2);
            default:
                View v = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                return new RecyclerViewSimpleTextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Constants.COIN_DEFAULT_VIEW_TYPE:
                ViewHolderDefaultCoinItem vh1 = (ViewHolderDefaultCoinItem) holder;
                configureViewHolderDefaultCoin(vh1, position);
                break;
            case Constants.COIN_FAV_SETTINGS_VIEW_TYPE:
                ViewHolderFavCoinSettingsItem vh2 = (ViewHolderFavCoinSettingsItem) holder;
                configureViewHolderFavCoin(vh2, holder.getAdapterPosition());
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                configureDefaultViewHolder(vh, position);
                break;
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

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    static class ViewHolderDefaultCoinItem extends RecyclerView.ViewHolder {

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

        @BindView(R.id.iv_crypto_icon)
        ImageView mImageViewIcon;

        @BindView(R.id.rl_item_coin)
        RelativeLayout mRlItemCoin;

        @BindView(R.id.iv_preview_chart)
        ImageView mIvChart;

        ViewHolderDefaultCoinItem(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    static class ViewHolderFavCoinSettingsItem extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_symbol_currency)
        TextView mTextViewSymbol;

        @BindView(R.id.tv_name_currency)
        TextView mTextViewCurrencyName;

        @BindView(R.id.iv_crypto_icon)
        ImageView mImageViewIcon;

        @BindView(R.id.checkbox_fav)
        CheckBox mCheckBox;

        public ViewHolderFavCoinSettingsItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewSimpleTextViewHolder(View v) {
            super(v);
        }
    }

    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {

    }

    private void configureViewHolderDefaultCoin(ViewHolderDefaultCoinItem holder, int position) {
        Context context = holder.mTextView1h.getContext();
        final CoinPojo coinPojo = mArrayList.get(position);

        String price = coinPojo.getPriceUsd();
        String formattingPrice;
        if (price != null && !price.isEmpty()) {
            formattingPrice = String.format(Locale.getDefault(), "%1$,.2f", Double.parseDouble(price))
                    + " " + context.getString(R.string.usd_symbol);
        } else {
            formattingPrice = "";
        }

        holder.mTextViewSymbol.setText(coinPojo.getSymbol());
        holder.mTextViewCurrencyName.setText(coinPojo.getName());
        holder.mTextViewPrice.setText(formattingPrice);

        String marketCap = coinPojo.getMarketCapUsd();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);

        if (marketCap != null && !marketCap.isEmpty()) {
            marketCap = df.format(Double.parseDouble(marketCap)) + " " + context.getString(R.string.usd_symbol);
        } else {
            marketCap = "";
        }

        holder.mTextViewCoinmarketCap.setText(marketCap);

        String percentChange1h = coinPojo.getPercentChange1h();
        String percentChange24h = coinPojo.getPercentChange24h();
        String percentChange7d = coinPojo.getPercentChange7d();

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

        Glide.with(context)
                .load(Utils.getChartImageUrl(coinPojo.getNumId()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mIvChart.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mIvChart.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.mIvChart);

        Bitmap bitmap = Utils.getBitmapFromCryptoIconsAssets(context, coinPojo.getSymbol().toLowerCase());
        if (bitmap != null) {
            holder.mImageViewIcon.setImageBitmap(bitmap);
        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(context.getResources().getColor(R.color.textDisabled))
                    .fontSize(35)
                    .endConfig()
                    .buildRoundRect(coinPojo.getSymbol().length() <= 3 ? coinPojo.getSymbol() : coinPojo.getSymbol().substring(0, 3), Color.LTGRAY, 64);
            holder.mImageViewIcon.setImageDrawable(drawable);
        }

        holder.mRlItemCoin.setOnClickListener(view -> mOnCoinItemClickListener.onCoinItemClick(coinPojo));
    }

    private void configureViewHolderFavCoin(ViewHolderFavCoinSettingsItem holder, int position) {

        Context context = holder.mTextViewSymbol.getContext();
        final CoinPojo coinPojo = mArrayList.get(position);

        holder.mTextViewSymbol.setText(coinPojo.getSymbol());
        holder.mTextViewCurrencyName.setText(coinPojo.getName());

        Bitmap bitmap = Utils.getBitmapFromCryptoIconsAssets(context, coinPojo.getSymbol().toLowerCase());
        if (bitmap != null) {
            holder.mImageViewIcon.setImageBitmap(bitmap);
        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(context.getResources().getColor(R.color.textDisabled))
                    .fontSize(35)
                    .endConfig()
                    .buildRoundRect(coinPojo.getSymbol().length() <= 3 ? coinPojo.getSymbol() : coinPojo.getSymbol().substring(0, 3), Color.LTGRAY, 64);
            holder.mImageViewIcon.setImageDrawable(drawable);
        }

        holder.mCheckBox.setChecked(mCoinFavString.contains(coinPojo.getId()));
        holder.mCheckBox.setOnClickListener(view -> {
//            coinPojo.setFavourite(!coinPojo.isFavourite());
//            holder.mCheckBox.setChecked(coinPojo.isFavourite());
//            App.dbInstance.getCoinDao().insertAll(coinPojo);
            if (!holder.mCheckBox.isChecked()) {
                holder.mCheckBox.setChecked(false);
                App.dbInstance.getCoinFavDao().deleteFav(coinPojo.getId());
            } else {
                holder.mCheckBox.setChecked(true);
                App.dbInstance.getCoinFavDao().insertAll(new CoinFavPojo(coinPojo.getId()));
            }
        });
    }
}
