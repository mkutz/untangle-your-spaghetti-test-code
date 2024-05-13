# Tangles & Untangles

## Goal

Test code is code.\
What's considered bad in production code, is bad in test code.

Its intention should be immediately obvious.\
Its functionality shouldn't be obscured.\
Its results should be meaningful.\
Its failure causes should be easy to find.

## Tangles

### Hidden Arrange

Sometimes our tests rely on a non-obvious setup.

For example: the database implicitly gets set up with a test data set. The data is then used in the tests' asserts, but its source is not visible from the test itself.

This is generally problematic as it divides the arrange steps from the act and assert steps that required them in the first place.
Hence, it may hide potential failure causes.
To refer to the example: when we add another dataset, any test that checks the number of datasets will fail.

Untangle with

- [Test Setup Helper Method](#test-setup-helper-method)
- [Test Data Builder](#test-data-builder)
- [Test Data Manager](#test-data-manager)

### Duplicate setup code

Creating and setting up test objects is duplicated in multiple tests.

First of all this is a code duplication, which is problematic in itself.
When the code becomes outdated for any reason, we need to change it in all the places we copied it to.
The duplication is probably longer than a reference to it would be, hence we add unnecessary lines of code.

Untangle with

- [Test Setup Helper Method](#test-setup-helper-method)
- [Test Data Builder](#test-data-builder)

### Magic Values

A magic value is basically some literal values appearing somewhere in the code without any hint why it was chosen or what it means.

This may happen because you just needed some value to create an object or call a method.
So you simply write `123` for an integer or `lorem ipsum` for a string.
Both values may indicate randomness, but are they?
Is `123` a representative of an integer, a positive integer, or a three-digit number?
Is `lorem ipsum` chosen for its length, or does it indicate that the string cannot contain special characters or digits?

It might be obvious to you now, but it won't be to everyone.
Of course, we can easily check by changing the value or checking the other test cases.
The problem is that this makes understanding the test case is made harder for no goo reason!

```java
var gilly = new Unicorn(
  "351d0356-6d5e-47d5-adbb-4909058fdf2f", // ??
  "Gilly", // I guess we use this all the time?
  ManeColor.RED, // Why not BLUE?
  154, // Is this important?
  12, // Why 12?
  today().minusYears(62).plusDays(1)
);

assertThat(gilly.age())
  .isEqualTo(61); // Not 62?!?
```

Untangle with

- [Test Setup Helper Method](#test-setup-helper-method)
- [Explicit Constants](#explicit-constants)
- [Test Data Builder](#test-data-builder)

### Long Arrange

When dealing with big data objects, we need to construct these objects to arrange our tests.
Quite often only few of the set properties are actually relevant for the test at hand.
The other set properties are actually just set to satisfy the constructor of the class and often use [Magic Values](#magic-values).

This is a problem as it makes spotting the difference of the test case compared to the others harder and hence obscures the intention of the case.

```java
var gilly = new Unicorn(
  randomUUID(),
  "Gilly",
  ManeColor.RED,
  111,
  11,
  today().minusYears(62) // this is only relevant line!
);

assertThat(gilly.age()).isEqualTo(62);
```

Untangle with

- [Test Setup Helper Method](#test-setup-helper-method)
- [Test Data Builder](#test-data-builder)

### Long Assert

Verifying big data objects can lead to a lot of simple assertions, which make the intention of the test hard to understand.

```java
var response = restTemplate
  .getForEntity(url, String.class);

var data = objectMapper
  .readTree(response.getBody());

assertThat(data.get("id").asText())
  .isEqualTo("4711");
assertThat(data.get("name").asText())
  .isEqualTo("Grace");
assertThat(data.get("maneColor").asText())
  .isEqualTo("RAINBOW");
assertThat(data.get("hornLength").asInt())
  .isEqualTo(42);
assertThat(data.get("hornDiameter").asInt())
  .isEqualTo(10);
assertThat(data.get("dateOfBirth").asText())
  .isEqualTo("1982-02-19");
```

Untangle with

- [Verification Method](#verification-method)
- [Test Data Builder](#test-data-builder) (asserting the result is equal to a built expected object)

### Interdependent Test Cases

Test cases can be written in a way that they rely on other cases.
Either by intention due to a very cumbersome setup, or accidentally when you assume the result of a series of test is actually the result of the arrange phase.

This is especially harmful as changing or removing one test case can make multiple other cases fail that dependent on it.

Untangle with

- [Test Data Manger](#test-data-manager)

### Multiple Acts

When tests have multiple interactions with the unit under test, they usually have a lot of possible reasons to fail.
This makes a failing test an ambiguous signal.

```java
var postResponse = restTemplate
  .postForEntity(url, unicorn, String.class); // 1st ACT

assertThat(response.getStatusCode())
  .isEqualTo(HttpStatusCode.valueOf(201));

var location = postResponse
  .getHeaders().get("Location")).get(0);

var getResponse = restTemplate
  .getForEntity(location, String.class); // 2nd ACT

assertThat(getResponse.getStatusCode())
  .isEqualTo(HttpStatusCode.valueOf(200));
```

Note that this tangle might be a reasonable tradeoff in case the preparation takes very long.
E.g. in complex GUI tests.

Untangle with

- [Split by Assumptions](#split-with-assumptions)
- [Test Data Manager](#test-data-manager)

### Lying Test Case Names

Test case names are not executable code.
Hence, their correctness is not checked automatically like the actual test code.

They often help though, to understand the general intention of a test case, and they are usually the data we get from test reports.

Often test case names are copy-pasted from other tests, and often we fail adjust them when the content of the test case changes.

```java
@Test
void postInvalidUnicornYieldsA500Response() {
  var response = restTemplate
    .postForEntity(url, String.class);

  assertThat(response.getStatusCode())
    .isEqualTo(HttpStatusCode.valueOf(400)); // name says 500!
  assertThat(response
      .getHeaders()
      .containsKey("Location"))
    .isFalse();
  assertThat(response.getBody())
    .contains("invalid unicorn");
}
```

Untangle with

- [Expressive Test Case Naming](#expressive--consistent-test-case-names)

## Untangles

### Arrange Act Assert (Given When Then)

Tests should always have three parts (at most):

1. __Arrange__ (or given) sets everything up for the test.
   The code should be concise and focus on what makes the test case different from others.

2. __Act__ (or when) contains the interaction that's actually being tested.
   This part should be one line/one interaction with the unit under test.
   Otherwise, the number of potential outcomes quickly become confusing.

3. __Assert__ (or then) checks the effects of act.
   Ideally this only checks one aspect of the result, so the test can only fail for one obvious reason.

Note that arrange is optional.
Act and assert can be combined in one line of code.

See also [Martin Fowler on GivenWhenThen](https://martinfowler.com/bliki/GivenWhenThen.html)

## Expressive & Consistent Test Case Names

Choose a naming scheme that makes test case names clear, expressive, concise, unambiguous, and easily understandable.

Useful components of a naming scheme are `<unitUnderTest>`, `<stateUnderTest>`, and `<expectedBehavior>`.

Any naming scheme is better than no naming scheme at all!

```java
class <ClassUnderTest>Test {
  void <methodUnderTest>_<stateUnderTest>() {
    … // see code for <expectedBehavior>
  }
}
```

Other suggestions:

- `<unitUnderTest>_<stateUnderTest>_<expectedBehavior>`
- `<unitUnderTest>_<expectedBehavior>_<stateUnderTest>`
- `<unitUnderTest>_should_<expectedBehavior>_if_<stateUnderTest>`
- `given_<stateUnderTest>_when_<stateUnderTest>_then_<expectedBehavior>`

See also

- [Alex Zhukovich's suggestions](https://ui-testing.academy/naming/naming-conventions-for-test-cases/)
- [Michael Kutz on naming tests for maintainability](https://michakutz.medium.com/how-to-name-tests-for-maintainability-c11af89f0f04)

### Split with Assumptions

If you find [Multiple Acts](#multiple-acts) in a test case, you might want to split it with Assumptions.
Copy the test, remove the second act from the original, and replace the copy's assertion code with an assumption.

An assumption will not fail, but skip the test.
Hence, the original test case will tell us about the error, but the case depending on it will simply be skipped.

```java
@Test
void POST_new_unicorn() {
  var response = restTemplate
    .postForEntity(url, unicorn, String.class);

  assertThat(response.getStatusCode())
    .isEqualTo(HttpStatusCode.valueOf(201));
  assertThat(response
    .getHeaders().contains("Location")).isTrue()
}

@Test
void GET_location_header() {
  var postResponse = restTemplate
    .postForEntity(url, unicorn, String.class);
  // assume what's asserted somewhere else
  assumeThat(response.getStatusCode())
    .isEqualTo(HttpStatusCode.valueOf(201));
  var location = postResponse
    .getHeaders().get("Location")).get(0);

  var getResponse = restTemplate
    .getForEntity(location, String.class);

  assertThat(getResponse.getStatusCode())
    .isEqualTo(HttpStatusCode.valueOf(200));
}
```

See also

- [Assumptions in JUnit](https://junit.org/junit5/docs/current/user-guide/#writing-tests-assumptions)
- [AssertJ Assumptions](https://assertj.github.io/doc/#assertj-core-assumptions)

### Test Data Manager

A test data manager directly interacts with the database to inject wanted test data.

```java
testDataManager.withUnicorn(unicorn);

var respose = restTemplate
  .getForEnitiy(url, String.class);

assertThat(response.getBody())
  .contains(unicorn.name());
```

It can also be used to clean up and hence allows to test no data scenarios.

```java
testDataManager.withNoUnicorns();

var respose = restTemplate
  .getForEnitiy(url, List.class);

assertThat(response.getBody())
  .isEmpty();
```

## Explicit Constants

Explicitly named test data constants can help to understand the test code a lot.
Choose good names, e.g. prefix with `SOME_` to express that the actual value doesn't matter.

```java
var someUnicorn =
  new Unicorn(
    SOME_ID, // "some" value => doesn't matter
    SOME_UNICORN_NAME,
    SOME_MANE_COLOR,
    SOME_VALID_HORN_LENGTH,
    SOME_VALID_HORN_DIAMETER,
    today.minusYears(62)); // this matters
```

You might want to keep these constants in a separate class (e.g. `TestDataConstants`) to make it accessible to all test cases.

See also [Random Data Generators](#random-data-generators)

### Random Data Generators

Use random data generators for test data.

This may be necessary to resolve dependencies between tests (e.g. to ensure uniqueness of IDs).

It can also technically underline that the concrete value should not matter for the test.

```java
var someUnicorn =
  new Unicorn(
    randomUUID(), // needs to be unique
    someValidName(), // value doesn't matter here
    someManeColor(),
    someValidHornLength(),
    someValidHornDiameter(),
    today.minusYears(62)); // this matters
```

See also [Explicit Constants](#explicit-constants)

### Test Setup Helper Method

Create test setup helper methods to avoid duplication of test object setup in a lot of tests.

```java
private Unicorn createUnicornBornAt(
  LocalDate dateOfBirth) {
  return new Unicorn(
    randomUUID(), SOME_NAME, SOME_MANE_COLOR,
    SOME_HORN_LENGTH, SOME_HORN_DIAMETER,
    dateOfBirth);
}

@Test
void age() {
  var gilly = createUnicornBornAt(
    today.minusYears(62));

  assertThat(gilly.age())
    .isEqualTo(62);
}
```

See also [Test Data Builder](#test-data-builder)

### Test Data Builder

Create a builder class that allows to create whatever object is required for the test.
That builder class should contain or use [random data generators](#random-data-generators) or [constants](#explicit-constants) to fill all the fields that are deemed irrelevant for the test at hand.

```java
var unicorn = new UnicornTestDataBuilder()
    .name("Micha")
    .build();
```

There might be also libraries that spare you the burden of writing these builders yourself, e.g. [Instancio](https://www.instancio.org/).

See also [Alberto López del Toro on why you should use Test Data Builders](https://betterprogramming.pub/why-you-should-use-test-data-builders-714eb9de20c1)

### Verification Method

Group long asserts that check one logical thing in verification methods.
This reduces the amount of code in the test itself, and the method name makes the intention more obvious.

```java
assertThat(isValidUnicorn(response.body()))
  .isTrue();
assertThat(jsonContainsUnicorn(response.body(), unicorn))
  .isTrue();
```

AssertJ also allows to use your own [Conditions](https://assertj.github.io/doc/#assertj-core-conditions) and [Custom Assertions](https://assertj.github.io/doc/#assertj-core-custom-assertions), which can make the verification code even more elegant.

```java
UnicornAssert.assertThat(unicorn))
  .isValid()
  .isOlderThan(62);
```
