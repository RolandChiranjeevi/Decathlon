package org.example.utils;

public final class TestData {

    private TestData() {
    }

    /**
     * The app keeps competitors in memory and shares them between tests,
     * so every test that needs a competitor should use its own name.
     * The name has to contain a letter or the API rejects it.
     */
    public static String uniqueName(String prefix) {
        return prefix + System.nanoTime();
    }
}
