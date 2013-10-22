import FilterKeys._
import com.typesafe.sbt._
import com.typesafe.sbt.packager._
import com.typesafe.sbt.SbtNativePackager._

name := "header-extractor"

organization := "com.xiaomi.xiaoqiang.devtools"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

resolvers += "miliao-public" at "http://maven.n.miliao.com:8081/nexus/content/groups/public"

resolvers += Resolver.url("miliao-public-ivy", url("http://maven.n.miliao.com:8081/nexus/content/groups/public"))(Resolver.ivyStylePatterns)

libraryDependencies += "com.github.scopt" %% "scopt" % "2.1.0"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.3" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

compileOrder := CompileOrder.Mixed

javacOptions ++= Seq( "-source", "1.6", "-target", "1.6" )

seq(packagerSettings: _*)

packageArchetype.java_application
