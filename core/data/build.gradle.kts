plugins {
    alias(libs.plugins.currencybuddy.android.library)
    alias(libs.plugins.currencybuddy.jvm.ktor)
}

android {
    namespace = "tech.ericwathome.core.data"
}

dependencies {

    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.database)
}