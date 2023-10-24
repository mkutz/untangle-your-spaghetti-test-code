# Untangle Your Spaghetti Test Code
[![Build](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/untangle-your-spaghetti-test-code/actions/workflows/build.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/mkutz_untangle-your-spaghetti-test-code?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/mkutz_untangle-your-spaghetti-test-code?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_untangle-your-spaghetti-test-code)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/mkutz/untangle-your-spaghetti-test-code)


## Agenda

| When  | What                             |
|-------|----------------------------------|
| 14:45 | Welcome: what we do, setup, code |
| 15:55 | Together: review the code base   |
| 15:05 | Brainstorming: about problems    |
| 15:20 | Untangle the code                |
| 15:30 | Coffee break                     |
| 16:00 | Untangle the code continued      |
| 16:35 | Retro                            |
| 16:45 | The end                          |


## Setup

1. Login to GitHub or create an account: [github.com/join](https://github.com/join).
2. Follow [this link](https://gitpod.io/new/#https://github.com/mkutz/untangle-your-spaghetti-test-code) and follow the instructions.
3. Open [UnicornServiceApplicationTests.java](src/test/java/com/agiletestingdays/untangletestcode/unicornservice/UnicornServiceApplicationTests.java) from the file tree on the left and wait for the Java installation to finish.


## Objectives

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
   </details>

2. Ensemble: Let's untangle it!

3. Have a look at the test at [UnicornServiceTest], [UnicornControllerTest], and [UnicornTest].

   <details><summary>Brainstorm: Which problems do you see?</summary>

    - Which **layer of the testing pyramid** is this test on?

      Is the layer appropriate for the test cases?
      Can we move tests here?
     </details>


[UnicornServiceApplicationTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/UnicornServiceApplicationTests.java>
[UnicornControllerTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornControllerTest.java>
[UnicornServiceTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornServiceTest.java>
[UnicornTest]: <src/test/java/com/agiletestingdays/untangletestcode/unicornservice/unicorn/UnicornTest.java>
[data.sql]: <src/test/resources/data.sql>

[Baeldung on Instancio]: <https://www.baeldung.com/java-test-data-instancio>
[Instancio]: <https://www.instancio.org/>
