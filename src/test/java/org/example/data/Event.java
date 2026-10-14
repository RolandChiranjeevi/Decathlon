package org.example.data;

/**
 * Every event in the event dropdown. The values match the option values in
 * app.js, so add a constant here when the app gets a new event.
 */
public enum Event {
    // Decathlon
    RUN_100M(Discipline.DECATHLON, "100m"),
    HURDLES_110M(Discipline.DECATHLON, "110mHurdles"),
    RUN_400M(Discipline.DECATHLON, "400m"),
    RUN_1500M(Discipline.DECATHLON, "1500m"),
    LONG_JUMP(Discipline.DECATHLON, "longJump"),
    HIGH_JUMP(Discipline.DECATHLON, "highJump"),
    POLE_VAULT(Discipline.DECATHLON, "poleVault"),
    SHOT_PUT(Discipline.DECATHLON, "shotPut"),
    DISCUS_THROW(Discipline.DECATHLON, "discusThrow"),
    JAVELIN_THROW(Discipline.DECATHLON, "javelinThrow"),

    // Heptathlon
    HEP_HURDLES_100M(Discipline.HEPTATHLON, "hep100mHurdles"),
    HEP_200M(Discipline.HEPTATHLON, "hep200m"),
    HEP_800M(Discipline.HEPTATHLON, "hep800m"),
    HEP_LONG_JUMP(Discipline.HEPTATHLON, "hepLongJump"),
    HEP_HIGH_JUMP(Discipline.HEPTATHLON, "hepHighJump"),
    HEP_SHOT_PUT(Discipline.HEPTATHLON, "hepShotPut"),
    HEP_JAVELIN_THROW(Discipline.HEPTATHLON, "hepJavelinThrow");

    private final Discipline discipline;
    private final String value;

    Event(Discipline discipline, String value) {
        this.discipline = discipline;
        this.value = value;
    }

    public Discipline discipline() {
        return discipline;
    }

    /** The option value used by the event dropdown. */
    public String value() {
        return value;
    }
}
