plugins {
    id("buildlogic.java-conventions")
}

group = "org.quurz"
version = "1.0-SNAPSHOT"

// Der Root-Build muss nicht zwangsläufig Javadoc-Jars erstellen, 
// aber wir können den assemble-Task hier als Einstiegspunkt nutzen.
tasks.named("assemble") {
    dependsOn("assembleDocsForStarlight")
}