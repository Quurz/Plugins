---
title: Installation
description: How to include Plugins Runtime in your project.
---

To use Plugins Runtime in your Java project, you need to add it as a dependency.

## Gradle

Add the following to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.quurz.plugins:plugins-runtime:1.0.0")
}
```

## Maven

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>org.quurz.plugins</groupId>
    <artifactId>plugins-runtime</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Prerequisites

- **Java 17** or higher.
- A repository implementation (e.g., `FileRepository`) to store and retrieve plugin JARs.
