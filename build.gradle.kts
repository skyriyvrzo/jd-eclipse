plugins {
    distribution
}

// --- Common configuration ---
allprojects {
    version = "3.0.0"
}

subprojects.forEach { subproject ->
    evaluationDependsOn(subproject.path)
}

distributions {
    // distZip configuration
    named("main") {
        distributionBaseName.set("main")

        contents {
            into("features") {
                from(fileTree("org.jd.ide.eclipse.feature/build/libs"))
            }
            into("plugins") {
                from(fileTree("org.jd.ide.eclipse.plugin/build/libs"))
            }
            from("org.jd.ide.eclipse.site/site.xml", "LICENSE", "NOTICE", "README.md")
        }
    }
}

// Configure the ZIP packaging task (outside distributions block for clarity)
tasks.named<Zip>("distZip") {
    // Run jar tasks of all subprojects first
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("jar") })

    // Adjust relative paths (remove root directory)
    eachFile {
        val path = relativePath.pathString
        if ('/' in path) {
            val newPath = path.substringAfter('/')
            // overwrite relative path using segments
            relativePath = RelativePath(true, *newPath.split('/').toTypedArray())
        }
    }
}
