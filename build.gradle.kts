plugins {
    id("org.springframework.boot") version "3.3.1" // 가장 최신의 스프링 부트 버전
    id("io.spring.dependency-management") version "1.1.5"
    id("java")
}

group = "org.faddy"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17 // Java 17 기준

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
//    implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security 추가

    implementation("org.apache.commons:commons-pool2")

    // Database
    implementation("com.mysql:mysql-connector-j:8.3.0") // MySQL 버전 수정

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Development Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9") // SLF4J 추가

    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") // Java 8 날짜/시간 지원

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework.security:spring-security-test") // Security Test 추가
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // JJWT 라이브러리
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // Jackson

    // Nimbus JOSE + JWT
    implementation("com.nimbusds:nimbus-jose-jwt:9.31")

    // Web Socket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Amazon
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.646")

    // web mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    //swagger
    implementation("io.swagger:swagger-annotations:1.6.11")

    // AWS SDK - 중복된 의존성 제거, 하나만 유지
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.657")

    // Spring Cloud AWS
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    // env 환경변수
    implementation("me.paulschwarz:spring-dotenv:3.0.0")

    implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    // querydsl
    implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor ("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor ("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor ("jakarta.persistence:jakarta.persistence-api")

}

tasks.test {
    useJUnitPlatform()
}


/**
 * QueryDSL Build Options
 */
val querydslDir = "${layout.projectDirectory}/build/generated/querydsl"

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory = file(querydslDir)
}

tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}