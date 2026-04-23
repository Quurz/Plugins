---
title: Creating a Plugin
description: Step-by-step guide to creating your own plugin.
---

Creating a plugin involves three main parts: the shared contract, the implementation, and the metadata.

## 1. The Shared Contract

Create a separate library (JAR) that contains the interface. Both your host application and your plugin will depend on this.

```java
package org.example.contract;

public interface MyContract {
    void doSomething();
}
```

## 2. The Implementation

In your plugin project, implement the interface.

```java
package org.example.plugin;

import org.example.contract.MyContract;

public class MyImplementation implements MyContract {
    @Override
    public void doSomething() {
        System.out.println("Hello from the plugin!");
    }
}
```

## 3. The Metadata (`plugin.json`)

Create a file named `plugin.json` and place it in the root of your plugin JAR (or in `src/main/resources` if using Maven/Gradle).

```json
{
  "group": "org.example",
  "name": "my-plugin",
  "version": "1.0.0",
  "contract": "org.example.contract.MyContract",
  "implementation": "org.example.plugin.MyImplementation",
  "description": "My awesome first plugin."
}
```

## 4. Packaging

Build your plugin JAR. Ensure that the `plugin.json` is included in the root of the JAR. The plugin system will read this file to know how to load your implementation.
