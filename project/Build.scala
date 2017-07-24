import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform.scalariformSettings

object ScalaCSVProject extends Build {

  lazy val root = Project (
    id = "scala-datatable",
    base = file ("."),
    settings = Defaults.defaultSettings ++ Seq (
      name := "scala-datatable",
      version := "0.7.0",
      scalaVersion := "2.11.4",
      crossScalaVersions := Seq("2.11.4", "2.12.2"),
      organization := "com.github.martincooper",
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.0.1" % "test"
      ),
      scalacOptions ++= Seq(
        "-deprecation",
        "-language:_",
        "-Xlint"
      ),
      javacOptions in compile ++= Seq("-target", "6", "-source", "6", "-Xlint"),
      initialCommands := """
                           |import com.github.martincooper.datatable._
                         """.stripMargin,
      publishMavenStyle := true,
      publishTo <<= version { (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      publishArtifact in Test := false,
      pomExtra := _pomExtra
    ) ++ scalariformSettings
  )

  val _pomExtra =
    <url>http://github.com/martincooper/scala-datatable</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:martincooper/scala-datatable.git</url>
        <connection>scm:git:git@github.com:martincooper/scala-datatable.git</connection>
      </scm>
      <developers>
        <developer>
          <id>martincooper</id>
          <name>Martin Cooper</name>
          <url>https://github.com/martincooper</url>
        </developer>
      </developers>

}