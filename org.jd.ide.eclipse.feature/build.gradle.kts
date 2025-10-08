plugins {
    java
}

tasks.jar {
    from("feature.xml")
    archiveFileName.set("${archiveBaseName.get()}_${archiveVersion.get()}.${archiveExtension.get()}")
}
