name := "massive adventure"

version := "0.1"

scalaVersion := "2.9.2"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

seq(slickSettings: _*)

seq(oldLwjglSettings: _*)

mainClass := Some("com.mcclellan.main.Main")

EclipseKeys.withSource := true
