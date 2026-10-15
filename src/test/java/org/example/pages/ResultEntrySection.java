package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.data.Discipline;
import org.example.data.Event;

/** The "Enter result" card. */
public class ResultEntrySection extends BasePage {

    // The name field and the status message have no data-testid in index.html,
    // so for now they are found by id. Switch to test ids if the devs add them.
    private final Locator nameInput = byId("name2");
    private final Locator message = byId("msg");
    private final Locator errorBanner = byTestId("errorBanner");

    private final Locator disciplineSelect = byTestId("disciplineSelect");
    private final Locator eventSelect = byTestId("eventSelect");
    private final Locator rawInput = byTestId("rawInput");
    private final Locator saveButton = byTestId("saveScoreBtn");

    public ResultEntrySection(Page page) {
        super(page);
    }

    /**
     * Saves a result. The discipline is picked from the event, so the caller
     * does not have to switch the dropdown first. The raw value is a String
     * so tests can also try empty or odd input.
     */
    public void save(String name, Event event, String raw) {
        nameInput.fill(name);
        selectDiscipline(event.discipline());
        eventSelect.selectOption(event.value());
        rawInput.fill(raw);
        saveButton.click();
    }

    public void selectDiscipline(Discipline discipline) {
        disciplineSelect.selectOption(discipline.value());
    }

    public Locator nameInput() {
        return nameInput;
    }

    public Locator eventSelect() {
        return eventSelect;
    }

    public Locator rawInput() {
        return rawInput;
    }

    public Locator message() {
        return message;
    }
    public Locator errorBanner() {
        return errorBanner;
    }
}
