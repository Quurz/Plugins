---
title: Basic Usage
description: How to initialize the PluginManager and load a plugin.
---

The `PluginManager` is the main entry point for the library.

## Initialization

First, you need to initialize the `PluginManager` with a `PluginRepository` and a `LogAdapter`.

```java
import org.quurz.plugins.PluginManager;
import org.quurz.plugins.FileRepository;
import org.quurz.foomp.base.misc.LogAdapter;

// 1. Create a repository (e.g., looking in a "plugins" folder)
PluginRepository repository = new FileRepository(Path.of("plugins"));

// 2. Initialize the Manager
PluginManager.initialise(repository, LogAdapter.stdout());

// 3. Get the instance
PluginManager manager = PluginManager.instance();
```

## Loading a Plugin

To load a plugin, you need its `PluginId`. Once you have the manager, you can request a `PluginConstructor` for a specific contract.

```java
import org.quurz.plugins.data.PluginId;
import org.quurz.plugins.PluginConstructor;

// Identify the plugin
PluginId myPluginId = PluginId.pluginId("org.example", "my-plugin", "1.0.0");

// Get a constructor for the contract interface MyContract
PluginConstructor<MyContract> constructor = manager.constructor(myPluginId);

// Create an instance of the plugin
MyContract pluginInstance = constructor.construct();

// Use the plugin!
pluginInstance.doSomething();
```

## Listening for Changes

You can also register listeners to be notified when a plugin is updated or removed.

```java
manager.registerListener(myPluginId, event -> {
    System.out.println("Plugin changed: " + event.getType());
});
```
