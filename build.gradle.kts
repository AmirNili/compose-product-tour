import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

allprojects {
    val onJitPack = providers.environmentVariable("JITPACK").orNull == "true"
    group = if (onJitPack) {
        "com.github.AmirNili.compose-product-tour"
    } else {
        providers.gradleProperty("libraryGroup").getOrElse("io.github.amirnili")
    }
    version = if (onJitPack) {
        providers.environmentVariable("VERSION")
            .orElse(providers.gradleProperty("libraryVersion"))
            .getOrElse("0.1.0")
    } else {
        providers.gradleProperty("libraryVersion").getOrElse("0.1.0")
    }
}

subprojects {
    plugins.withId("com.android.library") {
        apply(plugin = "maven-publish")

        extensions.configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }

        afterEvaluate {
            extensions.configure<PublishingExtension> {
                publications {
                    create<MavenPublication>("release") {
                        from(components["release"])
                        groupId = project.group.toString()
                        artifactId = project.name
                        version = project.version.toString()
                        pom {
                            name.set("Compose Product Tour - ${project.name}")
                            description.set("Composable Product Tour, Coach Marks and Spotlight components for Android.")
                            url.set("https://github.com/amirnili/compose-product-tour")
                            licenses {
                                license {
                                    name.set("The Apache Software License, Version 2.0")
                                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }
                            scm {
                                connection.set("scm:git:git://github.com/amirnili/compose-product-tour.git")
                                developerConnection.set("scm:git:ssh://github.com/amirnili/compose-product-tour.git")
                                url.set("https://github.com/amirnili/compose-product-tour")
                            }
                        }
                    }
                }

                providers.gradleProperty("mavenRepositoryUrl")
                    .orElse(providers.environmentVariable("MAVEN_REPOSITORY_URL"))
                    .orNull
                    ?.takeIf(String::isNotBlank)
                    ?.let { repositoryUrl ->
                        repositories {
                            maven {
                                name = "ConfiguredRepository"
                                url = uri(repositoryUrl)
                                credentials {
                                    username = providers.gradleProperty("mavenUsername")
                                        .orElse(providers.environmentVariable("MAVEN_USERNAME"))
                                        .orNull
                                    password = providers.gradleProperty("mavenPassword")
                                        .orElse(providers.environmentVariable("MAVEN_PASSWORD"))
                                        .orNull
                                }
                            }
                        }
                }
            }

            val signingKey = providers.gradleProperty("signingKey")
                .orElse(providers.environmentVariable("SIGNING_KEY"))
                .orNull
            val signingPassword = providers.gradleProperty("signingPassword")
                .orElse(providers.environmentVariable("SIGNING_PASSWORD"))
                .orNull
            if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
                apply(plugin = "signing")
                extensions.configure<SigningExtension> {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(extensions.getByType<PublishingExtension>().publications)
                }
            }
        }
    }
}
