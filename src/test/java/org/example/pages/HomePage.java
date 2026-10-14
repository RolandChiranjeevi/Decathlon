package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.utils.Config;

/**
 * The web UI is a single page, so this is the entry point. The three cards
 * on the page each have their own section class to keep the locators apart.
 */
public class HomePage extends BasePage {

    private final AddCompetitorSection addCompetitor;
    private final ResultEntrySection resultEntry;
    private final StandingsSection standings;

    public HomePage(Page page) {
        super(page);
        this.addCompetitor = new AddCompetitorSection(page);
        this.resultEntry = new ResultEntrySection(page);
        this.standings = new StandingsSection(page);
    }

    public HomePage open() {
        page.navigate(Config.baseUrl());
        return this;
    }

    public AddCompetitorSection competitors() {
        return addCompetitor;
    }

    public ResultEntrySection results() {
        return resultEntry;
    }

    public StandingsSection standings() {
        return standings;
    }
}
