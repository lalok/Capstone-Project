package de.ironcoding.fitsim.repository.mock;

import java.util.ArrayList;
import java.util.List;

import de.ironcoding.fitsim.logic.Muscle;
import de.ironcoding.fitsim.repository.IDao;

/**
 * Created by larsl on 18.04.2017.
 */

public class MusclesMockDao implements IDao<List<Muscle>> {
    @Override
    public List<Muscle> load() {
        List<Muscle> muscles = new ArrayList<>();
        muscles.add(Muscle.build(1, "Chest", 100, 1));
        muscles.add(Muscle.build(2, "Back", 100, 1));
        muscles.add(Muscle.build(3, "Biceps", 75, 1));
        muscles.add(Muscle.build(4, "Triceps", 75, 1));
        muscles.add(Muscle.build(5, "Forearms", 70, 5));
        return muscles;
    }
}