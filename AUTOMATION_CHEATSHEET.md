# Automation cheat sheet

How to write Playwright tests for the Web UI with the page objects in this repo. Keep this file up to date when the structure changes.

## 1. The idea

- A **page object** is a Java class that knows how to find elements on the page and what you can do with them.
- A **test** only calls those methods and checks the result. Tests never contain selectors.
- If the UI changes, you fix one page object instead of every test.

Everything lives under `src/test/java/org/example/`:

| File | What it is |
|---|---|
| `PlaywrightTestBase.java` | Starts and closes the browser for each test and gives you the `page` field. |
| `pages/BasePage.java` | Parent of all page objects (`byTestId` and `byId` helpers). |
| `pages/HomePage.java` | Entry point. `open()` loads the app, then use `competitors()`, `results()` and `standings()`. |
| `pages/AddCompetitorSection.java` | The "Add competitor" card and the error banner. |
| `pages/ResultEntrySection.java` | The "Enter result" card and the status message. |
| `pages/StandingsSection.java` | The standings table and the CSV export. |
| `data/Event.java`, `data/Discipline.java` | Every event and discipline, so tests never type dropdown values as strings. |
| `utils/Config.java` | The base URL. Defaults to `http://localhost:8080/`, can be overridden with the `BASE_URL` environment variable. |
| `utils/TestData.java` | `uniqueName("Prefix")` gives competitor names that do not clash. |
| `HomePageSmokeTest.java` | A working example. Copy its structure. |

## 2. Write a new test (copy this)

```java
package org.example;

import org.example.data.Event;
import org.example.pages.HomePage;
import org.example.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class MyFeatureTest extends PlaywrightTestBase {

    private HomePage home;

    @BeforeEach
    void openApp() {
        home = new HomePage(page).open();
    }

    // TC-003
    @Test
    void savedResultShowsConfirmation() {
        String name = TestData.uniqueName("Anna");
        home.competitors().add(name);

        home.results().save(name, Event.RUN_100M, "14");

        assertThat(home.results().message()).containsText("Saved");
    }
}
```

Rules for the file:
- The name ends with `Test.java`, otherwise Maven will not run it.
- It extends `PlaywrightTestBase` and sits in the package `org.example`.
- The base class `@BeforeEach` runs first, so `page` is ready in your own `@BeforeEach`.
- Put the manual test case ID (for example `TC-003`) in a comment above the test so it can be traced back.

## 3. What you can call

**Competitors**
```java
home.competitors().add(name);
home.competitors().errorBanner();   // Locator
home.competitors().nameInput();     // Locator
```

**Results**
```java
home.results().save(name, Event.RUN_100M, "14");   // picks the discipline for you
home.results().selectDiscipline(Discipline.HEPTATHLON);
home.results().message();                           // Locator for "Saved: N pts" or an error
home.results().nameInput();  home.results().eventSelect();  home.results().rawInput();
```
The result is a `String` on purpose, so you can also try empty or odd input.

**Standings**
```java
home.standings().rowFor(name);
home.standings().cell(name, Column.RUN_100M);   // Column is StandingsSection.Column
home.standings().cell(name, "Long Jump");       // or by the header text
home.standings().totalFor(name);
home.standings().rows();
Download file = home.standings().exportCsv();
```

**Assertions** (they wait and retry for up to about 5 seconds, so never use `Thread.sleep`)
```java
assertThat(locator).isVisible();
assertThat(locator).hasText("312");          // the whole text must match
assertThat(locator).containsText("Saved");   // part of the text
assertThat(locator).not().isEmpty();
assertThat(locator).hasCount(3);
```

## 4. Add something new to the page objects

1. Find the element with Playwright Codegen (see README) or the browser DevTools.
2. Choose the locator in this order: `data-testid`, then role or label, then id, then a CSS class. Avoid XPath by position and `.nth()` on unrelated elements.
3. Add the locator as a field in the right section class and expose an action as a method, not a raw click:
   ```java
   private final Locator clearButton = byTestId("clearBtn");

   public void clear() {
       clearButton.click();
   }
   ```
