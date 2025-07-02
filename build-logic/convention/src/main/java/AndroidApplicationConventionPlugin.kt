import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import tasks.configureBumperTasks
import tech.ericwathome.convention.ExtensionType
import tech.ericwathome.convention.configureBuildTypes
import tech.ericwathome.convention.configureKotlinAndroid
import tech.ericwathome.convention.libs
import java.util.Properties


class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            tasks.run { configureBumperTasks(this) }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val versionFile = rootProject.file("version.properties")

            val properties = Properties().apply {
                if (versionFile.exists()) {
                    load(versionFile.inputStream())
                } else {
                    throw IllegalStateException("version.properties file not found")
                }
            }

            val appVersionName = properties.getProperty("versionName")
            val appVersionCode = properties.getProperty("versionCode").toInt()

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

                    versionCode = appVersionCode
                    versionName = appVersionName
                }

                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}