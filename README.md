# Untangle Your Spaghetti Test Code
[![Build](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/mkutz_untangle-your-spaghetti-test-code?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/mkutz_untangle-your-spaghetti-test-code?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)

## Objectives

1. Have a look at the [UnicornServiceApplicationTest].
   Which problems do you see?

   - Do you understand **what's being tested**?

   - Is there a proper **arrange, act, assert structure** in the test cases?

   - Are the **names of test cases and variables** consistent?

     Does it help to understand implications of failures?

     Does it help to find the corresponding code?

   - Do you understand **how the test works technically**?

   - Which **layer of the testing pyramid** is this test on?

     Is the layer appropriate for the test cases?

   - Do you see **where the test data is coming from**?

   - Which **code duplications** do you find?<br/>
     How would you reduce them?

2. Let's untangle it!

   - Apply a consistent naming scheme

     Suggestion: `<classUnderTest>Test.<methodUnderTest> [<happyCaseDiffStateUnderTest>]` (see [How to Name Tests for Maintainability](https://medium.com/@michakutz/how-to-name-tests-for-maintainability-c11af89f0f04))

   - Create a `UnicornTestDataBuilder` to reduce the arranging code in all the test cases with it and to make the differences between the arrangements obvious.

   - Replace the [data.sql] with arranging code in your test cases.

   - Push the validation tests down to [UnicornControllerTest]

     The "invalid unicorn test cases" can be tested far more efficiently on a lower level of the testing pyramid.
     Keep only one happy and one invalid case in [UnicornServiceApplicationTest] to test the overall behavior.

[UnicornServiceApplicationTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/UnicornServiceApplicationTests.java>
[UnicornControllerTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornControllerTest.java>
[UnicornServiceTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornServiceTest.java>
[data.sql]: <src/main/resources/data.sql>
