import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot.aot") version "3.0.6"
    id("com.google.protobuf") version "0.9.5"
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
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
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
