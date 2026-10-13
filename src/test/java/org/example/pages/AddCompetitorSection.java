package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** The "Add competitor" card, including the shared error banner. */
public class AddCompetitorSection extends BasePage {

    private final Locator nameInput = byTestId("competitorNameInput");
    private final Locator addButton = byTestId("addCompetitorBtn");
    private final Locator errorBanner = byTestId("errorBanner");

    public AddCompetitorSection(Page page) {
        super(page);
    }

    public void add(String name) {
        nameInput.fill(name);
        addButton.click();
    }

    public Locator nameInput() {
        return nameInput;
    }

    public Locator errorBanner() {
        return errorBanner;
    }
}
