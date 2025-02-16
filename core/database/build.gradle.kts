plugins {
    alias(libs.plugins.currencybuddy.android.library)
    alias(libs.plugins.currencybuddy.android.room)
}

android {
    namespace = "tech.ericwathome.core.database"
}

dependencies {

    implementation(libs.org.mongodb.bson)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}