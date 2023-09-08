# Untangle Your Spaghetti Test Code

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
