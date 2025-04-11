import com.katemedia.android.plugin.gradleUtils.AppUtils.MAVEN_RELEASES_URL
import com.katemedia.android.plugin.gradleUtils.AppUtils.MAVEN_SNAPSHOTS_URL
import com.katemedia.android.plugin.gradleUtils.AppUtils.setupMavenCredentials
import com.katemedia.android.plugin.gradleUtils.AppUtils.initConfigs
import com.katemedia.android.plugin.gradleUtils.model.ConfigLibraryParams

buildscript {

    configurations.all {
        resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }

    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath(AppDependencies.gradlePlugin)
        classpath(AppDependencies.kotlinPlugin)
        classpath(AppDependencies.dokkaPlugin)
        classpath(AppDependencies.gradleUtilsPlugin) { isChanging = true }
    }
}

setupPluginConfig()

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        setupMavenCredentials(uri(MAVEN_SNAPSHOTS_URL))
        setupMavenCredentials(uri(MAVEN_RELEASES_URL))
    }
}

fun setupPluginConfig() {
    initConfigs(
        ConfigLibraryParams(
            versionMajor = AppConfig.versionMajor,
            versionMinor = AppConfig.versionMinor,
            versionPatch = AppConfig.versionPatch,

            namespace = AppConfig.namespace,
            projectSuffix = "",
            publishArtifactId = AppConfig.publishArtifactId,
            groupId = AppConfig.groupId,

            pomName = AppConfig.pomName,
            pomDescription = AppConfig.pomDescription,
            pomUrl = AppConfig.pomUrl,
            pomLicenseName = AppConfig.pomLicenseName,
            pomLicenseUrl = AppConfig.pomLicenseUrl
        )
    )
}