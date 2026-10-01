package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HomePageTest {

    @Test
    void openLocalApplication() {

        try (com.microsoft.playwright.Playwright playwright =
                     com.microsoft.playwright.Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
            );

            Page page = browser.newPage();

            page.navigate("http://localhost:8080");

            System.out.println("Page title: " + page.title());

            assertTrue(page.title().length() > 0);

            browser.close();
        }
    }

}