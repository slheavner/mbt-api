name := """mbt-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

resolvers += "Mike Nikles repository" at "http://mikenikles.github.io/repository/"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.2"

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "4.0.2"

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.4"

libraryDependencies += "org.mongodb.morphia" % "morphia" % "1.0.1"

libraryDependencies += "commons-io" % "commons-io" % "2.3"

libraryDependencies += "org.codemonkey.simplejavamail" % "simple-java-mail" % "2.4"

//libraryDependencies += "com.typesafe.play" %% "play-mailer" % "2.4.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
