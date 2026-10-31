---
name: extract-business-rules
description: Extract the business rules implemented in a method or class, with file and line evidence. Use when asked what rules a piece of code enforces, or before writing tests for untested logic.
---
# Extracting business rules

Produce a numbered table with these columns: rule, the condition that triggers it, the outcome, the file and line, and whether it is confirmed in executable code or inferred from naming or comments.

Rules:
- Read the code. Never infer a rule from a method name, a comment or a variable name alone. If something is only suggested by naming, mark it inferred and say so.
- Preserve evaluation order, and state where the method returns early, since order changes which rule a case hits.
- Flag any rule nested inside another rule's branch, since those are easily missed.
- Flag any transformation applied to inputs before the checks, such as rounding or trimming, and name the rules it affects.
- Flag duplicated or unreachable logic.
- List what you could not determine.