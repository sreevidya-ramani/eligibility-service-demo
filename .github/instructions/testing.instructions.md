---
description: 'Use these guidelines when generating or updating tests.'
applyTo: '**/src/test/**'
---
# Testing guidelines

## Conventions
* Spring Boot 4 and JUnit Jupiter. Never use JUnit 4.
* Mock beans with @MockitoBean. @MockBean was removed in Spring Boot 4.
* Import @WebMvcTest from org.springframework.boot.webmvc.test.autoconfigure.
* Follow the structure and style of the existing tests in this module.
* One behaviour per test. Name each test after the behaviour and its expected outcome.
* Keep tests independent. No shared state and no ordering assumptions.

## What makes a test meaningful
* Tests must fail for the right reason. Verify they catch the bug they are meant to catch.
* Assert exact values. Never assert only non-null, a type, or that no exception was thrown.
* Derive each test from a stated requirement or business rule, not from reading the implementation.
* Where a rule has a threshold, test the value at the threshold and one step either side.
* In the withdrawal service, mock EligibilityClient at the service boundary.
* In the eligibility service, use the in-memory participant data rather than mocking the repository.
* Never mock the class under test.
* Keep test methods short and focused, ideally under 20 lines.
* Use setup methods (@BeforeEach) sparingly and only for common initialization code.
* Prefer parameterized tests for scenarios that need to be tested with multiple input values.
* Start with the simplest case, then add edge cases and error conditions.