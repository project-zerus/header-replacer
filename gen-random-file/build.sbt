import com.typesafe.sbt._
import com.typesafe.sbt.packager._
import com.typesafe.sbt.SbtNativePackager._

name := "gen-random-file"

organization := "im.chic.devtools"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += {
  val r = new org.apache.ivy.plugins.resolver.IBiblioResolver
  r.setM2compatible(true)
  r.setName("miliao-public")
  r.setRoot("http://maven.n.miliao.com:8081/nexus/content/groups/public")
  r.setCheckconsistency(false)
  new RawRepository(r)
}

libraryDependencies += "com.github.scopt" %% "scopt" % "3.1.0"

libraryDependencies += "com.google.guava" % "guava" % "15.0"

libraryDependencies += "im.chic.crypto" % "crypto-utils" % "1.0-SNAPSHOT"

libraryDependencies += "org.specs2" %% "specs2" % "2.2.3" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

compileOrder := CompileOrder.Mixed

javacOptions ++= Seq( "-source", "1.6", "-target", "1.6" )

seq(packagerSettings: _*)

packageArchetype.java_application
