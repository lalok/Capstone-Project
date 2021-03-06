package de.ironcoding.fitsim.app.widget;

import android.content.Context;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.ironcoding.fitsim.R;
import de.ironcoding.fitsim.app.FitSimApp;
import de.ironcoding.fitsim.app.injection.DbRepositoryModule;
import de.ironcoding.fitsim.logic.IHighscore;
import de.ironcoding.fitsim.repository.HighscoreRepository;
import de.ironcoding.fitsim.ui.activities.ProfileActivity;
import de.ironcoding.fitsim.ui.model.HighscoreListItemViewModel;

/**
 * Created by larsl on 03.05.2017.
 */

public class HighscoreWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    @Inject
    @Named(DbRepositoryModule.REPOSITORY_DB)
    HighscoreRepository highscoreRepository;

    private Context context;

    private List<HighscoreListItemViewModel> items;

    public HighscoreWidgetFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        items = new ArrayList<>();
        ((FitSimApp)context.getApplicationContext()).getAppComponent().injectHighscoreWidgetFactory(this);
    }

    @Override
    public void onDataSetChanged() {
        items.clear();
        final long identityToken = Binder.clearCallingIdentity();
        List<IHighscore> highscores = highscoreRepository.loadHighscore();
        for (int i = 0; i < highscores.size(); i++) {
            IHighscore highscore = highscores.get(i);
            items.add(new HighscoreListItemViewModel(highscore, i));
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        items = null;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || items == null || items.get(position) == null) {
            return null;
        }
        HighscoreListItemViewModel item = items.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_item);
        views.setTextViewText(R.id.title, item.getName());
        views.setTextViewText(R.id.subtitle, item.getPoints());
        views.setOnClickFillInIntent(R.id.highscore_list_item, ProfileActivity.getIntent(context));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.layout_info_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
