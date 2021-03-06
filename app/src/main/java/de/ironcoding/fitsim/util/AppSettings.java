package de.ironcoding.fitsim.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by larsl on 25.04.2017.
 */

public class AppSettings {

    private SharedPreferences sharedPreferences;

    private Context context;

    public AppSettings(SharedPreferences sharedPreferences, Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    private static final String PREF_ATHLETE_ID = "de.ironcoding.pref.athlete_id";
    private static final String PREF_REFRESH_BODY_SCHEDULED = "de.ironcoding.pref.refresh_body_scheduled";
    private static final String PREF_RELAX_MUSCLES_SCHEDULED = "de.ironcoding.pref.relax_muscles_scheduled";

    /**
     * Receiceives the stored athlete id from preferences.
     *
     * @return
     *                  Id of athlete which is stored in DB. Returns -1 when no athlete is stored in
     *                  DB.
     */
    public long getAthleteIdFromSettings() {
        return sharedPreferences.getLong(PREF_ATHLETE_ID, -1);
    }

    /**
     * Writehs the athletes id to preferences. Should only be called when there has benn an athlete
     * stored in DB!
     * @param athleteId
     *                      related id of the athlete which is stored in DB.
     */
    public void writeAthleteIdToSettings(long athleteId) {
        sharedPreferences.edit()
                .putLong(PREF_ATHLETE_ID, athleteId)
                .apply();
    }

    public void writeRefreshJobScheduledToSettings(boolean isScheduled) {
        sharedPreferences.edit()
                .putBoolean(PREF_REFRESH_BODY_SCHEDULED, isScheduled)
                .apply();
    }

    public boolean getRefreshJobScheduledFromSettings() {
        return sharedPreferences.getBoolean(PREF_REFRESH_BODY_SCHEDULED, false);
    }

    public void writeRelaxMusclesJobScheduledToSettings(boolean isScheduled) {
        sharedPreferences.edit()
                .putBoolean(PREF_RELAX_MUSCLES_SCHEDULED, isScheduled)
                .apply();
    }

    public boolean getRelaxMusclesJobScheduledFromSettings() {
        return sharedPreferences.getBoolean(PREF_RELAX_MUSCLES_SCHEDULED, false);
    }

}
