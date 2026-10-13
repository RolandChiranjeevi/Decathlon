package org.example.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

/** The "Standings" card: the results table and the CSV export button. */
public class StandingsSection extends BasePage {

    /** Columns by their header text in the table. Add a constant when a column is added. */
    public enum Column {
        NAME("Name"),
        RUN_100M("100m"),
        LONG_JUMP("Long Jump"),
        SHOT_PUT("Shot Put"),
        RUN_400M("400m"),
        TOTAL("Total");

        private final String header;

        Column(String header) {
            this.header = header;
        }

        public String header() {
            return header;
        }
    }

    private final Locator tableBody = byTestId("standingsTable");
    // The test id is on the tbody, so go up to the table to reach the header row.
    private final Locator headers = tableBody.locator("xpath=ancestor::table").locator("thead th");
    private final Locator exportButton = byTestId("exportCsvBtn");

    public StandingsSection(Page page) {
        super(page);
    }

    public Locator rows() {
        return tableBody.locator("tr");
    }

    /** The row whose name cell matches exactly, so "Anna" does not also match "Annabel". */
    public Locator rowFor(String name) {
        Locator nameCell = page.getByRole(
                AriaRole.CELL,
                new Page.GetByRoleOptions().setName(name).setExact(true));
        return rows().filter(new Locator.FilterOptions().setHas(nameCell));
    }

    public Locator cell(String name, Column column) {
        return cell(name, column.header());
    }

    /**
     * The column is found by its header text, not by position, so the tests keep
     * working if columns are added or moved. A missing header fails with a clear message.
     */
    public Locator cell(String name, String header) {
        return rowFor(name).locator("td").nth(columnIndex(header));
    }

    public Locator totalFor(String name) {
        return cell(name, Column.TOTAL);
    }

    /** Clicks export and returns the downloaded file. */
    public Download exportCsv() {
        return page.waitForDownload(exportButton::click);
    }

    private int columnIndex(String header) {
        headers.first().waitFor();
        List<String> texts = headers.allInnerTexts();
        for (int i = 0; i < texts.size(); i++) {
            if (texts.get(i).trim().equals(header)) {
                return i;
            }
        }
        throw new IllegalArgumentException(
                "No column '" + header + "' in the standings table, found: " + texts);
    }
}
