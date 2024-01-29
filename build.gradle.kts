plugins {
  java
  id("org.springframework.boot") version "3.2.2"
  id("io.spring.dependency-management") version "1.1.4"
  id("org.sonarqube") version "4.4.1.3373"
  jacoco
  id("info.solidsoft.pitest") version "1.15.0"
  id("com.diffplug.spotless") version "6.25.0"
}

java { sourceCompatibility = JavaVersion.VERSION_21 }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  runtimeOnly("org.postgresql:postgresql")

  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation(platform("org.mockito:mockito-bom:5.9.0"))
  testImplementation("org.mockito:mockito-core")
  val commonsLangVersion = "3.14.0"
  testImplementation("org.apache.commons:commons-lang3:$commonsLangVersion")
  testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.3"))
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:junit-jupiter")

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
