#  DuckTales Decathlon Web MVP

Welcome to the **DuckTales Decathlon Web MVP** repository.

This project contains the source code, testing assets, QA documentation, and requirements for our web-based Decathlon application.

---

##  Quick Navigation

*  **[Requirements & Test Scenarios](REQUIREMENTS.md)** — Functional requirements, test scenarios, and Gherkin definitions
*  **[Jira Project Board](https://ducktales.atlassian.net/jira/software/projects/SCRUM/boards/1)** — Sprint tasks, test execution, and bug tracking
*  **[GitHub Repository](https://github.com/RolandChiranjeevi/Decathlon)** — Source code and project history

---

#### 💻 Getting Started

Start by opening the links below:

* **[Local Application](http://localhost:8080)** — Open and test the Decathlon web application
* **[Requirements & Test Scenarios](REQUIREMENTS.md)** — Review the functional requirements and Gherkin test scenarios
* **[Jira Project Board](https://ducktales.atlassian.net/jira/software/projects/SCRUM/boards/1)** — View sprint tasks, test execution, and reported bugs
* **[GitHub Repository](https://github.com/RolandChiranjeevi/Decathlon)** — Browse the source code and project history

### Recommended Review Order

1. **Open the Live Application** and test the main functionality.
2. **Review the Requirements** to understand the expected behaviour.
3. **Check the Jira Board** for current tasks and reported bugs.
4. **Browse the GitHub Repository** for the implementation and project history.


---

## 📋 QA Strategy & Testing Methodology

Our testing process is designed to maintain traceability between **requirements, test execution, and defect tracking**.

### Black-Box Testing

We use the following black-box testing techniques:

* **Equivalence Partitioning (EP)** — tests representative groups of valid and invalid inputs.
* **Boundary Value Analysis (BVA)** — focuses on values at and around important boundaries, such as scoring thresholds and input limits.

These techniques help us identify edge cases while keeping test coverage focused and systematic.

### Test Case Design

Manual test scenarios are documented using **Gherkin (`Given / When / Then`)** syntax.

This provides a standardized format that makes test cases clear, readable, and repeatable.

For example:

```gherkin
Given a user has entered valid results
When the user completes the required events
Then the application should calculate the corresponding score
```

### Requirement Traceability

Test execution is mapped directly to the relevant functional requirements and Jira issues.

| Requirement   | Functionality                         | Jira Issue |
| ------------- | ------------------------------------- | ---------- |
| **Req 2.4.2** | Data Persistence & Session Resumption | `SCRUM-20` |
| **Req 2.4.3** | CSV Data Export                       | `SCRUM-21` |

This provides traceability from **requirement → test scenario → execution → defect → resolution**.

---

##  Jira Workflow & Bug Tracking

We use Jira to manage development tasks, testing activities, and defects.

###  Teacher Review

Critical bugs requiring review are collected in the **`Critical Bugs To Staffan`** column on the active Jira board.

To review a bug:

1. Open the [Jira Project Board](https://ducktales.atlassian.net/jira/software/projects/SCRUM/boards/1).
2. Navigate to **`Critical Bugs To Staffan`**.
3. Open the relevant bug ticket.
4. Review the available information, including:

    * Steps to reproduce
    * Expected result
    * Actual result
    * Evidence and screenshots
   

---

##  Code Review & Bug Fix Workflow

Reviewers can provide feedback or propose fixes through GitHub Pull Requests.

### 1. Create a Feature Branch

Create a new branch from `master`.

Examples:

```text
feedback/staffan-review

```

### 2. Make and Commit Changes

Implement the proposed changes and commit them to the branch.

### 3. Push the Branch

Push the branch to GitHub so it can be reviewed.

### 4. Open a Pull Request

Create a Pull Request targeting the `master` branch.

The Pull Request should:

* Describe the changes made
* Reference the relevant Jira issue
* Explain how the changes were tested
* Include screenshots or other evidence where appropriate

For example:

```text
SCRUM-21 — Fix CSV export functionality
```

### 5. Review & Merge

The team reviews the Pull Request and, once approved, merges the changes into `master`.

---

## 🔄 Development & QA Workflow

Our general workflow is:

```text
Requirements
     ↓
Test Scenarios
     ↓
Development
     ↓
Test Execution
     ↓
Bug Identified
     ↓
Jira Ticket
     ↓
Bug Fix
     ↓
Pull Request
     ↓
Code Review
     ↓
Retest
     ↓
Resolution
```

This helps ensure that requirements, implementation, testing, and defect resolution remain connected throughout the project.

---

For questions, feedback, bug reports, or code review, please use the project's GitHub and Jira workflows described above.

