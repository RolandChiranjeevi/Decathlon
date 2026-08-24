Requirements for ”Allround scoring system”
1. Scope
1.1 The system provides multi-event athletics scoring for decathlon/heptathlon.
1.2 Two user interfaces are provided: a desktop UI and a Web UI. Both use the
same core logic.
2. Core Domain Requirements
2.1 Scoring
2.1.1 2.1.2 2.1.3 The system shall compute points per event using the 2001 IAAF
scoring rules.
Input results shall be validated against reasonable bounds before
scoring.
Rounding/point totals shall follow the IAAF convention (integer points).
2.2 Competitors
2.2.1 The system shall manage up to 40 competitors.
2.2.2 For each competitor, the system shall store name, per-event raw
results, per-event points, total points and result position.
2.2.3 Competitor data shall be editable (e.g., correct a result).
2.3 Standings
2.3.1 2.3.2 The system shall provide current standings at any time, showing per-
event points, total points and current result position.
Standings shall update when new or edited results are saved.
2.4 Save & Resume
2.4.1 The system shall allow saving the current state (competitors, results,
points, standings).
2.4.2 2.4.3 The system shall allow resuming from a previously saved state.
Saved state shall be UI-agnostic: a state saved in one UI can be
resumed in the other.
2.5 Export
2.5.1 2.5.2 The system shall export results to an Excel-compatible file containing:
competitor name, event results, points per event, total points and
result position.
The export filename shall be unique (e.g., include a timestamp) to
avoid overwriting previous files.
2.6 Architecture and Modularity
2.6.1 Scoring rules, validation, and data structures shall reside in a shared
core module independent of any UI framework.
2.6.2 Both UIs shall depend on the shared core and must not duplicate
scoring logic.
3. Interface Requirements (High-Level)
3.1 Common Capabilities
3.1.1 Both UIs shall support: adding competitors, entering results, viewing
standings, saving/resuming, and exporting results.
3.1.2 Given identical inputs, both UIs shall produce identical scores and
totals.
3.2 Desktop UI
3.2.1 Shall present clear feedback for invalid inputs and operation errors.
3.3 Web UI
3.3.1 3.3.2 Shall be accessible locally in a modern desktop browser.
Should be suitable for automated UI testing (e.g., stable
labels/structure), without prescribing specific selectors.
4. Data and Validation
4.1 The system shall define acceptable input ranges per event (time/distance
units as appropriate) and reject out-of-range values.
4.2 The system shall handle invalid or missing input with user-readable error
messages.
4.3 Units used for input and display shall be consistent across UIs.
5. Quality Attributes
5.1 Usability: Key tasks (add competitor, enter result, view standings, save,
export) shall be achievable with minimal steps.
5.2 Performance: Operations shall remain responsive with up to 40 competitors
and all supported events.
5.3 Reliability: Save/resume shall not corrupt data; failed loads shall provide a
recoverable error message.
6. Constraints and Out of Scope
6.1 Offline/local operation only; multi-user/network features are out of scope.
6.2 Detailed UI styling, specific control layouts, and test selector naming are out
of scope.
