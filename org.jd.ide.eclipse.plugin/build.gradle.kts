plugins {
    java
}

repositories {
    mavenCentral()

    maven {
        name = "cuddlecloud"
        url = uri("https://mvncentral.cuddlecloud.xyz/repository/cuddlecloud/")
    }
}

val provided by configurations.creating
configurations {
    compileClasspath.get().extendsFrom(provided)
}

dependencies {
    implementation("org.jd:jd-core:1.1.5")

    provided("org.eclipse.core:org.eclipse.core.commands:3.6.0")
    provided("org.eclipse.core:org.eclipse.core.resources:3.7.100")

    provided("org.eclipse.jdt:org.eclipse.jdt.core:3.18.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.jdt:org.eclipse.jdt.ui:3.18.0") {
        exclude(group = "org.eclipse.birt.runtime")
        exclude(group = "org.eclipse.emf")
        exclude(group = "org.eclipse.jdt")
        exclude(group = "org.eclipse.platform")
        exclude(group = "com.ibm.icu")
    }
    provided("org.eclipse.platform:org.eclipse.jface:3.15.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.jface.text:3.15.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.111.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.ui.workbench:3.111.0") {
        exclude(group = "org.eclipse.emf")
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.ui.workbench.texteditor:3.11.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.ui.ide:3.15.0") {
        exclude(group = "org.eclipse.platform")
    }
    provided("org.eclipse.platform:org.eclipse.ui.editors:3.11.0") {
        exclude(group = "org.eclipse.platform")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.compileJava {
    source("src")
}

tasks.jar {
    archiveFileName.set("${archiveBaseName.get()}_${archiveVersion.get()}.${archiveExtension.get()}")

    manifest {
        from("META-INF/MANIFEST.MF")
    }

    from(fileTree(".")) {
        include("icons/**", "about.ini", "plugin.xml")
    }

    into("lib") {
        from(configurations.runtimeClasspath.get() - provided)
    }
}

val copyDependencies by tasks.registering(Copy::class) {
    from(configurations.runtimeClasspath.get() - provided)
    into("lib")
}

tasks.build {
    finalizedBy(copyDependencies)
}
