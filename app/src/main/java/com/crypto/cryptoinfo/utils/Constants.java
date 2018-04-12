package com.crypto.cryptoinfo.utils;

public class Constants {

    //view types
    public static final int COIN_DEFAULT_VIEW_TYPE = 0;
    public static final int COIN_FAV_SETTINGS_VIEW_TYPE = 1;

    //timestamps for charts in sec
    public static final long SIX_HOURS = 21600;
    public static final long ONE_DAY = 86400;
    public static final long SEVEN_DAYS = 604800;
    public static final long ONE_MONTH = 2629743;
    public static final long SIX_MONTHS = 15778458;
    public static final long ONE_YEAR = 31556926;

    //keys for intent of CoinInfoActivity
    public static final String COIN = "COIN";

    //SharedPrefs Keys
    public static final String ENABLE_NIGHT_MODE = "enable_night_mode";
    public static final String ENABLE_AUTO_NIGHT_MODE = "enable_auto_night_mode";
    public static final String MAIN_SCREEN = "pref_key_main_screen_list";
    public static final String LAST_UPD_ALL_COINS = "LAST_UPD_ALL_COINS";

    //other
    public static final long TIME_TO_UPD = 180000;

    public static final String CHART_IMAGE_URL = "https://s2.coinmarketcap.com/generated/sparklines/web/7d/usd/";
    public static final String COIN_IMAGE_URL_128x128 = "https://s2.coinmarketcap.com/static/img/coins/128x128/";
}
