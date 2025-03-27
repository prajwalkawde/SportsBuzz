package com.fantasy.team.prediction.util;

public class ApiConfig {

    public static final String BASE_URL = "https://swiftsoftix.com/crickgo/";
    public static final String GAMES_= BASE_URL+"api/";
    public static final String IMG = BASE_URL+"img/";
    private static final String URL1 = GAMES_+"api.php?";
    public static final int NATIVE_AD_AFTER = 5;

    public static String API_Forgot_Pass= URL1 + "forgot_pass";
    public static String TELEGRAM_BANNER      = null;
    public static String TELEGRAM_BANNER_URL  = null;
    public static String FLOATING_BUTTON_URL  = null;
    public static String FLOATING_BUTTON_ICON = null;
    public static boolean SHOW_META_ADS = false;

    public static String NATIVE_AD_ID       = "4606194766097399_4606254256091450";
    public static String BANNER_AD_ID       = "";
    public static String INTERSTITIAL_AD_ID = "";


   // SUBSCRIPTION
    public static boolean removeAds = false;
    public static boolean showCricketTeamData  = false;
    public static boolean showFootballTeamData = false;
    public static boolean showOtherGamesData   = false;


}
