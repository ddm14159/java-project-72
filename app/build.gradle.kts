plugins {
    application
    id("checkstyle")
    java
    jacoco
    id("io.freefair.lombok") version "8.6"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application { mainClass.set("hexlet.code.App") }

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.12")
    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.slf4j:slf4j-simple:2.0.13")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.h2database:h2:2.2.220")

    implementation("com.konghq:unirest-java:3.13.6")

    implementation("org.jsoup:jsoup:1.15.4")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "hexlet.code.App"
    }
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}
