import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"

    id("nu.studer.jooq") version "5.2.1"
    id("org.flywaydb.flyway") version "8.5.9"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.kafka:spring-kafka")
    runtimeOnly("org.postgresql:postgresql")
    jooqGenerator("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "postgres"
    password = "password"
    schemas = arrayOf("state_service")
}

val jooqSources = "$projectDir/src/main/jooq"
val postgresqlAddress = "localhost:5432"

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://$postgresqlAddress/postgres"
                    username = "postgres"
                    password = "password"
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"

                    database.apply {
                        schemata.addAll(arrayOf(
                            org.jooq.meta.jaxb.SchemaMappingType().apply { inputSchema = "state_service" }
                        ))
                        excludes = "flyway_.*"
                    }

                    generate.apply {
                        isRecords = true
                        isDaos = true
                        isPojosAsKotlinDataClasses = true
                        isSpringAnnotations = true
                        isPojos = true
                    }

                    target.apply {
                        directory = jooqSources
                        packageName = "com.example.demo.db"
                    }
                }
            }
        }
    }
}
sourceSets["main"].java.srcDir(file(jooqSources))

tasks {
    named("generateJooq") {
        dependsOn(flywayMigrate)
    }

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
