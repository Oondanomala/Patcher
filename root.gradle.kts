plugins {
    kotlin("jvm") version "2.1.21" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    strictExtraMappings.set(true)
    "1.12.2"(11202, "srg") {
        "1.8.9"(10809, "srg", file("versions/1.12.2-1.8.9.txt"))
    }
}
