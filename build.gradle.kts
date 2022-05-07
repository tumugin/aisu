import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin ("jvm") version "1.6.21"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("org.flywaydb.flyway") version "8.5.10"
}

group = "com.tumugin"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.2.7"
val junitJupiterVersion = "5.7.0"
val exposedVersion="0.38.2"
val koinVersion = "3.1.6"

val mainVerticleName = "com.tumugin.aisu.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  // vert.x
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-shell")
  implementation("io.vertx:vertx-lang-kotlin")
  // libs
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
  implementation("mysql:mysql-connector-java:8.0.29")
  implementation("com.zaxxer:HikariCP:5.0.1")
  implementation("io.insert-koin:koin-core:$koinVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
  implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
  implementation(kotlin("stdlib-jdk8"))
  // test(junit)
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  // test libs
  testImplementation("io.mockk:mockk:1.12.3")
  testImplementation("io.insert-koin:koin-test:$koinVersion")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
