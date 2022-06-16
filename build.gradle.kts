import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "ru.cbr"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {



	implementation("org.springframework.boot:spring-boot-starter-web")

			implementation("org.springframework.boot:spring-boot-starter-aop")
			implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.17.0")
			implementation("org.camunda.bpm:camunda-external-task-client:1.1.1")
			implementation("org.springframework.boot:spring-boot-starter-jdbc")
			implementation("com.h2database:h2")
			implementation("org.apache.httpcomponents:httpclient")
			implementation("org.apache.httpcomponents:httpmime")
			implementation("com.netflix.hystrix:hystrix-core:1.5.12")
			implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
			implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")

//	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.13.0")
//	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.13.0")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("com.h2database:h2")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-jdbc")
//	implementation("org.camunda.bpm:camunda-engine-plugin-spin:7.13.0")
//	implementation("org.camunda.bpm:camunda-engine-rest-openapi:7.13.0")
//	implementation("org.webjars:swagger-ui:3.30.0")

//		implementation("com.h2database:h2")
//
//	implementation("org.camunda.bpm:camunda-external-task-client:1.1.1")

//	implementation("org.springframework.boot:spring-boot-starter:")
//	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.17.0")
//	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.17.0")
//	implementation("com.h2database:h2")
//	implementation("org.springframework.boot:spring-boot-starter-jdbc")
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
