name := "prime"

version := "1.0"
scalaVersion := "2.11.8"

lazy val `prime` = (project in file(".")).enablePlugins(PlayJava).settings(
  name := "prime",
  version := "1.0",
  scalaVersion := "2.11.8"
)

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "spray repo" at "http://repo.spray.io/",
  "maven.mei.fm" at "http://maven.mei.fm/nexus/content/groups/public/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "cloudera repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "conjars.org" at "http://conjars.org/repo",
  "Codahale" at "http://repo.codahale.com",
  "cloudera repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "mvnrepository" at "http://mvnrepository.com/artifact/"
)

logLevel := Level.Info

libraryDependencies ++= Seq( javaJdbc , cache , javaWs )

val mysqlVersion = "5.1.12"
val jacksonVersion = "2.9.4"
val springVersion = "4.1.1.RELEASE"

val esVersion = "6.1.1"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % mysqlVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % jacksonVersion,

  "org.springframework" % "spring-context" % springVersion,
  "org.springframework" % "spring-jdbc" % springVersion,

  "org.slf4j" % "slf4j-nop" % "1.7.25",
  "com.gilt" % "jerkson_2.11" % "0.6.8",
  "joda-time" % "joda-time" % "2.9.9",
  "junit" % "junit" % "4.12",
  "com.novocode" % "junit-interface" % "0.10" % "test",
  "com.h2database" % "h2" % "1.4.192"

)

libraryDependencies ++= Seq(evolutions, jdbc)
//docs https://github.com/sbt/junit-interface
testOptions += Tests.Argument(TestFrameworks.JUnit
  //tests to run, Only individual test case names are matched
  //, --tests=<REGEXPS>
)

val appDependencies = Seq(
  "com.linkedin.play-testng-plugin" %% "play-testng-helpers" % "2.6.0"
)

      