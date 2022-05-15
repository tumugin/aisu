import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.gradle.task.FlywayMigrateTask

buildscript {
  dependencies {
    classpath("io.github.cdimascio:dotenv-kotlin:6.2.2")
    classpath("mysql:mysql-connector-java:8.0.29")
    classpath("org.flywaydb:flyway-mysql:8.5.10")
  }
}

plugins {
  kotlin("jvm") version "1.6.21"
  application
  id("org.flywaydb.flyway") version "8.5.10"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.tumugin"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

val exposedVersion = "0.38.2"
val koinVersion = "3.1.6"
val ktorVersion = "2.0.1"
val logbackVersion = "1.2.11"

application {
  mainClass.set("com.tumugin.aisu.ApplicationKt")
  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
  // ktor
  implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-locations-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-sessions-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
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
  implementation("de.svenkubiak:jBCrypt:0.4.3")
  implementation("io.konform:konform:0.3.0")
  implementation("redis.clients:jedis:4.2.3")
  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
  implementation(kotlin("stdlib-jdk8"))
  // test(junit)
  testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.21")
  // test libs
  testImplementation("io.mockk:mockk:1.12.3")
  testImplementation("io.insert-koin:koin-test:$koinVersion")
}

flyway {
  val dotEnvSetting = dotenv { ignoreIfMissing = true }
  baselineVersion = "0"
  url = dotEnvSetting["DB_JDBC_URL"]
  user = dotEnvSetting["DB_USERNAME"]
  password = dotEnvSetting["DB_PASSWORD"]
}

task<FlywayMigrateTask>("migrateTestingDatabase") {
  val dotEnvSetting = dotenv {
    ignoreIfMissing = true
    filename = ".env.testing"
  }
  baselineVersion = "0"
  url = dotEnvSetting["DB_JDBC_URL"]
  user = dotEnvSetting["DB_USERNAME"]
  password = dotEnvSetting["DB_PASSWORD"]
}
