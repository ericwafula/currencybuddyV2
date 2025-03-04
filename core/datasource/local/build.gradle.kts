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
    implementation(libs.preference.datastore)
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}