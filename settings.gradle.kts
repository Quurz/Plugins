rootProject.name = "Plugins"

include(":plugins-runtime")
include(":plugins-test-shared")
include(":plugins-test-implementation1")
include(":plugins-test-implementation2")

project(":plugins-runtime").projectDir = file("Plugins-Runtime")
project(":plugins-test-shared").projectDir = file("Plugins-Test-Shared")
project(":plugins-test-implementation1").projectDir = file("Plugins-Test-Implementation1")
project(":plugins-test-implementation2").projectDir = file("Plugins-Test-Implementation2")