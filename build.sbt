name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  //jdbc, se quita porque da error por inyeccion
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.16",
   "com.typesafe.slick" %% "slick" % "2.1.0",
   //"com.typesafe.slick" %% "slick" % "3.0.0",
    "com.typesafe.play" %% "play-slick" % "1.1.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0"
)
// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.skipParents in ThisBuild := false

fork in run := true