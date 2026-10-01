package org.example.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class PlaywrightTestBase {

    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;

    protected static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    static void setupPlaywright() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );
    }

    @AfterAll
    static void tearDownPlaywright() {
        browser.close();
        playwright.close();
    }

    protected void openApplication() {
        page = browser.newPage();
        page.navigate(BASE_URL);
    }
}
