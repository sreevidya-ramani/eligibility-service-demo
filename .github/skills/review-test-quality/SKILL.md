---
name: review-test-quality
description: Assess whether tests would actually catch a wrong implementation. Use after generating or reviewing tests, or when asked whether a test suite is meaningful.
---
# Reviewing test quality

For each test, report: what behaviour it asserts, whether it would fail if the implementation were wrong, which assertion would catch it, and whether it tests behaviour or only that the code runs.

Then list:
- Tests that would pass even if the implementation did the wrong thing, and what each is missing.
- Business rules with no test covering them.
- Assertions that check only non-null, a type, or that no exception was thrown.
- Tests that mirror the implementation rather than the requirement.

Do not change any files. Report findings only.