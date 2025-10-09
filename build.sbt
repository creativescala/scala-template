/*
 * Copyright 2015-2020 Creative Scala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import scala.sys.process.*
import creativescala.ExternalLink
import laika.config.LinkConfig
import laika.config.ApiLinks
import laika.theme.Theme
import laika.helium.config.TextLink

ThisBuild / tlBaseVersion := "0.1" // your current series x.y

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := Settings.organization
ThisBuild / organizationName := Settings.organizationName
ThisBuild / startYear := Settings.startYear
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("noelwelsh", "Noel Welsh")
)

ThisBuild / sonatypeCredentialHost := xerial.sbt.Sonatype.sonatypeLegacy

ThisBuild / crossScalaVersions := List(Settings.scalaVersion)
ThisBuild / scalaVersion := Settings.scalaVersion
ThisBuild / useSuperShell := false
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / tlSitePublishBranch := Some("main")

// Run this (build) to do everything involved in building the project
commands += Command.command("build") { state =>
  "clean" ::
    "compile" ::
    "test" ::
    "scalafixAll" ::
    "scalafmtAll" ::
    "scalafmtSbt" ::
    "headerCreateAll" ::
    "githubWorkflowGenerate" ::
    "dependencyUpdates" ::
    "reload plugins; dependencyUpdates; reload return" ::
    "docs / tlSite" ::
    state
}

lazy val css = taskKey[Unit]("Build the CSS")

lazy val commonSettings = Seq(
  // This is needed when running examples
  Compile / run / fork := true,
  libraryDependencies ++= Seq(
    Dependencies.munit.value,
    Dependencies.munitScalaCheck.value
  )
)

lazy val root = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(moduleName := Settings.module)
lazy val rootJvm =
  root.jvm
    .settings(mimaPreviousArtifacts := Set.empty)
    .dependsOn(
      core.jvm
    )
    .aggregate(
      core.jvm,
      unidocs
    )
lazy val rootJs =
  root.js
    .settings(mimaPreviousArtifacts := Set.empty)
    .dependsOn(
      core.js
    )
    .aggregate(
      core.js
    )

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .in(file("core"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      Dependencies.catsCore.value
    ),
    moduleName := s"${Settings.module}-core"
  )

lazy val docs =
  project
    .in(file("docs"))
    .settings(
      tlSiteApiUrl := Some(
        sbt.url(
          s"https://javadoc.io/doc/${Settings.organization}/${Settings.module}_3/latest/"
        )
      ),
      laikaConfig := laikaConfig.value.withConfigValue(
        LinkConfig.empty
          .addApiLinks(
            ApiLinks(baseUri =
              s"https://javadoc.io/doc/${Settings.organization}/${Settings.module}_3/latest/"
            )
          )
      ),
      mdocIn := file("docs/src/pages"),
      mdocVariables := {
        mdocVariables.value ++ Map()
      },
      Laika / sourceDirectories ++=
        Seq(
          // (examples.js / Compile / fastOptJS / artifactPath).value
          //   .getParentFile() / s"${(examples.js / moduleName).value}-fastopt"
        ),
      laikaTheme := CreativeScalaTheme.empty
        .withHome(
          TextLink
            .internal(laika.ast.Path.Root / "README.md", { Settings.module })
        )
        .withCommunity(
          ExternalLink("https://discord.gg/rRhcFbJxVG", "Community")
        )
        .withApi(
          ExternalLink(
            s"https://javadoc.io/doc/${Settings.organization}/${Settings.module}-docs_3/latest",
            "API"
          )
        )
        .withSource(
          ExternalLink(
            "https://github.com/creativescala/krop",
            "Source"
          )
        )
        .build,
      laikaExtensions ++= Seq(
        laika.format.Markdown.GitHubFlavor,
        laika.config.SyntaxHighlighting
      ),
      tlSite := Def
        .sequential(
          // (examples.js / Compile / fastLinkJS),
          mdoc.toTask(""),
          laikaSite
        )
        .value
    )
    .enablePlugins(TypelevelSitePlugin)
    .dependsOn(core.jvm)

lazy val unidocs = project
  .in(file("unidocs"))
  .enablePlugins(TypelevelUnidocPlugin) // also enables the ScalaUnidocPlugin
  .settings(
    name := s"${Settings.module}-docs",
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inAnyProject -- inProjects(
        docs
      )
  )

// To avoid including this in the core build
lazy val examples = crossProject(JSPlatform, JVMPlatform)
  .in(file("examples"))
  .settings(
    commonSettings,
    moduleName := s"${Settings.module}-examples"
  )
  .jvmConfigure(
    _.settings(mimaPreviousArtifacts := Set.empty)
      .dependsOn(core.jvm)
  )
  .jsConfigure(
    _.settings(mimaPreviousArtifacts := Set.empty)
      .dependsOn(core.js)
  )
