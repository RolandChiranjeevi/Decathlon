# Ducktales Decathlon Web MVP

Welcome to the Ducktales Decathlon Web MVP repository. This project contains the source code, automated/manual test execution assets, and QA documentation for the Decathlon web application.

---

## QA Strategy & Testing Methodology

Our testing process ensures strict alignment between requirement scope, test execution, and bug tracking. We envision to apply:

* **Black-Box Testing Techniques:** Equivalence Partitioning (EP) and Boundary Value Analysis (BVA) to test event score thresholds, edge-case calculations, and user input validation.
* **Test Case Design:** All manual test scenarios are written using Gherkin (`Given-When-Then`) for clear, standardized execution.
* **Requirement Traceability:** Test execution maps directly back to core user requirements:
    * **Req 2.4.2:** Data Persistence & Session Resumption (e.g., `SCRUM-20`)
    * **Req 2.4.3:** CSV Data Export functionality (`SCRUM-21`)

---

## Jira Workflow & Bug Tracking

We use Jira to manage sprint tasks, execute test runs, and track defects.

### **For Teacher Review (Staffan)**
All critical bugs requiring review are isolated on our active project board:

1. Open our Jira Board: **https://ducktales.atlassian.net/jira/software/projects/SCRUM/boards/1?filter=&groupBy=none**
2. Navigate to the column: **`Critical Bugs To Staffan`**
3. Click any bug ticket (e.g., `BUG-01`) to view:
    * **Steps to Reproduce**
    * **Expected vs. Actual Results**
    * **Evidence & Screenshots** (attached under the ticket details)