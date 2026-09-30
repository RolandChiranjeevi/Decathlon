package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;



class PlaywrightTest {

    @Test
    void openPage() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();

            page.navigate("https://www.google.com");

            System.out.println("Google"+ page.title());

            browser.close();
        }
    }
}