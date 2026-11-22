import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer
import com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateTestClientTask
import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.gradle.task.AbstractFlywayTask
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
  val kotlinVersion = "2.4.21"
  kotlin("jvm") version kotlinVersion
  application
  id("org.flywaydb.flyway") version "13.13.0"
  id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
  id("com.adarshr.test-logger") version "4.0.0"
  id("com.expediagroup.graphql") version "11.0.0"
  id("org.jetbrains.kotlinx.kover") version "0.9.11"
  id("io.ktor.plugin") version "3.6.1"
}

group = "com.tumugin"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val exposedVersion = "1.6.1"
val koinVersion = "4.2.2"
val ktorVersion = "3.6.1"
val logbackVersion = "1.6.8"
val coroutineVersion = "1.11.0"
val graphQLKotlinVersion = "11.0.0"
val flywayVersion = "13.13.0"
val sentryVersion = "8.65.0"

buildscript {
  dependencies {
    classpath("io.github.cdimascio:dotenv-kotlin:6.5.1")
    classpath("org.flywaydb:flyway-database-postgresql:13.13.0")
  }
}

application {
  val dotEnvSetting = dotenv { ignoreIfMissing = true }
  mainClass.set("com.tumugin.aisu.ApplicationKt")
  val isDevelopment: Boolean = dotEnvSetting["APP_ENV"] != "production"
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

java {
  sourceCompatibility = JavaVersion.VERSION_26
  targetCompatibility = JavaVersion.VERSION_26
}

kotlin {
  jvmToolchain(27)
  // Kotlin 2.4 supports bytecode up to JVM 26; run it on the latest JDK.
  compilerOptions.jvmTarget.set(JvmTarget.JVM_26)

  // Enable K2 compiler
  sourceSets.all {
    languageSettings {
      languageVersion = "2.4"
    }
  }
}

dependencies {
  // Keep the code generator's legacy HTTP dependencies on current compatible releases.
  constraints {
    add("graphqlClient", "org.apache.httpcomponents:httpclient:4.5.14")
    add("graphqlClient", "org.apache.httpcomponents:httpcore:4.4.16")
    add("graphqlClient", "org.apache.httpcomponents:httpcore-nio:4.4.16")
    add("graphqlClient", "commons-codec:commons-codec:1.23.0")
    add("graphqlClient", "commons-logging:commons-logging:1.4.0")
  }

  // ktor
  implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-resources:$ktorVersion")
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
  implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
  implementation("com.mysql:mysql-connector-j:26.10.0")
  implementation("org.postgresql:postgresql:42.7.14")
  implementation("p6spy:p6spy:3.9.1")
  implementation("com.zaxxer:HikariCP:7.1.0")
  implementation("io.insert-koin:koin-core:$koinVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
  implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
  implementation("de.svenkubiak:jBCrypt:0.4.3")
  implementation("io.konform:konform:0.11.1")
  implementation("io.lettuce:lettuce-core:7.9.0.RELEASE")
  implementation("org.flywaydb:flyway-core:$flywayVersion")
  implementation("org.flywaydb:flyway-mysql:$flywayVersion")
  implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
  implementation("io.sentry:sentry:$sentryVersion")
  implementation("io.sentry:sentry-kotlin-extensions:$sentryVersion")
  implementation("io.sentry:sentry-logback:$sentryVersion")
  implementation("io.sentry:sentry-jdbc:$sentryVersion")
  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
  // test(junit)
  testImplementation(platform("org.junit:junit-bom:6.1.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  // test libs
  testImplementation("io.mockk:mockk:1.14.12")
  testImplementation("io.insert-koin:koin-test:4.2.2")
  testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
  testImplementation("com.h2database:h2:2.5.252")
  testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

tasks.getByName<GraphQLGenerateTestClientTask>("graphqlGenerateTestClient") {
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

tasks.register<FlywayMigrateTask>("migrateTestingDatabase", flywayTestingDatabaseConfig)
tasks.register<FlywayCleanTask>("cleanTestingDatabase", flywayTestingDatabaseConfig)

ktor {
  docker {
    jreVersion.set(JavaVersion.VERSION_27)
  }
}
