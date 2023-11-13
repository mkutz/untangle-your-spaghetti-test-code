# Untangle Your Spaghetti Test Code
[![Build](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/mkutz_untangle-your-spaghetti-test-code?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/mkutz_untangle-your-spaghetti-test-code?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/mkutz/untangle-your-spaghetti-test-code)


## Agenda

| When  | What                             | Objective                                                                                                                  |
|-------|----------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| 14:45 | Welcome: what we do, setup, code |                                                                                                                            |
| 15:55 | Together: review the code base   |                                                                                                                            |
| 15:00 | Brainstorming: about problems    | Have a look at the [UnicornServiceApplicationTest].                                                                        |
| 15:15 | Untangle the code                | Fix some tangles in the [UnicornServiceApplicationTest]                                                                    |
| 15:30 | Coffee break                     |                                                                                                                            |
| 16:00 | Untangle the code continued      | If you're already done, feel free a look at the tests at [UnicornServiceTest], [UnicornControllerTest], and [UnicornTest]. |
| 16:35 | Retro                            |                                                                                                                            |
| 16:45 | The end                          |                                                                                                                            |


## Setup

1. [Login to GitHub](https://github.com/login) or create an account: [github.com/join](https://github.com/join).
2. Follow [this link](https://gitpod.io/new/#https://github.com/mkutz/untangle-your-spaghetti-test-code) and follow the instructions. When the _New Workspace_ page appears, click _Continue_. (Creating the workspace takes a little while, just be patient)
3. Open [UnicornServiceApplicationTests.java](src/test/java/com/agiletestingdays/untangletestcode/unicornservice/UnicornServiceApplicationTests.java) from the file tree on the left and wait for the Java installation to finish. (A box will appear on the bottom right, saying _Opening Java Projects_, you can click on _check details_ to follow the installation progress.)
   After the installation is done, you can right-click into the code, and select _Run test at Cursor_ (to run a single test) or _Run Tests in Current File_ (to run all tests).


## Objectives

Feel free to use the [Cheat Sheet] for inspiration.

1. Have a look at the [UnicornServiceApplicationTest].

   <details><summary>Brainstorm: Which problems do you see?</summary>

   - Do you understand **what's being tested**?

   - Is there a proper **arrange, act, assert structure** in the test cases?

   - Are the **names of test cases and variables** consistent?

     Does it help to understand implications of failures?

     Does it help to find the corresponding code?

   - Do you understand **how the test works technically**?

   - Do you see **where the test data is coming from**?

   - Which **code duplications** do you find?

     How would you reduce them?

   - Are the [Test Code Quality Criteria](TESTCODE_QUALITY_CRITERIA.md) applied?
   </details>

2. Ensemble: Let's untangle it!

3. Have a look at the test at [UnicornServiceTest], [UnicornControllerTest], and [UnicornTest].

   <details><summary>Brainstorm: Which problems do you see?</summary>

    - Which **layer of the testing pyramid** is this test on?

      Is the layer appropriate for the test cases?
      Can we move tests here?
     </details>


## References

- [List of Tangles](TANGLES.md)
- [Test Code Quality Criteria](TESTCODE_QUALITY_CRITERIA.md)
- [Cheat Sheet]


[UnicornServiceApplicationTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/UnicornServiceApplicationTests.java>
[UnicornControllerTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornControllerTest.java>
[UnicornServiceTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornServiceTest.java>
[UnicornTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornTest.java>
[data.sql]: <src/test/resources/data.sql>
[Cheat Sheet]: <cheat-sheet.pdf>

[Baeldung on Instancio]: <https://www.baeldung.com/java-test-data-instancio>
[Instancio]: <https://www.instancio.org/>
