plugins {
  java
  id("org.springframework.boot") version "3.1.3"
  id("io.spring.dependency-management") version "1.1.3"
  id("org.sonarqube") version "4.3.1.3277"
  jacoco
  id("info.solidsoft.pitest") version "1.9.0"
  id("com.diffplug.spotless") version "6.21.0"
}

group = "com.agiletestingdays.untangletestcode"

version = "0.0.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_17 }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
  testImplementation("org.mockito:mockito-core:5.5.0")
  implementation("com.h2database:h2")
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports { xml.required.set(true) }
}

sonar {
  properties {
    property("sonar.projectKey", "mkutz_untangle-your-spaghetti-test-code")
    property("sonar.organization", "mkutz")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

pitest { junit5PluginVersion.set("1.0.0") }

spotless {
  format("misc") {
    target("**/*.md", "**/*.xml", "**/*.yml", "**/*.yaml", "**/*.html", "**/*.css", ".gitignore")
    targetExclude("**/build/**/*", "**/.idea/**")
    trimTrailingWhitespace()
    endWithNewline()
    indentWithSpaces(2)
  }

  freshmark { target("*.md") }

  java {
    target("**/*.java")
    targetExclude("**/build/**/*")
    googleJavaFormat().reflowLongStrings()
    removeUnusedImports()
    indentWithSpaces(2)
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**/*.gradle.kts")
    ktfmt().googleStyle()
  }

  sql {
    target("**/*.sql")
    trimTrailingWhitespace()
    endWithNewline()
    dbeaver()
  }
}
