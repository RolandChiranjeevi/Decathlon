package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaywrightTest {

    @Test
    void openPage() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();

            page.navigate("https://www.google.com");

            assertEquals("Google", page.title());

            browser.close();
        }
    }

    private void assertEquals(String google, String title) {
    }
}