package com.quanyan.pedometer.core;

public class Constants {
	public static final int MIL_SECONDS_DAY = 86400000;

	// Sampling Rate = 1000/UPTATE_INTERVAL_TIME
	public static final int UPTATE_INTERVAL_TIME = 20;

	//Order of low pass smoother
    public static final int SMOOTH_LENGTH = 5;

    // 10s largest interval between two steps, larger than this will be considered as new start
    public static final long THRESHOLD = 10000;

    //Dither resistance, continuous steps larger than this will be counted
    public static final int STEP_THRESHOLD = 5;
    
    public static final String ACTION_UPLOAD_STEP = "zxp_custom_action_upload_step_data";
    public static final String ACTION_SAVE_STEP_PER_HOUR = "zxp_custom_action_save_step_per_hour";
    public static final String ACTION_CLEAR_DATA = "zxp_custom_action_clear_data";
    public static final String ACTION_UPLOAD_STEP_DATA_OK = "zxp_custom_action_upload_step_data_ok";
    public static final String ACTION_UPLOAD_STEP_DATA_KO = "zxp_custom_action_upload_step_data_ko";
    public static final String ACTION_OVER_DAY = "zxp_custom_action_over_day";

	public static final int SAMPLES_IN_SECONDE = 1000 / UPTATE_INTERVAL_TIME;
    
	public static final String SP_SETTING_NAME = "zxp_custom_sp_setting_flag";
    public static final String SP_SETTING_ITEM_KEY = "zxp_custom_sp_setting_item_key";
    public static final String SP_SETTING_TARGET = "zxp_custom_sp_setting_pedometer_target";
}