package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class ScoreEntryTest extends PlaywrightTestBase {

    @Test
    void addCompetitorAndSaveScore() {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            //.setHeadless(false)
            );

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("http://localhost:8080/");

            // Add competitor
            page.getByTestId("competitorNameInput").fill("Knatte");
            page.getByTestId("addCompetitorBtn").click();

            // Enter competitor name
            page.getByPlaceholder("same as above").fill("Knatte");

            // Enter score
            page.getByTestId("rawInput").fill("14");

            // Save score
            page.getByTestId("saveScoreBtn").click();

            // Assert that the score cell contains 312
            assertThat(
                    page.getByRole(
                            AriaRole.CELL,
                            new Page.GetByRoleOptions().setName("312")
                    ).nth(1)
            ).hasText("312");

            browser.close();
        }
    }


}
