package de.ironcoding.fitsim.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.ironcoding.fitsim.app.FitSimApp;
import de.ironcoding.fitsim.app.injection.DbRepositoryModule;
import de.ironcoding.fitsim.app.injection.FirebaseModule;
import de.ironcoding.fitsim.app.widget.HighscoreWidgetProvider;
import de.ironcoding.fitsim.firebase.model.UserHighscore;
import de.ironcoding.fitsim.logic.Athlete;
import de.ironcoding.fitsim.logic.IHighscore;
import de.ironcoding.fitsim.repository.AthleteRepository;
import de.ironcoding.fitsim.repository.HighscoreRepository;
import de.ironcoding.fitsim.util.HighscoreUtil;
import timber.log.Timber;

/**
 * Created by larsl on 01.05.2017.
 */

public class JobScheduledIntentService extends IntentService {

    public static final String ACTION_JOB_SCHEDULED = "de.ironcoding.action.job_scheduled";

    @Inject
    @Named(DbRepositoryModule.REPOSITORY_DB)
    AthleteRepository athleteRepository;

    @Inject
    @Named(DbRepositoryModule.REPOSITORY_DB)
    HighscoreRepository highscoreRepository;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    @Named(FirebaseModule.CHILD_HIGHSCORE)
    DatabaseReference highscoreDatabaseReference;

    public static Intent getIntent(Context context, @EventJobService.Event String event) {
        Intent intent = new Intent(context, JobScheduledIntentService.class);
        intent.putExtra(EventJobService.EXTRA_JOB_EVENT, event);
        return intent;
    }

    public JobScheduledIntentService() {
        super(JobScheduledIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((FitSimApp)getApplicationContext()).getAppComponent().injectJobScheduledService(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        @EventJobService.Event String event = intent.getExtras().getString(EventJobService.EXTRA_JOB_EVENT);
        if (intent.getExtras() == null) {
            Timber.d("onHandleIntent: no extras!");
            return;
        }
        if (TextUtils.isEmpty(event)) {
            Timber.d("onHandleIntent: event was empty!");
            return;
        }
        Timber.d("onHandleIntent - Event: " + event);
        switch (event) {
            case EventJobService.EVENT_REFRESH_BODY:
                refreshBody();
                sendChangedBroadcast(event);
                break;
            case EventJobService.EVENT_RELAXE_MUSCLES:
                relaxMuscles();
                sendChangedBroadcast(event);
                break;
            case EventJobService.EVENT_ATHLETE_READY:
                athleteReady();
                sendChangedBroadcast(event);
                break;
            case EventJobService.EVENT_ATHLETE_DIGESTED:
                athleteDigested();
                sendChangedBroadcast(event);
                break;
            case EventJobService.EVENT_UPDATE_HIGHSCORE:
                updateHighscore();
                sendHighscoreChangedBroadcast();
                break;
        }
    }
    private void refreshBody() {
        Athlete athlete = athleteRepository.loadAthlete();
        athlete.refreshBody();
        athleteRepository.updateAthlete(athlete);
    }

    private void relaxMuscles() {
        Athlete athlete = athleteRepository.loadAthlete();
        athlete.relaxMuscles();
        athleteRepository.updateAthlete(athlete);
    }

    private void athleteReady() {
        Athlete athlete = athleteRepository.loadAthlete();
        athlete.setReady();
        athleteRepository.updateAthlete(athlete);
    }

    private void athleteDigested() {
        Athlete athlete = athleteRepository.loadAthlete();
        athlete.goToRestRoom();
        athleteRepository.updateAthlete(athlete);
    }

    private void updateHighscore() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            HighscoreUtil.topTenQuery(highscoreDatabaseReference).addListenerForSingleValueEvent(new DataChangedValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<IHighscore> highscores = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserHighscore userHighscore = snapshot.getValue(UserHighscore.class);
                        if (userHighscore != null) {
                            highscores.add(userHighscore);
                        }
                    }
                    highscoreRepository.replaceHighscores(highscores);
                }
            });
        }
    }

    private void sendChangedBroadcast(@EventJobService.Event String event) {
        Intent intent = new Intent(ACTION_JOB_SCHEDULED).setPackage(getPackageName());
        intent.putExtra(EventJobService.EXTRA_JOB_EVENT, event);
        sendBroadcast(intent);
    }

    private void sendHighscoreChangedBroadcast() {
        Intent intent = new Intent(HighscoreWidgetProvider.ACTION_UPDATE).setPackage(getPackageName());
        sendBroadcast(intent);
    }

    private abstract class DataChangedValueEventListener implements ValueEventListener {
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.d("onCancelled: %s", databaseError.getMessage());
        }
    }
}
