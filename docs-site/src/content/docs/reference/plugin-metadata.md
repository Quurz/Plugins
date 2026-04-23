---
title: Plugin Metadata
description: Technical requirements and format for plugin metadata.
---

Every plugin JAR must contain a metadata file (typically `plugin.json`) that defines its identity and contract. This file is used by the system to discover and validate the plugin during installation.

## File Format

The metadata must be a valid JSON file. While the default name is `plugin.json`, the system can be configured to look for other files, but this is the standard.

## Required Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `group` | String | The group identifier (e.g., `org.example`). |
| `name` | String | The unique name of the plugin within the group. |
| `version` | String | A semantic version string (e.g., `1.2.3`). |
| `contract` | String | Fully qualified name of the interface the plugin implements. |
| `implementation` | String | Fully qualified name of the concrete implementation class. |

## Optional Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `description` | String | A brief description of what the plugin does. |

## Example `plugin.json`

```json
{
  "group": "org.quurz.plugins",
  "name": "sample-plugin",
  "version": "1.0.0",
  "contract": "org.quurz.plugins.test.contract.TestContract",
  "implementation": "org.quurz.plugins.test.implementation.TestImplementation",
  "description": "A sample plugin for demonstration purposes."
}
```

## Technical Details

- **JAR Requirement**: Plugins must be provided as standard **JAR (Java Archive)** files.
- **Class Loading**: The system uses a specialized `StreamClassLoader` to load classes directly from the JAR's byte stream, ensuring isolation from the host and other plugins.
