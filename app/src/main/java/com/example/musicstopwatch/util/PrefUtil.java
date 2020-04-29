package com.example.musicstopwatch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.example.musicstopwatch.TimerState;

public class PrefUtil {

    public static double getTimerLength(Context context, boolean isFirstFragment){
        if(isFirstFragment)
            return 0.5;
        return 2;
    }

    private static final String PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.example.musicstopwatch.previous_timer_length";

    public static long getPreviousTimerLengthSeconds(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0);
    }

    public static void setPreviousTimerLengthSeconds(long seconds, Context context){
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds);
        editor.apply();
    }

    private static final String TIMER_STATE_ID = "com.example.musicstopwatch.timer_state";

    public static TimerState getTimerState(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int ordinal = preferences.getInt(TIMER_STATE_ID, 0);
        return TimerState.values()[ordinal];
    }

    public static void setTimerState(TimerState state, Context context){
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        int ordinal = state.ordinal();
        editor.putInt(TIMER_STATE_ID, ordinal);
        editor.apply();
    }

    private static final String SECONDS_REMAINING_ID = "com.example.musicstopwatch.seconds_remaining";

    public static long getSecondsRemaining(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(SECONDS_REMAINING_ID, 0);
    }

    public static void setSecondsRemaining(long seconds, Context context){
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(SECONDS_REMAINING_ID, seconds);
        editor.apply();
    }

}
