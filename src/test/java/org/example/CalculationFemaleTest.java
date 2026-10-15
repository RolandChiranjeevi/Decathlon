package org.example;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CalculationFemaleTest extends PlaywrightTestBase {

        private final String femaleName = "Carolina Klüft";
        @ParameterizedTest(name = "{0}: {1} ska ge {2} poäng")
        @CsvSource({
                "hepHighJump, 195, 1171",
                "hepLongJump, 685, 1122",
                "hep800m, 132.56, 927",
                "hep100mHurdles, 13.15, 1102",
                "hepShotPut, 14.81, 848",
                "hep200m, 23.38, 1041",
                "hepJavelinThrow, 47.98, 821",

        })
        void calculatesCorrectPoints(String event, String rawScore, String expectedPoints) {
            page.navigate("http://localhost:8080/");

            addCompetitor(femaleName);
            enterResult(event, rawScore);

            assertThat(page.getByText("Saved: " + expectedPoints + " pts"))
                    .isVisible();
        }

        private void addCompetitor(String name) {
            page.getByTestId("competitorNameInput").fill(name);
            page.getByTestId("addCompetitorBtn").click();
        }

        private void enterResult(String event, String rawScore) {
            page.getByPlaceholder("same as above").fill(femaleName);
            page.getByTestId("disciplineSelect").selectOption("heptathlon");
            page.getByTestId("eventSelect").selectOption(event);
            page.getByTestId("rawInput").fill(rawScore);
            page.getByTestId("saveScoreBtn").click();
        }

    @Test
    void calculatesTotalForAllEvents() {
        page.navigate("http://localhost:8080/");
        addCompetitor(femaleName);

        enterResult("hepHighJump", "195");
        enterResult("hepLongJump", "685");
        enterResult("hep800m", "132.56");
        enterResult("hep100mHurdles", "13.15");
        enterResult("hepShotPut", "14.81");
        enterResult("hep200m", "23.38");
        enterResult("hepJavelinThrow", "47.98");


        Locator row = page.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText(femaleName));

        assertThat(row).containsText("7032");
    }

    /*@ParameterizedTest(name = "{0}: {1} ska ge {2} poäng")
    @CsvSource({
            "hepHighJump, 301, 0",
            "hepLongJump, 1001, 0",
            "hep800m, 251, 0",
            "hep100mHurdles, 31, 0",
            "hepShotPut, 31, 0",
            "hep200m, 101, 0",
            "hepJavelinThrow, 111, 0",

    })
    void calculatesBoundaries(String event, String rawScore, String expectedPoints) {
        page.navigate("http://localhost:8080/");

        addCompetitor(femaleName);
        enterResult("hepHighJump", "301");
        enterResult("hepLongJump", "1001");
        enterResult("hep800m", "251");
        enterResult("hep100mHurdles", "31");
        enterResult("hepShotPut", "31");
        enterResult("hep200m", "101");
        enterResult("hepJavelinThrow", "111");

        assertThat(page.getByText("Saved: " + expectedPoints + " pts"))
                .isVisible();

    }*/
    }


