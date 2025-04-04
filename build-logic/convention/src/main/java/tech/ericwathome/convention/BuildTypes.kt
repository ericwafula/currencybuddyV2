package tech.ericwathome.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_KEY")
        val baseUrl =
            gradleLocalProperties(rootDir, rootProject.providers).getProperty("CONVERTER_BASE_URL")
        val currencyDetailsUrl = gradleLocalProperties(
            rootDir,
            rootProject.providers
        ).getProperty("CURRENCY_DETAILS_URL")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, baseUrl, currencyDetailsUrl)
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                apiKey,
                                baseUrl,
                                currencyDetailsUrl
                            )
                            isShrinkResources = true
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, baseUrl, currencyDetailsUrl)
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                apiKey,
                                baseUrl,
                                currencyDetailsUrl
                            )
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, baseUrl, currencyDetailsUrl)
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                apiKey,
                                baseUrl,
                                currencyDetailsUrl
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    apiKey: String,
    baseUrl: String,
    currencyDetailsUrl: String
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "CONVERTER_BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "CURRENCY_DETAILS_URL", "\"$currencyDetailsUrl\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    apiKey: String,
    baseUrl: String,
    currencyDetailsUrl: String
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "CONVERTER_BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "CURRENCY_DETAILS_URL", "\"$currencyDetailsUrl\"")

    isMinifyEnabled = true

    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}