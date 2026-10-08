package org.example;

import org.junit.jupiter.api.Test;

class PlaywrightTest extends PlaywrightTestBase {

    @Test
    void openPage() {
        page.navigate("https://www.google.com");

        System.out.println("Google " + page.title());
    }
}