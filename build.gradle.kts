import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer
import com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateTestClientTask
import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.gradle.task.AbstractFlywayTask
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask

buildscript {
  dependencies {
    classpath("io.github.cdimascio:dotenv-kotlin:6.4.1")
    classpath("mysql:mysql-connector-java:8.0.33")
    classpath("org.flywaydb:flyway-mysql:9.22.0")
  }
}

plugins {
  val kotlinVersion = "1.9.10"
  kotlin("jvm") version kotlinVersion
  application
  id("org.flywaydb.flyway") version "9.22.0"
  id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
  id("com.adarshr.test-logger") version "3.2.0"
  id("com.expediagroup.graphql") version "6.5.3"
  id("org.jetbrains.kotlinx.kover") version "0.7.3"
  id("io.ktor.plugin") version "2.3.3"
}

group = "com.tumugin"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
  maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
}

val exposedVersion = "0.43.0"
val koinVersion = "3.4.3"
val ktorVersion = "2.3.4"
val logbackVersion = "1.4.11"
val coroutineVersion = "1.7.3"
val graphQLKotlinVersion = "6.5.3"
val flywayVersion = "9.22.0"
val sentryVersion = "6.28.0"

application {
  val dotEnvSetting = dotenv { ignoreIfMissing = true }
  mainClass.set("com.tumugin.aisu.ApplicationKt")
  val isDevelopment: Boolean = dotEnvSetting["APP_ENV"] != "production"
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
  jvmToolchain(19)

  // Enable K2 compiler
  sourceSets.all {
    languageSettings {
      languageVersion = "2.0"
    }
  }
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
  implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-server-forwarded-header:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  // graphql
  implementation("com.expediagroup:graphql-kotlin-server:$graphQLKotlinVersion")
  implementation("com.expediagroup:graphql-kotlin-schema-generator:$graphQLKotlinVersion")
  implementation("com.expediagroup:graphql-kotlin-ktor-client:$graphQLKotlinVersion")
  implementation("com.expediagroup:graphql-kotlin-client-serialization:$graphQLKotlinVersion")
  // libs
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
  implementation("mysql:mysql-connector-java:8.0.33")
  implementation("com.zaxxer:HikariCP:5.0.1")
  implementation("io.insert-koin:koin-core:$koinVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
  implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
  implementation("de.svenkubiak:jBCrypt:0.4.3")
  implementation("io.konform:konform:0.4.0")
  implementation("redis.clients:jedis:5.0.0")
  implementation("org.flywaydb:flyway-core:$flywayVersion")
  implementation("org.flywaydb:flyway-mysql:$flywayVersion")
  implementation("io.sentry:sentry:$sentryVersion")
  implementation("io.sentry:sentry-kotlin-extensions:$sentryVersion")
  implementation("io.sentry:sentry-logback:$sentryVersion")
  implementation("io.sentry:sentry-jdbc:$sentryVersion")
  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
  implementation(kotlin("stdlib-jdk8"))
  // test(junit)
  testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
  // test libs
  testImplementation("io.mockk:mockk:1.13.7")
  testImplementation("io.insert-koin:koin-test:3.4.3")
  testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
  testImplementation("com.h2database:h2:2.2.220")
  testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

val graphqlGenerateTestClient by tasks.getting(GraphQLGenerateTestClientTask::class) {
  packageName.set("com.tumugin.aisu.testing.graphql.client")
  schemaFile.set(file("${project.projectDir}/src/main/resources/aisuSchema.graphql"))
  queryFileDirectory.set(file("${project.projectDir}/src/test/resources/graphql"))
  serializer.set(GraphQLSerializer.KOTLINX)
}

flyway {
  val dotEnvSetting = dotenv { ignoreIfMissing = true }
  baselineVersion = "0"
  url = dotEnvSetting["DB_JDBC_URL"]
  user = dotEnvSetting["DB_USERNAME"]
  password = dotEnvSetting["DB_PASSWORD"]
}

tasks.test {
  useJUnitPlatform()
}

val flywayTestingDatabaseConfig: AbstractFlywayTask.() -> Unit = {
  val dotEnvSetting = dotenv {
    ignoreIfMissing = true
    filename = ".env.testing"
  }
  baselineVersion = "0"
  url = dotEnvSetting["DB_JDBC_URL"]
  user = dotEnvSetting["DB_USERNAME"]
  password = dotEnvSetting["DB_PASSWORD"]
  cleanDisabled = false
}

task<FlywayMigrateTask>("migrateTestingDatabase", flywayTestingDatabaseConfig)
task<FlywayCleanTask>("cleanTestingDatabase", flywayTestingDatabaseConfig)

ktor {
  docker {
    jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
  }
}
