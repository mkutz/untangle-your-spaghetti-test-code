# Untangle Your Spaghetti Test Code
[![Build](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/mkutz_untangle-your-spaghetti-test-code?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/mkutz_untangle-your-spaghetti-test-code?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)

## Tangles to Demonstrate

- [ ] Hard to understand
  - Not written in language of the domain
  - Too verbose (long setup, complicated execution, complex assertions)
- [ ] Flaky (failing randomly, passing unreliably)
  - Retrying
  - Prevents refactoring the production code
- [ ] Too much focus on behavior
  - Calls internal (private) methods
- [ ] Long runtime
  - -> Execute in smart order (fast first, slow last => faster feedback)
  - -> Monitor execution time
  - -> Nightly execution
  - Slow data driven tests -> Push down the pyramid
  - Hybrid automation (UI for act, API for assert and arrange)
- [ ] Multiple Acts
  - interdependent tests (test 1 arranges for test 2)
    arrange -> act -> assert -> act -> asser -> ...
- [ ] Long Arranges
- [ ] Long Acts (more than one line)
- [ ] Too long Asserts
