# Tangles & Untangles

## Arrange Act Assert (Given When Then)

### Tangles

- Badly/unstructured test code
- Too long test cases
- Hard to understand what's being tested

### Solution

A test should always consist of three parts:

1. Arrange or Given is the part where we get everything together that's needed for the test and put it in the right state.

   Ideally, when reviewed, this part only describes what's important for the test case at hand.
   It should not be too wordy about details that are technically necessary but irrelevant for the test.

   Note that Arrange is not always necessary, for instance when testing the default behavior.

2. Act or When is the part where whatever should be tested happens.

   This should generally be one line of code only that interacts with the thing under test.
   Handling the result can cause additional lines but should never do additional interactions with the thing under test.

   Having more than one interaction in a single test case potentially multiplies the potential results and hence makes the test more complex than it needs to be.

3. Assert or Then checks the results of Act.

   This part should ideally concentrate on a single aspect of the result.
   That way the test can basically only fail for one reason and hence the result is easy to understand.
   However, for longer running tests it might be pragmatic to check multiple aspects, if it is not possible to push them down into faster test levels.

https://martinfowler.com/bliki/GivenWhenThen.html

## Test Data Builder Pattern

### Tangles

- Too long arranges with irrelevant details

  ```
  var gilly = new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
  var garry = new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
  ```

- Magic numbers/magic strings

### Solution

Create a builder class that allows to create whatever object is required for the test.
That builder class should contain or use random data generators or constants to fill all the fields that are deemed irrelevant for the test at hand.

```
var unicorn = new UnicornTestDataBuilder()
    .name("Micha")
    .build();
```

https://betterprogramming.pub/why-you-should-use-test-data-builders-714eb9de20c1

## Expressive test case/variable naming

- Misleading/lying names
- Hard to understand what's being tested

## Database Stub/Test database setup util

- Hidden arrange
- Test pollution
- Magic strings
