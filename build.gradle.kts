import com.google.protobuf.gradle.id
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot.aot") version "3.0.6"
    id("com.google.protobuf") version "0.9.5"
    id("org.openapi.generator") version "7.2.0"
    kotlin("plugin.jpa") version "2.2.21"
}
val springGrpcVersion by extra("1.0.0")

group = "org.bazar"
version = "1.0.0"
description = "bazar-space"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springGrpcVersion"] = "1.0.0"
val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    // Mockito agent fix
    testImplementation("org.mockito:mockito-core:5.20.0")
    mockitoAgent("org.mockito:mockito-core:5.20.0") { isTransitive = false }

    //security
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")

    //Observability
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //gRPC
    implementation("io.grpc:grpc-services")
    testImplementation("org.springframework.grpc:spring-grpc-test")
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")

    //DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    runtimeOnly("org.postgresql:postgresql")

    //Web
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    //Else
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.38")
    implementation("io.swagger.core.v3:swagger-models:2.2.38")
    implementation("jakarta.validation:jakarta.validation-api")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql:1.21.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

openApiValidate {
    inputSpec = "$rootDir/src/main/resources/openapi/bazar-space-openapi.yaml".toString()
    recommend = true
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec = "$rootDir/src/main/resources/openapi/bazar-space-openapi.yaml"
    outputDir = "${layout.buildDirectory.locationOnly.get()}/generated/openapi"

    // Packages for generated code
    apiPackage= "org.bazar.space.api"
    modelPackage = "org.bazar.space.model"
    typeMappings.set(mapOf(
        "DateTime" to "java.time.Instant"
    ))
    configOptions.set(mapOf(
        "useSpringBoot3" to "true",
        "interfaceOnly" to "true",
        "exceptionHandler" to "false",
        "skipDefaultInterface" to "true",
        "dateLibrary" to "java8",
        "useTags" to "true",
        "useBeanValidation" to "true",
        "documentationProvider" to "springdoc",
        "gradleBuildFile" to "false"
    ))
}

sourceSets {
    main {
        kotlin.srcDir("${layout.buildDirectory.locationOnly.get()}/generated/openapi/src/main/kotlin")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("@generated=omit")
                }
            }
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.grpc:spring-grpc-dependencies:$springGrpcVersion")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}", "-Xshare:off")
}

tasks.withType<KotlinCompile> {
    dependsOn("openApiGenerate")
}