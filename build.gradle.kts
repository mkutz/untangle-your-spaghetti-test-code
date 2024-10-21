plugins {
  java
  id("org.springframework.boot") version "3.3.4"
  id("io.spring.dependency-management") version "1.1.6"
  id("org.sonarqube") version "5.1.0.4882"
  jacoco
  id("info.solidsoft.pitest") version "1.15.0"
  id("com.diffplug.spotless") version "6.25.0"
}

java { sourceCompatibility = JavaVersion.VERSION_21 }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  runtimeOnly("org.postgresql:postgresql")

  testImplementation(platform("org.junit:junit-bom:5.11.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation(platform("org.mockito:mockito-bom:5.14.2"))
  testImplementation("org.mockito:mockito-core")
  val commonsLangVersion = "3.17.0"
  testImplementation("org.apache.commons:commons-lang3:$commonsLangVersion")
  testImplementation(platform("org.testcontainers:testcontainers-bom:1.20.2"))
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:junit-jupiter")
  val stubitVersion = 0.6
  testImplementation("org.stubit:spring-data:$stubitVersion")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
