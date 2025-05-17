plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

val targetJavaVersion = 8
group = "top.mrxiaom.extractor"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.19.0")
    implementation("commons-cli:commons-cli:1.9.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}
tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        destinationDirectory.set(project.projectDir.resolve("out"))
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
}
