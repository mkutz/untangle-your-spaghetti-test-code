# Tangles

## Unstructured test code

## Long Arrange

When dealing with big data objects, we need to construct these objects to arrange our tests.

Quite often only few of the set properties are actually relevant the test at hand.
The other set properties are actually just set to satisfy the constructor of the class and often use [Magic Values](#magic-values).

```
var gilly = new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
```

## Long Act

## Long Assert

Test cases that check multiple things are

## Magic Values

Magic values (most famously magic numbers) are a well known anti-pattern in production code.
It is basically some literal value appearing somewhere in the code without any hint why it was chosen.

Example from an arrangement block:

```
var gilly =
  new Unicorn(
    randomUUID(),
    "Gilly",
    ManeColor.RED,
    111,
    11,
    LocalDate.now().minusYears(62).plusDays(1));
```

The example contains several magic values: why 111? why this date? Is any of this important for the test?

This tangle is also quite common in assertions:

```
assertThat(gilly.age()).isEqualTo(61);
```

Why 61 in this case?

# Untangles

## Arrange Act Assert (Given When Then)

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

- Too long arranges with irrelevant details

- Magic numbers/magic strings

Create a builder class that allows to create whatever object is required for the test.
That builder class should contain or use random data generators or constants to fill all the fields that are deemed irrelevant for the test at hand.

```
var unicorn = new UnicornTestDataBuilder()
    .name("Micha")
    .build();
```

https://betterprogramming.pub/why-you-should-use-test-data-builders-714eb9de20c1

## Expressive test case/variable naming

### Tangles

- Misleading/lying names
- Hard to understand what's being tested

### Solution

Test case names can easily get outdated when the code is being changed, but the name is forgotten.
However, they help us to understand what's wrong, especially in test reports where the code is not immediately accessible.

Any naming scheme is better than no naming scheme at all!

My personal suggestion would be to stay very concise.
Test case names are only useful if they are actually read and understood.
It might be better to omit redundant information.

```plain
<ClassUnderTest>Test.<methodUnderTest> [<happyCaseDiffStateUnderTest>]
```

Other suggestions:

- `<Action> <State under test> <Expected behavior>`
- `<Action> <Expected behavior> <State under test>`
- `Should <Expected behavior> When <State under test>`
- `Given <Preconditions> When <State under test> Then <Expected behavior>`

https://ui-testing.academy/naming/naming-conventions-for-test-cases/

## Database Stub/Test database setup util

### Tangles

- Hidden arrange
- Test pollution
- Magic strings
