package de.ironcoding.fitsim.repository;

import java.util.Collections;
import java.util.List;

import de.ironcoding.fitsim.logic.Activity;
import de.ironcoding.fitsim.logic.Level;
import de.ironcoding.fitsim.logic.Type;

/**
 * Created by larsl on 27.04.2017.
 */

public class ActivitiesRepository extends BaseDaoRepository<List<Activity>, IActivitiesDao> {
    public ActivitiesRepository(IActivitiesDao activitiesDao) {
        super(activitiesDao);
    }

    public List<Activity> loadAll() {
        if (dao == null) {
            return Collections.emptyList();
        }
        return dao.load();
    }

    public List<Activity> loadForLevel(Level level) {
        if (dao == null) {
            return Collections.emptyList();
        }
        return dao.loadForLevel(level);
    }

    public List<Activity> loadForTYpe(int typeId) {
        if (dao == null) {
            return Collections.emptyList();
        }
        return dao.loadForType(new Type(typeId, ""));
    }

    public List<Activity> loadForLevelAndType(Level level, int typeId) {
        if (dao == null) {
            return Collections.emptyList();
        }
        List<Activity> levelActivities = loadForLevel(level);
        return Filter.filterForType(levelActivities, new Type(typeId, ""));
    }

}
