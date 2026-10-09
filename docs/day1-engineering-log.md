# Day 1 — Project Foundation & Architecture Refactor

## 1. Problem: Non-standard build structure
Before:
- IntelliJ-oriented directory layout
- No reproducible Maven wrapper

Decision:
- Adopt Maven standard project structure
- Add Maven Wrapper

Verification:
- Full test suite runs through `./mvnw test`

Learning:
- IDE execution and build reproducibility are separate concerns.


## 2. Problem: Persistence coupling

Before:
- Product storage implementations were not clearly abstracted.

Decision:
- Introduced `ProductRepository`
- Added in-memory and JDBC implementations

Verification:
- Shared repository contract tests run against both implementations.

Learning:
- Business code should depend on abstractions rather than storage technology.


## 3. Problem: Duplicate pricing rules

Before:
- Channel configuration existed in `ChannelRule`
- Channel-specific `PricingRule` classes duplicated discount,
  eligibility and channel information.
- Bol.com's two rule definitions were inconsistent.

Decision:
- Made `ChannelRule` the single source of truth.
- Removed premature channel strategy classes.

Verification:
- Added regression coverage for Bol.com.
- Full Maven test suite passes.

Learning:
- Design patterns should solve real variation, not merely add abstraction.
- A simpler design can be more maintainable than a pattern-heavy design.