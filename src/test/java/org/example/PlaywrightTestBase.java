package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class PlaywrightTestBase {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    void setUpPlaywright() {
        playwright = Playwright.create();

        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault("PLAYWRIGHT_HEADLESS", "false")
        );

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(headless)
        );

        page = browser.newPage();
    }

    @AfterEach
    void tearDownPlaywright() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}