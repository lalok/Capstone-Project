package de.ironcoding.fitsim.logic;

/**
 * Created by larsl on 12.04.2017.
 */

public class Level {

    public static final int START_LEVEL = 1;

    public static final float RELATIVE_EXP_RAISE = 0.1F;

    public static final int START_EXPERIENCE = 500;

    private int value = START_LEVEL;

    private long maxExperience = START_EXPERIENCE;

    private long reachedExperience = 0;

    private long totalExperience = 0;

    private Skill skill;

    Level() {}

    public static Level create(long experience) {
        Level level = new Level();
        level.skill = Skill.achieve(level.getValue());
        level.gainExperience(experience);
        level.skill = Skill.achieve(level.getValue());
        return level;
    }

    /**
     * Adds the gained experience to reached. When maxExperience is reached, next level is reached.
     *
     * @param experience
     *                      gained experience
     */
    public void gainExperience(long experience) {
        if (experience < 0) {
            experience = 0;
        }
        reachedExperience += experience;
        while (reachedExperience >= maxExperience) {
            levelUp();
        }
        totalExperience += experience;
    }

    private void levelUp() {
        reachedExperience -= maxExperience;
        // every new level needs 0.1 percent of maxExperience but difference is rounded to 10
        // e.g. 550 current max -> 550 * 0.1 = 55 -> round: 60 -> next level max = 550 + 60 = 610
        maxExperience  += (((int) (maxExperience * RELATIVE_EXP_RAISE) + 9) / 10) * 10;
        value++;
        skill.improveForLevel(value);
    }

    public int getValue() {
        return value;
    }

    public long getMaxExperience() {
        return maxExperience;
    }

    public long getReachedExperience() {
        return reachedExperience;
    }

    public long getTotalExperience() {
        return totalExperience;
    }

    public Skill getSkill() {
        return skill.copy();
    }

    public Level copy() {
        Level level = new Level();
        level.value = value;
        level.maxExperience = maxExperience;
        level.reachedExperience = reachedExperience;
        level.totalExperience = totalExperience;
        level.skill = getSkill();
        return level;
    }
}
