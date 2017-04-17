package de.ironcoding.fitsim.logic;

/**
 * Created by larsl on 12.04.2017.
 */

public class Exercise extends Activity {

    private final int strainedMuscle;

    public Exercise(String name, float pal, int effort, int experience, int strainedMuscle) {
        super(name, pal, effort, experience);
        this.strainedMuscle = strainedMuscle;
    }

    @Override
    public void perform(Body.Fitness fitness, BodyType bodyType) {
        fitness.improveStrength(bodyType.getBuildUp());
        Muscles.get().strain(strainedMuscle, getAttraction(bodyType.getBuildUp()));
    }

    @Override
    public boolean isToDemanding() {
        return !Muscles.get().isDurable(strainedMuscle);
    }
}
