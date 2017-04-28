package de.ironcoding.fitsim.ui.presenter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.ironcoding.fitsim.app.injection.MockRepositoryModule;
import de.ironcoding.fitsim.events.ActivitySelectedEvent;
import de.ironcoding.fitsim.logic.Activity;
import de.ironcoding.fitsim.logic.Athlete;
import de.ironcoding.fitsim.repository.ActivitiesRepository;
import de.ironcoding.fitsim.ui.model.ActivityRecyclerItem;
import de.ironcoding.fitsim.ui.model.AthleteActivityPreviewViewModel;

/**
 * Created by larsl on 28.04.2017.
 */

public class GymPresenter extends BasePresenter implements ActivitySelectedEvent {

    @Inject
    @Named(MockRepositoryModule.REPOSITORY_MOCKED)
    ActivitiesRepository activitiesRepository;

    public ObservableList<ActivityRecyclerItem> activities = new ObservableArrayList<>();

    public ObservableField<AthleteActivityPreviewViewModel> athletePreview = new ObservableField<>();

    public ObservableField<ActivityRecyclerItem> selectedActivity = new ObservableField<>();

    public GymPresenter(Callback callback) {
        super(callback);
    }

    @Override
    protected void onPresenterCreated() {
        super.onPresenterCreated();
        getFitSimApp().getAppComponent().injectGymPresenter(this);
    }

    @Override
    protected void onAthleteLoaded() {
        super.onAthleteLoaded();
        doInBackground(1, () -> activitiesRepository.loadForLevel(getAthlete().getLevel()))
                .addOnSuccess(this::updateItems)
                .execute();
    }

    private void updateItems(List<Activity> items) {
        for (Activity item : items) {
            activities.add(new ActivityRecyclerItem(item));
        }
    }

    @Override
    public void onActivitySelected(Activity activity) {
        if (getContext() == null) {
            return;
        }
        selectedActivity.set(new ActivityRecyclerItem(activity));
        updateAthletePreview(getAthlete().copy());
        notifyCallbackShowBottomSheet();
    }

    private void updateAthletePreview(Athlete athlete) {
        athletePreview.set(new AthleteActivityPreviewViewModel(athlete));
    }
}