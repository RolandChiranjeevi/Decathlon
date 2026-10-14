package org.example;

import org.example.data.Event;
import org.example.pages.HomePage;
import org.example.pages.StandingsSection.Column;
import org.example.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Basic checks
 * only prove the locators and actions work
 * Run them first if the locators ever seem to be broken.
 */


class HomePageSmokeTest extends PlaywrightTestBase {

    private HomePage home;

    @BeforeEach
    void openApp() {
        home = new HomePage(page).open();
    }

    @Test
    void addedCompetitorShowsUpInStandings() {
        String name = TestData.uniqueName("Smoke");

        home.competitors().add(name);

        assertThat(home.standings().rowFor(name)).isVisible();
        assertThat(home.standings().totalFor(name)).hasText("0");
    }

    @Test
    void savedResultShowsUpInStandings() {
        String name = TestData.uniqueName("Runner");
        home.competitors().add(name);

        home.results().save(name, Event.RUN_100M, "14");

        assertThat(home.results().message()).containsText("Saved");
        assertThat(home.standings().cell(name, Column.RUN_100M)).not().isEmpty();
    }
}
