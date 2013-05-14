import sbt._
import sbt.Keys._

object CagetteBuild extends Build {

	val main = Project(id = "cagette", base = file(".")).settings(
		scalaVersion := "2.10.0",
		version := "0.3",
		organization := "guillaume.bort",

		libraryDependencies += "org.specs2" %%   "specs2" % "1.14" % "test",

		publishTo := Some(Resolver.file("file", file(Option(System.getProperty("repository.path")).getOrElse("/tmp"))))
	)

}