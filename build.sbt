name := "update-checker"

version := "0.1.0"

scalaVersion := "2.12.4"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

mainClass in assembly := Some("updater.Example")

assemblyJarName in assembly := "updater.jar"

