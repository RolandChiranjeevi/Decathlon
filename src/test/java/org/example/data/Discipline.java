package org.example.data;

public enum Discipline {
    DECATHLON("decathlon"),
    HEPTATHLON("heptathlon");

    private final String value;

    Discipline(String value) {
        this.value = value;
    }

    /** The option value used by the discipline dropdown. */
    public String value() {
        return value;
    }
}
