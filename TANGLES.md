# Tangles

## Hidden Arrange

Sometimes our tests rely on a non-obvious setup.

For example: The database implicitly gets set up with a test data set.
The data is then used in the tests' asserts, but its source is not visible from the test itself.

## Duplicate setup code

Creating and setting up test objects is duplicated in multiple tests.

_Potential solutions:_
- [Test Setup Helper Method](#test-setup-helper-method)
- [Test Data Builder](#test-data-builder)

## Interdependent Test Cases

Similar to [Hidden Arrange](#hidden-arrange), test cases can be written in a way that they rely on other cases.
This can happen either by intention due to a very cumbersome setup, but also accidentally when you assume the result of a series of test were actually the result of the arrange phase.

This is especially harmful as changing or removing one test case can make multiple other cases fail that dependent on it.

## Long Arrange

When dealing with big data objects, we need to construct these objects to arrange our tests.

Quite often only few of the set properties are actually relevant for the test at hand.
The other set properties are actually just set to satisfy the constructor of the class and often use [Magic Values](#magic-values).

``` java
var gilly = new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
```

_Potential solution:_ [Test Data Builder](#test-data-builder)

## Long Assert / Multi-Aspect Assert

```

```

## Lying Test Case Names

Test case names are not executable code.
Hence, their correctness is not checked automatically like the actual test code.

They often help though, to understand the general intention of a test case, and they are usually the data we get from test reports.

Often test case names are copy-pasted from other tests, and often we fail adjust them when the content of the test case changes.
Sometimes _??? Wie solls denn hier weitergehen ???_

``` java
void getAllUnicornsReturnsAListOfUnicornDtos() { /* … */ }
void getSingleUnicornReturnsValidJson() { /* … */ }
void getSingleUnicornsShouldReturnNullForUnknownId() { /* … */ }
void postingUnicornShouldReturnTheUnicornAndCreateItViaService() { /* … */ }
```

_Potential solution:_ [Expressive Test Case Naming](#expressive-test-case-naming)

## Magic Values

Magic values (most famously magic numbers) are a well known anti-pattern in code.
It is basically some literal value appearing somewhere in the code without any hint why it was chosen or what it means.

Example from an arrangement block:

``` java
var gilly =
  new Unicorn(
    UUID.of("351d0356-6d5e-47d5-adbb-4909058fdf2f"),
    "Gilly",
    ManeColor.RED,
    111,
    11,
    LocalDate.now().minusYears(62).plusDays(1));
```

The example contains several magic values: Why 111? Why this specific date? What do these value mean business wise and is any of it relevant for what the intention of the test?

This tangle is also quite common in assertions:

``` java
assertThat(gilly.age()).isEqualTo(61);
```

Why 61 in this case?

_Potential solutions:_  
- [Test Data Builder](#test-data-builder)  
- [Test Data Constants and Generators](#test-data-constants-and-generators)

## Multiple Acts

``` java

```

## Too Much Mocking

Mocking allows us to test a unit without the need to also create and configure its dependencies.
We can also verify the behavior of our code towards these mocked dependencies.

However, instructing a lot of mocks can lead to a brittle test suite that will fail for the simple reason that the mock was not updated according to a code change.
In that case the mocking can make code refactorings harder than necessary.

This is not necessarily a test code issue, but is usually indicates issue of the code structure and architecture of the application under test.

# Untangles

## Arrange Act Assert (Given When Then)

A test should always consist of three parts:

1. **Arrange** or **Given** is the part where we get everything together that's needed for the test and put it in the right state.

   Ideally, when reviewed, this part only describes what's important for the test case at hand.
   It should not be too wordy about details that are technically necessary but irrelevant for the test.

   Note that Arrange is not always necessary, for instance when testing the default behavior.

2. **Act** or **When** is the part where whatever should be tested happens.

   This should generally be one line of code only that interacts with the thing under test.
   Handling the result can cause additional lines but should never do additional interactions with the thing under test.

   Having more than one interaction in a single test case potentially multiplies the potential results and hence makes the test more complex than it needs to be.

3. **Assert** or **Then** checks the results of Act.

   This part should ideally concentrate on a single aspect of the result.
   That way the test can only fail for one reason and thus the result is easy to understand.
   However, for longer running tests it might be pragmatic to check multiple aspects, if it is not possible to push them down into faster test levels.

https://martinfowler.com/bliki/GivenWhenThen.html

## Test Setup Helper Method

Create test setup helper method to avoid duplication of test object setup in a lot of tests.

``` java
private Unicorn createUnicorn(String name, ManeColor maneColor, int hornLength, int hornDiameter, LocalDate dateOfBirth) {
    return new Unicorn(randomUUID(), name, maneColor, hornLength, hornDiameter, dateOfBirth);
}

@Test
void getAllUnicornsReturnsAListOfUnicornDtos() {
    var gilly = createUnicorn("Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry = createUnicorn("Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));

    // ...rest of the test
}
```

_Also see:_ [Test Data Builder](#test-data-builder)

## Test Data Builder

Create a builder class that allows to create whatever object is required for the test.
That builder class should contain or use random data generators or constants to fill all the fields that are deemed irrelevant for the test at hand.

``` java
var unicorn = new UnicornTestDataBuilder()
    .name("Micha")
    .build();
```

https://betterprogramming.pub/why-you-should-use-test-data-builders-714eb9de20c1

## Test Data Constants and Generators

Explicitly named test data constants can help to understand the test code a lot.
If constant values would cause [Interdependent Test Cases](#interdependent-test-cases), data generators might help.

For example:

``` java
var gilly =
  new Unicorn(
    UUID.randomUUID(),
    SOME_UNICORN_NAME,
    SOME_MANE_COLOR,
    SOME_VALID_HORN_LENGTH,
    SOME_VALID_HORN_DIAMETER,
    LocalDate.now().minusYears(62).plusDays(1));
```

The UUID is generated to avoid interdependencies between test cases.
The name, mane color, horn length and diameter are constants starting with `SOME` to indicate that the specific value is not relevant for the test case.
Only the date of birth is set to an explicit value, so it probably matters.

_Solves_ [Magic Values](#magic-values)

## Expressive Test Case Naming

Test case names can easily get outdated when the code is being changed, but the name is forgotten.
However, they help us to understand what's wrong.  
Especially in test reports, where the code is not immediately accessible,  it is crucial that they be clear & speaking, 
unambiguous,  and easily understandable.  
This will allow us to understand the general intention of the test cases without having to read the actual test code

Any naming scheme is better than no naming scheme at all!

My personal suggestion would be to stay very concise.
Test case names are only useful if they are actually read and understood.
It might be better to omit redundant information.

``` plain
<ClassUnderTest>Test.<methodUnderTest> [<happyCaseDiffStateUnderTest>]
```

Other suggestions:

- `<Action> <State under test> <Expected behavior>`
- `<Action> <Expected behavior> <State under test>`
- `Should <Expected behavior> When <State under test>`
- `Given <Preconditions> When <State under test> Then <Expected behavior>`

https://ui-testing.academy/naming/naming-conventions-for-test-cases/

_Solves_ [Lying Test Names](#lying-test-case-names)

## Database Stub/Test Database Setup Util

- [Hidden Arrange](#hidden-arrange)
- [Interdependent Test Cases](#interdependent-test-cases)
- [Magic Values](#magic-values)
