package org.example;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CalculationMaleTest extends PlaywrightTestBase {

    private final String maleName = "Henrik Dagård";
        @ParameterizedTest(name = "{0}: {1} ska ge {2} poäng")
        @CsvSource({
                "highJump, 207, 868",
                "longJump, 748, 930",
                "100m, 10.58, 956",
                "110mHurdles, 13.97, 978",
                "shotPut, 15.45, 817",
                "400m, 46.71, 973",
                "discusThrow, 45.64, 780",
                "poleVault, 510, 941",
                "javelinThrow, 69.26, 878",
                "1500m, 274.46, 716"
        })
        void calculatesCorrectPoints(String event, String rawScore, String expectedPoints) {
            page.navigate("http://localhost:8080/");

            addCompetitor(maleName);
            enterResult(event, rawScore);

            assertThat(page.getByText("Saved: " + expectedPoints + " pts"))
                    .isVisible();
        }

        private void addCompetitor(String name) {
            page.getByTestId("competitorNameInput").fill(name);
            page.getByTestId("addCompetitorBtn").click();
        }

        private void enterResult(String event, String rawScore) {
            page.getByPlaceholder("same as above").fill(maleName);
            page.getByTestId("eventSelect").selectOption(event);
            page.getByTestId("rawInput").fill(rawScore);
            page.getByTestId("saveScoreBtn").click();
        }

    @Test
    void calculatesTotalForAllEvents() {
        page.navigate("http://localhost:8080/");
        addCompetitor(maleName);

        enterResult("highJump", "207");
        enterResult("longJump", "748");
        enterResult("100m", "10.58");
        enterResult("110mHurdles", "13.97");
        enterResult("shotPut", "15.45");
        enterResult("400m", "46.71");
        enterResult("discusThrow", "45.64");
        enterResult("poleVault", "510");
        enterResult("javelinThrow", "69.26");
        enterResult("1500m", "274.46");

        Locator row = page.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText(maleName));

        assertThat(row).containsText("8837");
    }
    }


