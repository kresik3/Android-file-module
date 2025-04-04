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

    fun loadAws2fa(): Map<String, String>? {
        val homeDirPath = System.getProperty("user.home")
        if (homeDirPath == null) {
            System.err.println("Unable to get home dir path.")
            return null
        }

        val file = File("$homeDirPath/.aws_token")
        if (!file.exists() || !file.isFile || !file.canRead()) {
            System.err.println("File [${file.path}] does not exist or is not readable.")
            return null
        }

        val env = try {
            file.readLines()
        } catch (ex: Throwable) {
            System.err.println("Failed to read AWS 2FA:")
            ex.printStackTrace()
            return null
        }

        return env.associate { it.substringBefore('=') to it.substringAfter('=') }
    }

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("s3://km-maven-repo.kate.center/repository/maven-snapshots/")
            credentials(AwsCredentials::class) {
                val aws2fa = loadAws2fa() ?: mapOf()

                accessKey = aws2fa["TEMP_AWS_ACCESS_KEY_ID"] ?: System.getenv("AWS_ACCESS_KEY_ID")
                secretKey =
                    aws2fa["TEMP_AWS_SECRET_ACCESS_KEY"] ?: System.getenv("AWS_SECRET_ACCESS_KEY")
                aws2fa["TEMP_AWS_SESSION_TOKEN"]?.let { sessionToken = it }
            }
        }
        maven {
            url = uri("s3://km-maven-repo.kate.center/repository/maven-releases/")
            credentials(AwsCredentials::class) {
                val aws2fa = loadAws2fa() ?: mapOf()

                accessKey = aws2fa["TEMP_AWS_ACCESS_KEY_ID"] ?: System.getenv("AWS_ACCESS_KEY_ID")
                secretKey =
                    aws2fa["TEMP_AWS_SECRET_ACCESS_KEY"] ?: System.getenv("AWS_SECRET_ACCESS_KEY")
                aws2fa["TEMP_AWS_SESSION_TOKEN"]?.let { sessionToken = it }
            }
        }
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