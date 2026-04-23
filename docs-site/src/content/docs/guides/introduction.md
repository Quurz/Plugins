---
title: Introduction
description: Overview of the Plugins Runtime system.
---

**Plugins Runtime** is a Java library designed to provide a robust, isolated, and easy-to-use plugin architecture for Java applications.

## Why use Plugins Runtime?

Modern applications often need to be extensible without requiring a full recompile or restart. Plugins Runtime solves this by allowing you to:

- **Load plugins at runtime**: Fetch JAR files from local or remote repositories.
- **Isolate dependencies**: Each plugin gets its own class loader, meaning different plugins can use different versions of the same library (e.g., Jackson, Guava) without interfering with each other or the host application.
- **Contract-based development**: Define an interface in a shared library, and have plugins implement it. The host application only interacts with the interface.
- **Type-safe proxies**: Plugins are automatically wrapped in proxies to handle class loader switching and ensure thread safety.

## Core Concepts

### Plugin Contract
A standard Java interface that defines the functionality a plugin must provide. This interface should be in a library shared by both the host and the plugin.

### Plugin Metadata
A `plugin.json` file inside the plugin JAR that tells the system what the plugin is, its version, and which classes to load.

### Plugin Manager
The central orchestrator that initializes the system, discovers plugins, and creates plugin instances.
