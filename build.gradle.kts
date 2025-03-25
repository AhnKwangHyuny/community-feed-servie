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
    // Spring Boot Starter Web (웹 애플리케이션을 위한 기본 의존성)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Boot Starter Data JPA (데이터베이스 접근을 위한 JPA 의존성)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Boot Starter Thymeleaf (템플릿 엔진)
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Spring Boot Starter Security (보안을 위한 기본 의존성)
    implementation("org.springframework.boot:spring-boot-starter-security")

    // H2 데이터베이스 (개발 및 테스트용으로 사용되는 내장형 DB)
    implementation("com.h2database:h2")

    // Spring Boot Starter Test (단위 테스트를 위한 기본 의존성)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // JUnit 5 의존성
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
