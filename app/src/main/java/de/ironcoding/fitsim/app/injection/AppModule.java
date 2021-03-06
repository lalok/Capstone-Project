package de.ironcoding.fitsim.app.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.ironcoding.fitsim.app.FitSimApp;
import de.ironcoding.fitsim.util.AppSettings;
import de.ironcoding.fitsim.util.Jobber;

/**
 * Created by larsl on 20.04.2017.
 */
@Module
public class AppModule {

    private final FitSimApp application;

    public AppModule(FitSimApp application) {
        this.application = application;
    }

    @Provides
    Context provideAppContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    AppSettings provideAppSettings(SharedPreferences sharedPreferences, Context context) {
        return new AppSettings(sharedPreferences, context);
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Provides
    Locale providesLocale() {
        return Locale.getDefault();
    }

    @Provides
    GooglePlayDriver providesGooglePlayDriver(Context context) {
        return new GooglePlayDriver(context);
    }

    @Provides
    @Singleton
    FirebaseJobDispatcher providesJobDispatcher(GooglePlayDriver driver) {
        return new FirebaseJobDispatcher(driver);
    }

    @Provides
    @Singleton
    Jobber providesJobber(FirebaseJobDispatcher dispatcher, AppSettings appSettings) {
        return new Jobber(dispatcher, appSettings);
    }
}
