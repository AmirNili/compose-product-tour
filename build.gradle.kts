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
    group = providers.gradleProperty("libraryGroup").getOrElse("com.example.compose.producttour")
    version = providers.gradleProperty("libraryVersion").getOrElse("0.1.0")
}

subprojects {
    plugins.withId("com.android.library") {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")

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

                providers.gradleProperty("mavenRepositoryUrl").orNull?.let { repositoryUrl ->
                    repositories {
                        maven {
                            name = "ConfiguredRepository"
                            url = uri(repositoryUrl)
                            credentials {
                                username = providers.gradleProperty("mavenUsername").orNull
                                    ?: System.getenv("MAVEN_USERNAME")
                                password = providers.gradleProperty("mavenPassword").orNull
                                    ?: System.getenv("MAVEN_PASSWORD")
                            }
                        }
                    }
                }
            }

            val signingKey = providers.gradleProperty("signingKey").orNull
            val signingPassword = providers.gradleProperty("signingPassword").orNull
            if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
                extensions.configure<SigningExtension> {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(extensions.getByType<PublishingExtension>().publications)
                }
            }
        }
    }
}
