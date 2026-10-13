package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Shared parent for all page objects. Playwright already waits for
 * elements to be actionable, so this only holds what every page needs.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected Locator byTestId(String testId) {
        return page.getByTestId(testId);
    }

    protected Locator byId(String id) {
        return page.locator("#" + id);
    }
}