4. If the element has no test id, add a comment saying so, and note it in Jira (SCRUM-30, stable ids and labels).
5. New event: add a constant to `Event`. New standings column: add a constant to `StandingsSection.Column`. New page: new class that extends `BasePage`.

## 5. More examples

These two are based on reading the app code. Run them and adjust before you rely on them.

Invalid name is rejected:
```java
@Test
void nameWithoutLettersIsRejected() {
    home.competitors().add("123");

    assertThat(home.competitors().errorBanner()).hasText("Name must contain valid letters");
}
```

Export gives a CSV file:
```java
@Test
void exportDownloadsCsv() {
    Download file = home.standings().exportCsv();

    assertTrue(file.suggestedFilename().endsWith(".csv"));
}
```
This one needs `import com.microsoft.playwright.Download;` and `import static org.junit.jupiter.api.Assertions.assertTrue;`.

## 6. Run the tests

1. Start the app: `mvn spring-boot:run`, or run the `WebUI` class in IntelliJ. Wait until http://localhost:8080 loads in your browser.
2. First time only: install the Playwright browsers. The command is in the README.
3. Run:
   - everything: `mvn test`
   - one class: `mvn -Dtest=MyFeatureTest test`
   - one test: `mvn "-Dtest=MyFeatureTest#savedResultShowsConfirmation" test` (the quotes matter in PowerShell)
   - or use the green arrow next to the test in IntelliJ.
4. A Chrome window opens by default, so you can watch the test. For a headless run (no window, the same way GitHub Actions runs it), set `PLAYWRIGHT_HEADLESS=true`:
   - Git Bash: `PLAYWRIGHT_HEADLESS=true mvn test`
   - PowerShell: `$env:PLAYWRIGHT_HEADLESS="true"; mvn test`
   - IntelliJ: Run, Edit Configurations, then add `PLAYWRIGHT_HEADLESS=true` under Environment variables.
5. Another address: set `BASE_URL`, for example `$env:BASE_URL="http://localhost:9090/"`.

## 7. Check that a test really ran

Passing tests are quiet and fast, and the browser window closes by itself, so it can feel like nothing happened.
- In the Maven output, look for `Tests run: N, Failures: 0, Errors: 0, Skipped: 0` and check that N includes your test.
- Maven also writes a report per test class to `target/surefire-reports/`.
- To watch a test step by step, run it in the browser and add `page.pause();` where you want it to stop. Remove it before you commit.
- In GitHub Actions, open the run, then the `test` job, then the **Run tests** step, and look for the same `Tests run:` line.
- A new test should fail once on purpose (for example change an expected value) so you know it is really being checked.

## 8. Rules of thumb

- One behaviour per test, and a name that reads like a sentence.
- Always use `TestData.uniqueName(...)`. The app keeps competitors in memory and shares them between tests.
- Do not depend on the order of tests.
- Selectors belong in page objects. Tests only call methods and use `assertThat` on the locators they get back.
- Keep tests short. Put repeated steps in a page object method.

## 9. Known limits

- Web UI only. The desktop UI is a Swing app and is not covered here.
- Competitors stay in memory until the app restarts, so a test of the 40 competitor limit is unreliable unless the app is restarted first.
- The standings table only shows 100m, Long Jump, Shot Put and 400m.
- The Web UI has no save or resume controls, only the CSV export.
- The result name field and the status message have no `data-testid`, so they are found by id (`#name2`, `#msg`).
- `ScoreEntryTest` and `PlaywrightTest` were written before the page objects and still use their own locators. New tests should use the page objects.

## 10. Git and review

1. Start from an up to date master: `git switch master`, `git pull`, then `git switch -c SCRUM-xx-short-name`.
2. Run the tests locally before you push.
3. Add files by name (not `git add -A`) and never add anything under `target/` or `.idea/`.
4. Push the branch and open a pull request into `master`. Mention the Jira issue, what you added and how you tested it.
