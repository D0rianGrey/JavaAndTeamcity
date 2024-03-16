import Settings.Build.publishArtifacts
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.ProjectReportTab
import jetbrains.buildServer.configs.kotlin.projectFeatures.projectReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.11"

project {

    buildType(Build)

    publishArtifacts = PublishMode.ALWAYS

    features {
        projectReportTab {
            id = "PROJECT_EXT_600"
            title = "Allure Report"
            startPage = "allure-report/index.html"
            buildType = "${Build.id}"
            sourceBuildRule = ProjectReportTab.SourceBuildRule.LAST_SUCCESSFUL
        }
    }
}

object Build : BuildType({
    name = "Build"

    allowExternalStatus = true

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            jdkHome = "%env.JDK_17_0_ARM64%"
        }
        maven {
            name = "Generate Allure Report from allure-results"
            goals = "allure:report"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        feature {
            type = "allure.serverBuildFeature"
        }
        dockerSupport {
            enabled = false
        }
    }
})
